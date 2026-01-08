package com.example.akilli.mahkum.nakil_sistemi.service;

import com.example.akilli.mahkum.nakil_sistemi.model.Arac;
import com.example.akilli.mahkum.nakil_sistemi.repository.AracRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AracService {

    private final AracRepository aracRepository;

    public AracService(AracRepository aracRepository) {
        this.aracRepository = aracRepository;
    }

    // 1. Tüm araçları getir
    public List<Arac> tumAraclar() {
        try {
            return aracRepository.findAllByOrderByPlakaAsc();
        } catch (Exception e) {
            System.err.println("Tüm araçlar getirilirken hata: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 2. Araç bul (plaka ile)
    public Optional<Arac> aracBul(String plaka) {
        try {
            return aracRepository.findByPlaka(plaka);
        } catch (Exception e) {
            System.err.println("Araç bulunurken hata (plaka: " + plaka + "): " + e.getMessage());
            return Optional.empty();
        }
    }

    // 3. Araç bul (ID ile)
    public Optional<Arac> aracBulById(Long id) {
        try {
            return aracRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Araç bulunurken hata (ID: " + id + "): " + e.getMessage());
            return Optional.empty();
        }
    }

    // 4. Araç kaydet (oluştur veya güncelle)
    public Arac aracKaydet(Arac arac) {
        try {
            // Plaka format kontrolü
            if (arac.getPlaka() != null && !arac.getPlaka().matches("^[0-9]{2}[A-Z]{1,3}[0-9]{2,4}$")) {
                throw new RuntimeException("Geçersiz plaka formatı! Örnek: 34ABC123");
            }

            // Benzersiz plaka kontrolü
            if (arac.getPlaka() != null) {
                Optional<Arac> mevcutArac = aracRepository.findByPlaka(arac.getPlaka());
                if (mevcutArac.isPresent() && (arac.getId() == null || !mevcutArac.get().getId().equals(arac.getId()))) {
                    throw new RuntimeException("Bu plaka ile kayıtlı araç zaten var: " + arac.getPlaka());
                }
            }

            // Zaman damgalarını ayarla
            if (arac.getId() == null) {
                arac.setKayitTarihi(LocalDateTime.now());
            }
            arac.setGuncellemeTarihi(LocalDateTime.now());

            System.out.println("Araç kaydediliyor: " + arac.getPlaka());
            return aracRepository.save(arac);

        } catch (Exception e) {
            System.err.println("Araç kaydetme hatası: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Araç kaydedilemedi: " + e.getMessage(), e);
        }
    }

    // 5. Araç sil (ID ile)
    @Transactional
    public boolean aracSil(Long id) {
        try {
            if (!aracRepository.existsById(id)) {
                throw new RuntimeException("ID'si " + id + " olan araç bulunamadı!");
            }

            // Araç bağlı görevlerde kullanılıyor mu kontrol et
            Optional<Arac> aracOpt = aracRepository.findById(id);
            if (aracOpt.isPresent()) {
                Arac arac = aracOpt.get();
                if (!arac.getGorevler().isEmpty()) {
                    throw new RuntimeException("Bu araç " + arac.getGorevler().size() + " görevde kullanılıyor. Önce görevlerden çıkarın.");
                }
            }

            aracRepository.deleteById(id);
            System.out.println("Araç silindi: ID=" + id);
            return true;
        } catch (Exception e) {
            System.err.println("Araç silme hatası (ID: " + id + "): " + e.getMessage());
            throw new RuntimeException("Araç silinemedi: " + e.getMessage(), e);
        }
    }

    // 6. Araç sil (plaka ile)
    @Transactional
    public boolean aracSilByPlaka(String plaka) {
        try {
            Optional<Arac> aracOpt = aracRepository.findByPlaka(plaka);
            if (!aracOpt.isPresent()) {
                throw new RuntimeException("Plakası " + plaka + " olan araç bulunamadı!");
            }

            return aracSil(aracOpt.get().getId());
        } catch (Exception e) {
            System.err.println("Araç silme hatası (plaka: " + plaka + "): " + e.getMessage());
            throw new RuntimeException("Araç silinemedi: " + e.getMessage(), e);
        }
    }

    // Plaka kontrolü
    public boolean aracExistsByPlaka(String plaka) {
        try {
            return aracRepository.existsByPlaka(plaka);
        } catch (Exception e) {
            System.err.println("Plaka kontrol hatası: " + e.getMessage());
            return false;
        }
    }

    // 7. Aktif araçları getir
    public List<Arac> aktifAraclar() {
        try {
            return aracRepository.findByAktifTrueOrderByPlakaAsc();
        } catch (Exception e) {
            System.err.println("Aktif araçlar getirilirken hata: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 8. Bakımdaki araçları getir
    public List<Arac> bakimdakiAraclar() {
        try {
            return aracRepository.findByBakimdaTrueOrderByPlakaAsc();
        } catch (Exception e) {
            System.err.println("Bakımdaki araçlar getirilirken hata: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 9. Müsait araçları getir
    public List<Arac> musaitAraclar() {
        try {
            return aracRepository.findByAktifTrueAndBakimdaFalseAndServisteFalseOrderByPlakaAsc();
        } catch (Exception e) {
            System.err.println("Müsait araçlar getirilirken hata: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 10. Kapasiteye göre araçları getir
    public List<Arac> kapasiteyeGoreAraclar(int minKapasite) {
        try {
            return aracRepository.findByKapasiteGreaterThanEqual(minKapasite);
        } catch (Exception e) {
            System.err.println("Kapasiteye göre araçlar getirilirken hata: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 11. Tipe göre araçları getir
    public List<Arac> tipeGoreAraclar(Arac.AracTipi tip) {
        try {
            return aracRepository.findByTipOrderByPlakaAsc(tip);
        } catch (Exception e) {
            System.err.println("Tipe göre araçlar getirilirken hata: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 12. Araç ara (plaka, model veya marka)
    public List<Arac> aramaYap(String aramaKelimesi) {
        try {
            if (aramaKelimesi == null || aramaKelimesi.trim().isEmpty()) {
                return tumAraclar();
            }
            return aracRepository.findByPlakaContainingOrModelContainingOrMarkaContainingAllIgnoreCase(
                    aramaKelimesi, aramaKelimesi, aramaKelimesi);
        } catch (Exception e) {
            System.err.println("Araç arama hatası: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 13. Araç durumu güncelle (aktif/pasif)
    public Arac aracDurumGuncelle(Long id, boolean aktif) {
        try {
            Arac arac = aracRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            arac.setAktif(aktif);
            arac.setGuncellemeTarihi(LocalDateTime.now());

            System.out.println("Araç durumu güncellendi: " + arac.getPlaka() + " -> " + (aktif ? "Aktif" : "Pasif"));
            return aracRepository.save(arac);
        } catch (Exception e) {
            System.err.println("Araç durumu güncelleme hatası: " + e.getMessage());
            throw new RuntimeException("Araç durumu güncellenemedi: " + e.getMessage(), e);
        }
    }

    // 14. Araç bakıma al
    public Arac aracBakimaAl(Long id, boolean bakimda) {
        try {
            Arac arac = aracRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            arac.setBakimda(bakimda);
            if (bakimda) {
                arac.setServiste(true);
            }
            arac.setGuncellemeTarihi(LocalDateTime.now());

            System.out.println("Araç bakım durumu güncellendi: " + arac.getPlaka() + " -> " + (bakimda ? "Bakımda" : "Bakımda Değil"));
            return aracRepository.save(arac);
        } catch (Exception e) {
            System.err.println("Araç bakım durumu güncelleme hatası: " + e.getMessage());
            throw new RuntimeException("Araç bakım durumu güncellenemedi: " + e.getMessage(), e);
        }
    }

    // 15. Araç servise al
    public Arac aracServiseAl(Long id, boolean serviste) {
        try {
            Arac arac = aracRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            arac.setServiste(serviste);
            arac.setGuncellemeTarihi(LocalDateTime.now());

            System.out.println("Araç servis durumu güncellendi: " + arac.getPlaka() + " -> " + (serviste ? "Serviste" : "Serviste Değil"));
            return aracRepository.save(arac);
        } catch (Exception e) {
            System.err.println("Araç servis durumu güncelleme hatası: " + e.getMessage());
            throw new RuntimeException("Araç servis durumu güncellenemedi: " + e.getMessage(), e);
        }
    }

    // 16. İstatistikler
    public long toplamAracSayisi() {
        try {
            return aracRepository.count();
        } catch (Exception e) {
            System.err.println("Toplam araç sayısı getirme hatası: " + e.getMessage());
            return 0;
        }
    }

    public long aktifAracSayisi() {
        try {
            return aracRepository.countByAktifTrue();
        } catch (Exception e) {
            System.err.println("Aktif araç sayısı getirme hatası: " + e.getMessage());
            return 0;
        }
    }

    // DÜZELTİLDİ: 'müsaitAracSayisi' -> 'musaitAracSayisi'
    public long musaitAracSayisi() {
        try {
            return aracRepository.countByAktifTrueAndBakimdaFalseAndServisteFalse();
        } catch (Exception e) {
            System.err.println("Müsait araç sayısı getirme hatası: " + e.getMessage());
            return 0;
        }
    }

    public boolean plakaVarMi(String plaka) {
        try {
            return aracRepository.existsByPlaka(plaka);
        } catch (Exception e) {
            System.err.println("Plaka kontrol hatası: " + e.getMessage());
            return false;
        }
    }

    // 17. Bakım gereken araçları getir
    public List<Arac> bakimGerekenAraclar() {
        try {
            List<Arac> tumAraclar = aracRepository.findAll();
            List<Arac> bakimGerekenler = new ArrayList<>();
            LocalDateTime bugun = LocalDateTime.now();

            for (Arac arac : tumAraclar) {
                if (arac.getSonrakiBakimTarihi() != null && arac.getSonrakiBakimTarihi().isBefore(bugun)) {
                    bakimGerekenler.add(arac);
                }
            }

            System.out.println("Bakım gereken araç sayısı: " + bakimGerekenler.size());
            return bakimGerekenler;
        } catch (Exception e) {
            System.err.println("Bakım gereken araçlar getirme hatası: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 18. Sigortası geçersiz araçları getir
    public List<Arac> sigortasiGecersizAraclar() {
        try {
            List<Arac> tumAraclar = aracRepository.findAll();
            List<Arac> sigortasiGecersizler = new ArrayList<>();
            LocalDateTime bugun = LocalDateTime.now();

            for (Arac arac : tumAraclar) {
                if (arac.getSigortaBitisTarihi() != null && arac.getSigortaBitisTarihi().isBefore(bugun)) {
                    sigortasiGecersizler.add(arac);
                }
            }

            System.out.println("Sigortası geçersiz araç sayısı: " + sigortasiGecersizler.size());
            return sigortasiGecersizler;
        } catch (Exception e) {
            System.err.println("Sigortası geçersiz araçlar getirme hatası: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 19. Muayenesi geçersiz araçları getir
    public List<Arac> muayenesiGecersizAraclar() {
        try {
            List<Arac> tumAraclar = aracRepository.findAll();
            List<Arac> muayenesiGecersizler = new ArrayList<>();
            LocalDateTime bugun = LocalDateTime.now();

            for (Arac arac : tumAraclar) {
                if (arac.getMuayeneBitisTarihi() != null && arac.getMuayeneBitisTarihi().isBefore(bugun)) {
                    muayenesiGecersizler.add(arac);
                }
            }

            System.out.println("Muayenesi geçersiz araç sayısı: " + muayenesiGecersizler.size());
            return muayenesiGecersizler;
        } catch (Exception e) {
            System.err.println("Muayenesi geçersiz araçlar getirme hatası: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 20. Yakıtı düşük araçları getir
    public List<Arac> yakitiDusukAraclar() {
        try {
            List<Arac> tumAraclar = aracRepository.findAll();
            List<Arac> yakitiDusukler = new ArrayList<>();

            for (Arac arac : tumAraclar) {
                if (arac.getYakitSeviyesi() != null && arac.getYakitSeviyesi() < 20.0) {
                    yakitiDusukler.add(arac);
                }
            }

            System.out.println("Yakıtı düşük araç sayısı: " + yakitiDusukler.size());
            return yakitiDusukler;
        } catch (Exception e) {
            System.err.println("Yakıtı düşük araçlar getirme hatası: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 21. Araç konumunu güncelle
    public Arac aracKonumGuncelle(Long id, Double enlem, Double boylam, Double hiz) {
        try {
            Arac arac = aracRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            arac.setSonKonumEnlem(enlem);
            arac.setSonKonumBoylam(boylam);
            arac.setSonHiz(hiz);
            arac.setSonKonumZamani(LocalDateTime.now());
            arac.setSonIletisim(LocalDateTime.now());
            arac.setGuncellemeTarihi(LocalDateTime.now());

            System.out.println("Araç konumu güncellendi: " + arac.getPlaka() +
                    " -> " + enlem + ", " + boylam + " (Hız: " + hiz + " km/s)");
            return aracRepository.save(arac);
        } catch (Exception e) {
            System.err.println("Araç konumu güncelleme hatası: " + e.getMessage());
            throw new RuntimeException("Araç konumu güncellenemedi: " + e.getMessage(), e);
        }
    }

    // 22. Son iletişimi güncelle
    public Arac sonIletisimGuncelle(Long id) {
        try {
            Arac arac = aracRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            arac.setSonIletisim(LocalDateTime.now());
            arac.setGuncellemeTarihi(LocalDateTime.now());

            return aracRepository.save(arac);
        } catch (Exception e) {
            System.err.println("Son iletişim güncelleme hatası: " + e.getMessage());
            throw new RuntimeException("Son iletişim güncellenemedi: " + e.getMessage(), e);
        }
    }

    // 23. Araç tiplerini getir
    public Arac.AracTipi[] getAracTipleri() {
        return Arac.AracTipi.values();
    }

    // 24. Yakıt türlerini getir
    public List<String> getYakitTurleri() {
        List<String> yakitTurleri = new ArrayList<>();
        yakitTurleri.add("Dizel");
        yakitTurleri.add("Benzin");
        yakitTurleri.add("LPG");
        yakitTurleri.add("Elektrik");
        yakitTurleri.add("Hibrit");
        return yakitTurleri;
    }

    // 25. Araç istatistikleri (dashboard için)
    public Map<String, Object> aracIstatistikleri() {
        Map<String, Object> istatistikler = new HashMap<>();

        try {
            istatistikler.put("toplamArac", toplamAracSayisi());
            istatistikler.put("aktifArac", aktifAracSayisi());
            // DÜZELTİLDİ: 'müsaitArac' -> 'musaitArac'
            istatistikler.put("musaitArac", musaitAracSayisi());
            istatistikler.put("bakimdakiArac", bakimdakiAraclar().size());
            istatistikler.put("servistekiArac", aracRepository.countByServisteTrue());
            istatistikler.put("bakimGerekenArac", bakimGerekenAraclar().size());
            istatistikler.put("sigortasiGecersizArac", sigortasiGecersizAraclar().size());
            istatistikler.put("muayenesiGecersizArac", muayenesiGecersizAraclar().size());
            istatistikler.put("yakitiDusukArac", yakitiDusukAraclar().size());

            // Tip dağılımı
            Map<String, Long> tipDagilimi = new HashMap<>();
            for (Arac.AracTipi tip : Arac.AracTipi.values()) {
                long sayi = aracRepository.countByTip(tip);
                tipDagilimi.put(tip.getDisplayName(), sayi);
            }
            istatistikler.put("tipDagilimi", tipDagilimi);

            // Son 24 saatte iletişim kurulan araçlar
            LocalDateTime son24Saat = LocalDateTime.now().minusHours(24);
            long son24SaatIletisim = aracRepository.countBySonIletisimAfter(son24Saat);
            istatistikler.put("son24SaatIletisim", son24SaatIletisim);

        } catch (Exception e) {
            System.err.println("Araç istatistikleri hatası: " + e.getMessage());
        }

        return istatistikler;
    }

    // 26. Araç bakım bilgilerini güncelle
    public Arac bakimBilgisiGuncelle(Long id, LocalDateTime sonBakimTarihi, LocalDateTime sonrakiBakimTarihi, Long km) {
        try {
            Arac arac = aracRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            arac.setSonBakimTarihi(sonBakimTarihi);
            arac.setSonrakiBakimTarihi(sonrakiBakimTarihi);
            if (km != null) {
                arac.setKm(km);
            }
            arac.setGuncellemeTarihi(LocalDateTime.now());

            System.out.println("Araç bakım bilgileri güncellendi: " + arac.getPlaka());
            return aracRepository.save(arac);
        } catch (Exception e) {
            System.err.println("Araç bakım bilgileri güncelleme hatası: " + e.getMessage());
            throw new RuntimeException("Araç bakım bilgileri güncellenemedi: " + e.getMessage(), e);
        }
    }

    // 27. Araç yakıt bilgilerini güncelle
    public Arac yakitBilgisiGuncelle(Long id, String yakitTuru, Double yakitSeviyesi, Double ortalamaYakitTuketimi) {
        try {
            Arac arac = aracRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            arac.setYakitTuru(yakitTuru);
            arac.setYakitSeviyesi(yakitSeviyesi);
            arac.setOrtalamaYakitTuketimi(ortalamaYakitTuketimi);
            arac.setGuncellemeTarihi(LocalDateTime.now());

            System.out.println("Araç yakıt bilgileri güncellendi: " + arac.getPlaka());
            return aracRepository.save(arac);
        } catch (Exception e) {
            System.err.println("Araç yakıt bilgileri güncelleme hatası: " + e.getMessage());
            throw new RuntimeException("Araç yakıt bilgileri güncellenemedi: " + e.getMessage(), e);
        }
    }

    // 28. GPS cihaz bilgilerini güncelle
    public Arac gpsBilgisiGuncelle(Long id, String gpsCihazNo, Boolean kameraSistemi, Boolean guvenlikSistemi, Boolean iletisimSistemi) {
        try {
            Arac arac = aracRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            arac.setGpsCihazNo(gpsCihazNo);
            arac.setKameraSistemi(kameraSistemi);
            arac.setGuvenlikSistemi(guvenlikSistemi);
            arac.setIletisimSistemi(iletisimSistemi);
            arac.setGuncellemeTarihi(LocalDateTime.now());

            System.out.println("Araç GPS bilgileri güncellendi: " + arac.getPlaka());
            return aracRepository.save(arac);
        } catch (Exception e) {
            System.err.println("Araç GPS bilgileri güncelleme hatası: " + e.getMessage());
            throw new RuntimeException("Araç GPS bilgileri güncellenemedi: " + e.getMessage(), e);
        }
    }
}