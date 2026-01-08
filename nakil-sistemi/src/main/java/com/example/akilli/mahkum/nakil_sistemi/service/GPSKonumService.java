package com.example.akilli.mahkum.nakil_sistemi.service;

import com.example.akilli.mahkum.nakil_sistemi.model.GPSKonum;
import com.example.akilli.mahkum.nakil_sistemi.model.NakilGorevi;
import com.example.akilli.mahkum.nakil_sistemi.repository.GPSKonumRepository;
import com.example.akilli.mahkum.nakil_sistemi.service.IhlalTespitService;
import com.example.akilli.mahkum.nakil_sistemi.service.NakilGoreviService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class GPSKonumService {

    private final GPSKonumRepository gpsKonumRepository;
    private final IhlalTespitService ihlalTespitService;
    private final NakilGoreviService nakilGoreviService;

    // Demo simülasyon için değişkenler
    private final Map<Long, Boolean> demoSimulasyonMap = new ConcurrentHashMap<>();
    private final Map<Long, double[]> demoKonumMap = new ConcurrentHashMap<>();
    private final Map<Long, Integer> demoAdimMap = new ConcurrentHashMap<>();
    private final Map<Long, Double> demoHizMap = new ConcurrentHashMap<>();

    // Türkiye için koordinatlar (Silivri -> Kartal rotası)
    private static final double SILIVRI_ENLEM = 41.0730;
    private static final double SILIVRI_BOYLAM = 28.2460;
    private static final double KARTAL_ENLEM = 40.9100;
    private static final double KARTAL_BOYLAM = 29.1800;

    // Maksimum hız limiti
    private static final double MAKSIMUM_HIZ = 90.0;

    // Toplam simülasyon adımı
    private static final int TOPLAM_ADIM = 20;

    public GPSKonumService(GPSKonumRepository gpsKonumRepository,
                           IhlalTespitService ihlalTespitService,
                           NakilGoreviService nakilGoreviService) {
        this.gpsKonumRepository = gpsKonumRepository;
        this.ihlalTespitService = ihlalTespitService;
        this.nakilGoreviService = nakilGoreviService;
    }

    // Konum kaydetme
    public GPSKonum konumKaydet(GPSKonum konum) {
        if (konum.getNakilGorevi() == null) {
            throw new RuntimeException("Konum kaydedilemedi: Görev bilgisi eksik");
        }

        // Hız ihlali kontrolü
        if (konum.isHizIhlali(MAKSIMUM_HIZ)) {
            konum.setIhlalVar(true);
            konum.setIhlalAciklama("Hız ihlali: " + konum.getHiz() + " km/s (Maksimum: " + MAKSIMUM_HIZ + " km/s)");

            // İhlal kaydı oluştur
            ihlalTespitService.hizAsimiTespit(
                    konum.getNakilGorevi(),
                    konum.getHiz(),
                    MAKSIMUM_HIZ,
                    konum.getEnlem(),
                    konum.getBoylam(),
                    konum.getZamanDamgasi()
            );
        }

        return gpsKonumRepository.save(konum);
    }

    // Demo simülasyon başlat
    public void demoSimulasyonBaslat(Long gorevId) {
        NakilGorevi gorev = nakilGoreviService.gorevBul(gorevId)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + gorevId));

        // Başlangıç konumunu kaydet
        GPSKonum baslangicKonum = new GPSKonum();
        baslangicKonum.setEnlem(SILIVRI_ENLEM);
        baslangicKonum.setBoylam(SILIVRI_BOYLAM);
        baslangicKonum.setHiz(0.0);
        baslangicKonum.setNakilGorevi(gorev);
        baslangicKonum.setArac(gorev.getArac());
        baslangicKonum.setZamanDamgasi(LocalDateTime.now());
        konumKaydet(baslangicKonum);

        // Demo simülasyonu başlat
        demoSimulasyonMap.put(gorevId, true);
        demoKonumMap.put(gorevId, new double[]{SILIVRI_ENLEM, SILIVRI_BOYLAM});
        demoAdimMap.put(gorevId, 0);
        demoHizMap.put(gorevId, 0.0);

        System.out.println("Demo simülasyon başlatıldı: Görev #" + gorevId);
    }

    // Demo simülasyon durdur
    public void demoSimulasyonDurdur(Long gorevId) {
        demoSimulasyonMap.put(gorevId, false);
        System.out.println("Demo simülasyon durduruldu: Görev #" + gorevId);
    }

    // Demo simülasyon sıfırla
    public void demoSimulasyonSifirla(Long gorevId) {
        demoSimulasyonMap.remove(gorevId);
        demoKonumMap.remove(gorevId);
        demoAdimMap.remove(gorevId);
        demoHizMap.remove(gorevId);

        // Başlangıç konumunu tekrar kaydet
        NakilGorevi gorev = nakilGoreviService.gorevBul(gorevId)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + gorevId));

        GPSKonum baslangicKonum = new GPSKonum();
        baslangicKonum.setEnlem(SILIVRI_ENLEM);
        baslangicKonum.setBoylam(SILIVRI_BOYLAM);
        baslangicKonum.setHiz(0.0);
        baslangicKonum.setNakilGorevi(gorev);
        baslangicKonum.setArac(gorev.getArac());
        baslangicKonum.setZamanDamgasi(LocalDateTime.now());
        konumKaydet(baslangicKonum);

        System.out.println("Demo simülasyon sıfırlandı: Görev #" + gorevId);
    }

    // Demo simülasyon konum güncelle
    public GPSKonum demoSimulasyonKonumuGuncelle(Long gorevId) {
        if (!demoSimulasyonMap.getOrDefault(gorevId, false)) {
            throw new RuntimeException("Demo simülasyon aktif değil");
        }

        NakilGorevi gorev = nakilGoreviService.gorevBul(gorevId)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + gorevId));

        int mevcutAdim = demoAdimMap.getOrDefault(gorevId, 0);
        double[] mevcutKonum = demoKonumMap.getOrDefault(gorevId, new double[]{SILIVRI_ENLEM, SILIVRI_BOYLAM});

        // Son adıma gelindiyse varış noktasına ulaş
        if (mevcutAdim >= TOPLAM_ADIM) {
            // Varış noktasına konum kaydet
            GPSKonum varisKonum = new GPSKonum();
            varisKonum.setEnlem(KARTAL_ENLEM);
            varisKonum.setBoylam(KARTAL_BOYLAM);
            varisKonum.setHiz(0.0);
            varisKonum.setNakilGorevi(gorev);
            varisKonum.setArac(gorev.getArac());
            varisKonum.setZamanDamgasi(LocalDateTime.now());

            // Görevi tamamla
            gorev.setDurum(NakilGorevi.GorevDurumu.TAMAMLANDI);
            nakilGoreviService.gorevKaydet(gorev);

            return konumKaydet(varisKonum);
        }

        // Yeni adım
        mevcutAdim++;

        // İlerleme oranı
        double ilerlemeOrani = (double) mevcutAdim / TOPLAM_ADIM;

        // Yeni konumu hesapla (varış noktasına doğru)
        double yeniEnlem = SILIVRI_ENLEM + (KARTAL_ENLEM - SILIVRI_ENLEM) * ilerlemeOrani;
        double yeniBoylam = SILIVRI_BOYLAM + (KARTAL_BOYLAM - SILIVRI_BOYLAM) * ilerlemeOrani;

        // Rastgele sapma ekle (gerçekçilik için)
        Random random = new Random();
        double rastgeleSapma = 0.01;
        double sapmaEnlem = (random.nextDouble() - 0.5) * rastgeleSapma * (1 - ilerlemeOrani);
        double sapmaBoylam = (random.nextDouble() - 0.5) * rastgeleSapma * (1 - ilerlemeOrani);

        yeniEnlem += sapmaEnlem;
        yeniBoylam += sapmaBoylam;

        // Hızı hesapla
        double hiz;
        if (ilerlemeOrani < 0.2) {
            // Başlangıç: hız yavaş artıyor
            hiz = 40 + random.nextDouble() * 20;
        } else if (ilerlemeOrani < 0.8) {
            // Orta bölüm: normal hız
            hiz = 70 + random.nextDouble() * 30;
        } else {
            // Son bölüm: yavaşlama
            hiz = 30 + random.nextDouble() * 20;
        }

        // %20 ihtimalle hız ihlali
        if (random.nextDouble() < 0.2) {
            hiz = 95 + random.nextDouble() * 20;
        }

        // Yeni konumu kaydet
        GPSKonum yeniKonum = new GPSKonum();
        yeniKonum.setEnlem(yeniEnlem);
        yeniKonum.setBoylam(yeniBoylam);
        yeniKonum.setHiz(hiz);
        yeniKonum.setNakilGorevi(gorev);
        yeniKonum.setArac(gorev.getArac());
        yeniKonum.setZamanDamgasi(LocalDateTime.now());

        // Hız ihlali kontrolü
        if (hiz > MAKSIMUM_HIZ) {
            yeniKonum.setIhlalVar(true);
            yeniKonum.setIhlalAciklama("Hız ihlali: " + hiz + " km/s");
        }

        // Demo verilerini güncelle
        demoAdimMap.put(gorevId, mevcutAdim);
        demoKonumMap.put(gorevId, new double[]{yeniEnlem, yeniBoylam});
        demoHizMap.put(gorevId, hiz);

        System.out.println("Demo güncellendi - Görev #" + gorevId +
                ", Adım: " + mevcutAdim +
                ", Hız: " + hiz);

        return konumKaydet(yeniKonum);
    }

    // Rota tamamlama oranı
    public double rotaTamamlamaOrani(Long gorevId) {
        try {
            GPSKonum sonKonum = sonKonumuGetir(gorevId);
            if (sonKonum == null) return 0.0;

            // Demo simülasyon aktifse, demo adımına göre hesapla
            if (demoSimulasyonMap.getOrDefault(gorevId, false)) {
                int mevcutAdim = demoAdimMap.getOrDefault(gorevId, 0);
                return Math.min(100.0, ((double) mevcutAdim / TOPLAM_ADIM) * 100.0);
            }

            // Normal hesaplama
            double toplamMesafe = mesafeHesapla(SILIVRI_ENLEM, SILIVRI_BOYLAM, KARTAL_ENLEM, KARTAL_BOYLAM);
            double gidilenMesafe = mesafeHesapla(SILIVRI_ENLEM, SILIVRI_BOYLAM,
                    sonKonum.getEnlem(), sonKonum.getBoylam());

            double oran = (gidilenMesafe / toplamMesafe) * 100.0;
            return Math.min(oran, 100.0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    // Mesafe hesaplama
    private double mesafeHesapla(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return 6371 * c;
    }

    // Son konumu getir
    public GPSKonum sonKonumuGetir(Long gorevId) {
        try {
            List<GPSKonum> konumlar = gpsKonumRepository.findTopByNakilGoreviIdOrderByZamanDamgasiDesc(gorevId);
            if (konumlar != null && !konumlar.isEmpty()) {
                return konumlar.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Görevin konumlarını getir
    public List<GPSKonum> gorevinKonumlari(Long gorevId) {
        try {
            return gpsKonumRepository.findByNakilGoreviIdOrderByZamanDamgasiAsc(gorevId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // İhlal konumlarını getir
    public List<GPSKonum> ihlalKonumlari(Long gorevId) {
        try {
            return gpsKonumRepository.findByIhlalVarTrueAndNakilGoreviId(gorevId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}