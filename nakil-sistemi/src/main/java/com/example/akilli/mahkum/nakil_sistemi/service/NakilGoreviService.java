package com.example.akilli.mahkum.nakil_sistemi.service;

import com.example.akilli.mahkum.nakil_sistemi.model.Arac;
import com.example.akilli.mahkum.nakil_sistemi.model.Gorevli;
import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import com.example.akilli.mahkum.nakil_sistemi.model.NakilGorevi;
import com.example.akilli.mahkum.nakil_sistemi.repository.AracRepository;
import com.example.akilli.mahkum.nakil_sistemi.repository.GorevliRepository;
import com.example.akilli.mahkum.nakil_sistemi.repository.MahkumRepository;
import com.example.akilli.mahkum.nakil_sistemi.repository.NakilGoreviRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NakilGoreviService {

    private final NakilGoreviRepository nakilGoreviRepository;
    private final MahkumRepository mahkumRepository;
    private final AracRepository aracRepository;
    private final GorevliRepository gorevliRepository;

    public NakilGoreviService(NakilGoreviRepository nakilGoreviRepository,
                              MahkumRepository mahkumRepository,
                              AracRepository aracRepository,
                              GorevliRepository gorevliRepository) {
        this.nakilGoreviRepository = nakilGoreviRepository;
        this.mahkumRepository = mahkumRepository;
        this.aracRepository = aracRepository;
        this.gorevliRepository = gorevliRepository;
    }

    // YENİ METOD: Tüm görevleri getir
    public List<NakilGorevi> tumGorevleriGetir() {
        return nakilGoreviRepository.findAll();
    }

    // Eski metod (tumGorevler) ile aynı işlevi görüyor, biri veya ikisi kullanılabilir
    public List<NakilGorevi> tumGorevler() {
        return nakilGoreviRepository.findAll();
    }

    public List<NakilGorevi> bugunkuGorevler() {
        LocalDateTime bugunBaslangic = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime bugunSon = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return nakilGoreviRepository.findBugunkuGorevler(bugunBaslangic, bugunSon);
    }

    public Optional<NakilGorevi> gorevBul(Long id) {
        return nakilGoreviRepository.findById(id);
    }

    // YENİ METOD: Controller için basit görev bulma (Optional olmayan)
    public NakilGorevi gorevBulById(Long id) {
        return nakilGoreviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID'si " + id + " olan görev bulunamadı!"));
    }

    // GPSKonumService tarafından kullanılacak basit güncelleme metodu
    public NakilGorevi gorevKaydet(NakilGorevi gorev) {
        if (gorev == null) {
            throw new RuntimeException("Görev boş olamaz!");
        }

        return nakilGoreviRepository.save(gorev);
    }

    // Ana gorevKaydet metodu (Controller tarafından kullanılacak)
    public NakilGorevi gorevKaydet(NakilGorevi gorev, List<Long> mahkumIds, List<Long> gorevliIds) {
        return this.gorevKaydet(gorev, null, mahkumIds, gorevliIds);
    }

    // GÜNCELLENMİŞ METOD: Arac ID'sini de alan versiyon
    public NakilGorevi gorevKaydet(NakilGorevi gorev, Long aracId, List<Long> mahkumIds, List<Long> gorevliIds) {
        // ID kontrolü - eğer güncelleme ise mevcut görevi al
        NakilGorevi mevcutGorev = null;
        if (gorev.getId() != null) {
            mevcutGorev = nakilGoreviRepository.findById(gorev.getId())
                    .orElseThrow(() -> new RuntimeException("Güncellenecek görev bulunamadı!"));

            // Mevcut tarihi koru
            gorev.setOlusturulmaTarihi(mevcutGorev.getOlusturulmaTarihi());
        } else {
            // Yeni görev ise tarih oluştur
            gorev.setOlusturulmaTarihi(LocalDateTime.now());
        }

        // Arac kontrolü - GÜNCELLENMİŞ KISIM
        if (aracId != null) {
            // Araç ID ile bul
            Arac arac = aracRepository.findById(aracId)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı! ID: " + aracId));

            // Araç müsait mi kontrolü
            if (!arac.isAktif()) {
                throw new RuntimeException("Araç pasif durumda: " + arac.getPlaka());
            }
            if (arac.isBakimda()) {
                throw new RuntimeException("Araç bakımda: " + arac.getPlaka());
            }

            gorev.setArac(arac);
        } else if (gorev.getArac() != null && gorev.getArac().getId() != null) {
            // Eğer gorev nesnesinde arac varsa
            Arac arac = aracRepository.findById(gorev.getArac().getId())
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı! ID: " + gorev.getArac().getId()));

            // Araç müsait mi kontrolü
            if (!arac.isAktif()) {
                throw new RuntimeException("Araç pasif durumda: " + arac.getPlaka());
            }
            if (arac.isBakimda()) {
                throw new RuntimeException("Araç bakımda: " + arac.getPlaka());
            }

            gorev.setArac(arac);
        } else {
            throw new RuntimeException("Araç seçimi zorunludur!");
        }

        // Mahkumları yükle
        List<Mahkum> mahkumlar = new ArrayList<>();
        if (mahkumIds != null && !mahkumIds.isEmpty()) {
            // Kapasite kontrolü
            int toplamMahkum = mahkumIds.size();
            if (gorev.getArac().getKapasite() < toplamMahkum) {
                throw new RuntimeException("Araç kapasitesi yetersiz! Araç kapasitesi: " +
                        gorev.getArac().getKapasite() + ", Seçilen mahkum: " + toplamMahkum);
            }

            mahkumlar = mahkumRepository.findAllById(mahkumIds);
            if (mahkumlar.size() != mahkumIds.size()) {
                throw new RuntimeException("Bazı mahkumlar bulunamadı!");
            }

            // Mahkumların aktif olup olmadığını kontrol et
            for (Mahkum mahkum : mahkumlar) {
                if (!mahkum.isAktif()) {
                    throw new RuntimeException(mahkum.getAd() + " " + mahkum.getSoyad() + " adlı mahkum pasif durumda!");
                }

                // Mahkum başka bir aktif görevde mi kontrolü
                List<NakilGorevi> mahkumGorevleri = nakilGoreviRepository.findByMahkumId(mahkum.getId());
                for (NakilGorevi mg : mahkumGorevleri) {
                    if (mg.getId() != null && !mg.getId().equals(gorev.getId()) &&
                            (mg.getDurum() == NakilGorevi.GorevDurumu.PLANLANDI ||
                                    mg.getDurum() == NakilGorevi.GorevDurumu.DEVAM_EDIYOR ||
                                    mg.getDurum() == NakilGorevi.GorevDurumu.BASLADI)) {
                        throw new RuntimeException(mahkum.getAd() + " " + mahkum.getSoyad() +
                                " adlı mahkum zaten " + mg.getDurum() + " durumunda bir görevde!");
                    }
                }
            }
        } else {
            throw new RuntimeException("En az bir mahkum seçmelisiniz!");
        }
        gorev.setMahkumlar(mahkumlar);

        // Görevlileri yükle
        List<Gorevli> gorevliler = new ArrayList<>();
        if (gorevliIds != null && !gorevliIds.isEmpty()) {
            gorevliler = gorevliRepository.findAllById(gorevliIds);
            if (gorevliler.size() != gorevliIds.size()) {
                throw new RuntimeException("Bazı görevliler bulunamadı!");
            }

            // Görevlilerin aktif olup olmadığını kontrol et
            for (Gorevli gorevli : gorevliler) {
                if (!gorevli.isAktif()) {
                    throw new RuntimeException(gorevli.getAd() + " " + gorevli.getSoyad() + " adlı görevli pasif durumda!");
                }
            }

            // En az bir şoför kontrolü
            boolean hasSofor = gorevliler.stream()
                    .anyMatch(g -> g.getRol() == Gorevli.GorevliRol.SOFOR);
            if (!hasSofor) {
                throw new RuntimeException("Görevde en az bir şoför bulunmalıdır!");
            }
        } else {
            throw new RuntimeException("En az bir görevli seçmelisiniz!");
        }
        gorev.setGorevliler(gorevliler);

        // Tarih kontrolü: varış başlangıçtan sonra olmalı
        if (gorev.getPlanlananVaris().isBefore(gorev.getPlanlananBaslangic())) {
            throw new RuntimeException("Varış tarihi başlangıç tarihinden önce olamaz!");
        }

        // Güncelleme ise mevcut ilişkileri temizle (Cascade yoksa)
        if (mevcutGorev != null) {
            // İlişkileri temizle
            mevcutGorev.getMahkumlar().clear();
            mevcutGorev.getGorevliler().clear();
            nakilGoreviRepository.save(mevcutGorev); // Temizlenmiş hali kaydet
        }

        return nakilGoreviRepository.save(gorev);
    }

    public void gorevSil(Long id) {
        NakilGorevi gorev = nakilGoreviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID'si " + id + " olan görev bulunamadı!"));

        // Sadece planlanan veya iptal edilmiş görevler silinebilir
        if (gorev.getDurum() == NakilGorevi.GorevDurumu.DEVAM_EDIYOR) {
            throw new RuntimeException("Devam eden görevler silinemez!");
        }
        if (gorev.getDurum() == NakilGorevi.GorevDurumu.TAMAMLANDI) {
            throw new RuntimeException("Tamamlanan görevler silinemez!");
        }

        // İlişkileri temizle (Cascade olmadığı için manuel temizleme)
        gorev.getMahkumlar().clear();
        gorev.getGorevliler().clear();
        nakilGoreviRepository.save(gorev); // İlişkileri temizle

        nakilGoreviRepository.delete(gorev);
    }

    public void gorevBaslat(Long id) {
        NakilGorevi gorev = nakilGoreviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + id));

        if (gorev.getDurum() != NakilGorevi.GorevDurumu.PLANLANDI) {
            throw new RuntimeException("Sadece planlanan görevler başlatılabilir!");
        }

        if (gorev.getMahkumlar().isEmpty()) {
            throw new RuntimeException("Görevde mahkum bulunmuyor!");
        }

        if (gorev.getArac() == null) {
            throw new RuntimeException("Görevde araç bulunmuyor!");
        }

        // Araç kontrolü
        if (!gorev.getArac().isAktif()) {
            throw new RuntimeException("Araç pasif durumda!");
        }
        if (gorev.getArac().isBakimda()) {
            throw new RuntimeException("Araç bakımda!");
        }

        // Görevlilerde şoför kontrolü
        boolean hasSofor = gorev.getGorevliler().stream()
                .anyMatch(g -> g.getRol() == Gorevli.GorevliRol.SOFOR);
        if (!hasSofor) {
            throw new RuntimeException("Görevde şoför bulunmuyor!");
        }

        // Görevi BASLADI durumuna getir
        gorev.setDurum(NakilGorevi.GorevDurumu.BASLADI);
        nakilGoreviRepository.save(gorev);
    }

    public void gorevDevamEttir(Long id) {
        NakilGorevi gorev = nakilGoreviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + id));

        if (gorev.getDurum() != NakilGorevi.GorevDurumu.BASLADI) {
            throw new RuntimeException("Sadece başlamış görevler devam ettirilebilir!");
        }

        gorev.setDurum(NakilGorevi.GorevDurumu.DEVAM_EDIYOR);
        nakilGoreviRepository.save(gorev);
    }

    public void gorevTamamla(Long id) {
        NakilGorevi gorev = nakilGoreviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + id));

        if (gorev.getDurum() != NakilGorevi.GorevDurumu.DEVAM_EDIYOR &&
                gorev.getDurum() != NakilGorevi.GorevDurumu.BASLADI) {
            throw new RuntimeException("Sadece devam eden veya başlamış görevler tamamlanabilir!");
        }

        gorev.setDurum(NakilGorevi.GorevDurumu.TAMAMLANDI);
        gorev.setTamamlanmaTarihi(LocalDateTime.now());
        nakilGoreviRepository.save(gorev);
    }

    public List<NakilGorevi> durumaGoreGorevler(NakilGorevi.GorevDurumu durum) {
        return nakilGoreviRepository.findByDurum(durum);
    }

    // GPS takip için aktif görevler - Sadece DEVAM_EDIYOR durumundaki görevler
    public List<NakilGorevi> aktifGorevler() {
        return nakilGoreviRepository.findDevamEdenGorevler();
    }

    // Tüm aktif görevler (PLANLANDI, BASLADI ve DEVAM_EDIYOR)
    public List<NakilGorevi> tumAktifGorevler() {
        return nakilGoreviRepository.findAktifGorevler();
    }

    public List<NakilGorevi> aracaGoreGorevler(String plaka) {
        return nakilGoreviRepository.findByAracPlaka(plaka);
    }

    public List<NakilGorevi> mahkumaGoreGorevler(Long mahkumId) {
        return nakilGoreviRepository.findByMahkumId(mahkumId);
    }

    // GPS takip için yeni metod
    public List<NakilGorevi> gpsTakipIcinAktifGorevler() {
        return nakilGoreviRepository.findGpsTakipIcinAktifGorevler();
    }

    // YENİ METOD: Tamamlanan görevleri getir
    public List<NakilGorevi> tamamlananGorevleriGetir() {
        return nakilGoreviRepository.findByDurum(NakilGorevi.GorevDurumu.TAMAMLANDI);
    }

    // YENİ METOD: Tamamlanan görevleri tarihe göre sıralı getir
    public List<NakilGorevi> tamamlananGorevleriTariheGoreGetir() {
        List<NakilGorevi> tamamlananGorevler = nakilGoreviRepository.findByDurum(NakilGorevi.GorevDurumu.TAMAMLANDI);

        // Tamamlanma tarihine göre ters sırala (en yeni en üstte)
        tamamlananGorevler.sort((g1, g2) -> {
            if (g1.getTamamlanmaTarihi() == null && g2.getTamamlanmaTarihi() == null) {
                return 0;
            }
            if (g1.getTamamlanmaTarihi() == null) {
                return 1;
            }
            if (g2.getTamamlanmaTarihi() == null) {
                return -1;
            }
            return g2.getTamamlanmaTarihi().compareTo(g1.getTamamlanmaTarihi());
        });

        return tamamlananGorevler;
    }

    // YENİ METOD: Dashboard için istatistikler
    public NakilGoreviIstatistikleri getIstatistikler() {
        List<NakilGorevi> tumGorevler = nakilGoreviRepository.findAll();

        NakilGoreviIstatistikleri istatistikler = new NakilGoreviIstatistikleri();
        istatistikler.setToplamGorev(tumGorevler.size());

        long toplamPlanlandi = tumGorevler.stream()
                .filter(g -> g.getDurum() == NakilGorevi.GorevDurumu.PLANLANDI)
                .count();
        long toplamBasladi = tumGorevler.stream()
                .filter(g -> g.getDurum() == NakilGorevi.GorevDurumu.BASLADI)
                .count();
        long toplamDevamEden = tumGorevler.stream()
                .filter(g -> g.getDurum() == NakilGorevi.GorevDurumu.DEVAM_EDIYOR)
                .count();
        long toplamTamamlandi = tumGorevler.stream()
                .filter(g -> g.getDurum() == NakilGorevi.GorevDurumu.TAMAMLANDI)
                .count();
        long toplamIptalEdildi = tumGorevler.stream()
                .filter(g -> g.getDurum() == NakilGorevi.GorevDurumu.IPTAL_EDILDI)
                .count();

        istatistikler.setToplamPlanlandi((int) toplamPlanlandi);
        istatistikler.setToplamBasladi((int) toplamBasladi);
        istatistikler.setToplamDevamEden((int) toplamDevamEden);
        istatistikler.setToplamTamamlandi((int) toplamTamamlandi);
        istatistikler.setToplamIptalEdildi((int) toplamIptalEdildi);

        return istatistikler;
    }

    // İç statik sınıf istatistikler için
    public static class NakilGoreviIstatistikleri {
        private int toplamGorev;
        private int toplamPlanlandi;
        private int toplamBasladi;
        private int toplamDevamEden;
        private int toplamTamamlandi;
        private int toplamIptalEdildi;

        // Getters and Setters
        public int getToplamGorev() { return toplamGorev; }
        public void setToplamGorev(int toplamGorev) { this.toplamGorev = toplamGorev; }

        public int getToplamPlanlandi() { return toplamPlanlandi; }
        public void setToplamPlanlandi(int toplamPlanlandi) { this.toplamPlanlandi = toplamPlanlandi; }

        public int getToplamBasladi() { return toplamBasladi; }
        public void setToplamBasladi(int toplamBasladi) { this.toplamBasladi = toplamBasladi; }

        public int getToplamDevamEden() { return toplamDevamEden; }
        public void setToplamDevamEden(int toplamDevamEden) { this.toplamDevamEden = toplamDevamEden; }

        public int getToplamTamamlandi() { return toplamTamamlandi; }
        public void setToplamTamamlandi(int toplamTamamlandi) { this.toplamTamamlandi = toplamTamamlandi; }

        public int getToplamIptalEdildi() { return toplamIptalEdildi; }
        public void setToplamIptalEdildi(int toplamIptalEdildi) { this.toplamIptalEdildi = toplamIptalEdildi; }
    }

    // YENİ METOD: Görevi bul ve döndür (Controller için)
    public NakilGorevi getGorevById(Long id) {
        return nakilGoreviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + id));
    }
}