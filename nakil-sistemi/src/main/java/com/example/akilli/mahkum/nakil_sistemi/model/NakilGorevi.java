package com.example.akilli.mahkum.nakil_sistemi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nakil_gorevleri")
public class NakilGorevi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "baslangic_noktasi", nullable = false)
    private String baslangicNoktasi;

    @Column(name = "varis_noktasi", nullable = false)
    private String varisNoktasi;

    @Column(name = "planlanan_baslangic")
    private LocalDateTime planlananBaslangic;

    @Column(name = "planlanan_varis")
    private LocalDateTime planlananVaris;

    @Column(name = "gerceklesen_baslangic")
    private LocalDateTime gerceklesenBaslangic;

    @Column(name = "gerceklesen_varis")
    private LocalDateTime gerceklesenVaris;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GorevDurumu durum = GorevDurumu.PLANLANDI;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gorev_mahkum",
            joinColumns = @JoinColumn(name = "gorev_id"),
            inverseJoinColumns = @JoinColumn(name = "mahkum_id")
    )
    private List<Mahkum> mahkumlar = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gorev_gorevli",
            joinColumns = @JoinColumn(name = "gorev_id"),
            inverseJoinColumns = @JoinColumn(name = "gorevli_id")
    )
    private List<Gorevli> gorevliler = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arac_id")
    private Arac arac;

    @Column(name = "olusturulma_tarihi", nullable = false)
    private LocalDateTime olusturulmaTarihi = LocalDateTime.now();

    @Column(name = "guncelleme_tarihi")
    private LocalDateTime guncellemeTarihi = LocalDateTime.now();

    @Column(name = "tamamlanma_tarihi")
    private LocalDateTime tamamlanmaTarihi;

    @Column(name = "iptal_tarihi")
    private LocalDateTime iptalTarihi;

    @Column(name = "baslangic_tarihi")
    private LocalDateTime baslangicTarihi;

    @Column(name = "aciklama", length = 2000)
    private String aciklama;

    @Column(name = "toplam_mesafe")
    private Double toplamMesafe;

    @Column(name = "tahmini_sure_dakika")
    private Integer tahminiSureDakika;

    @Column(name = "gerceklesen_sure_dakika")
    private Integer gerceklesenSureDakika;

    @Column(name = "ortalama_hiz")
    private Double ortalamaHiz;

    @Column(name = "maksimum_hiz")
    private Double maksimumHiz;

    @Column(name = "toplam_ihlal_sayisi")
    private Integer toplamIhlalSayisi = 0;

    @Column(name = "aktif_mi")
    private Boolean aktifMi = true;

    @Column(name = "oncelik_seviyesi")
    private Integer oncelikSeviyesi = 5; // 1: En yüksek, 10: En düşük

    @Column(name = "guvenlik_seviyesi")
    private String guvenlikSeviyesi = "NORMAL"; // DÜŞÜK, NORMAL, YÜKSEK, ÇOK_YÜKSEK

    @Column(name = "rota_verisi", columnDefinition = "TEXT")
    private String rotaVerisi;

    @OneToMany(mappedBy = "nakilGorevi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GPSKonum> gpsKonumlar = new ArrayList<>();

    @OneToMany(mappedBy = "nakilGorevi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ihlal> ihlaller = new ArrayList<>();

    public enum GorevDurumu {
        PLANLANDI("Planlandı"),
        HAZIR("Hazır"),
        BASLADI("Başladı"),
        DEVAM_EDIYOR("Devam Ediyor"),
        TAMAMLANDI("Tamamlandı"),
        IPTAL_EDILDI("İptal Edildi"),
        ERTELENDI("Ertelendi"),
        ACIL_DURUM("Acil Durum"),
        BLOKE("Bloke"),
        DEVAM_EDEN("Devam Eden");

        private final String displayName;

        GorevDurumu(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public NakilGorevi() {
    }

    public NakilGorevi(String baslangicNoktasi, String varisNoktasi,
                       LocalDateTime planlananBaslangic, LocalDateTime planlananVaris) {
        this.baslangicNoktasi = baslangicNoktasi;
        this.varisNoktasi = varisNoktasi;
        this.planlananBaslangic = planlananBaslangic;
        this.planlananVaris = planlananVaris;
        this.olusturulmaTarihi = LocalDateTime.now();
        this.guncellemeTarihi = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslangicNoktasi() {
        return baslangicNoktasi;
    }

    public void setBaslangicNoktasi(String baslangicNoktasi) {
        this.baslangicNoktasi = baslangicNoktasi;
    }

    public String getVarisNoktasi() {
        return varisNoktasi;
    }

    public void setVarisNoktasi(String varisNoktasi) {
        this.varisNoktasi = varisNoktasi;
    }

    public LocalDateTime getPlanlananBaslangic() {
        return planlananBaslangic;
    }

    public void setPlanlananBaslangic(LocalDateTime planlananBaslangic) {
        this.planlananBaslangic = planlananBaslangic;
    }

    public LocalDateTime getPlanlananVaris() {
        return planlananVaris;
    }

    public void setPlanlananVaris(LocalDateTime planlananVaris) {
        this.planlananVaris = planlananVaris;
    }

    public LocalDateTime getGerceklesenBaslangic() {
        return gerceklesenBaslangic;
    }

    public void setGerceklesenBaslangic(LocalDateTime gerceklesenBaslangic) {
        this.gerceklesenBaslangic = gerceklesenBaslangic;
    }

    public LocalDateTime getGerceklesenVaris() {
        return gerceklesenVaris;
    }

    public void setGerceklesenVaris(LocalDateTime gerceklesenVaris) {
        this.gerceklesenVaris = gerceklesenVaris;
    }

    public GorevDurumu getDurum() {
        return durum;
    }

    public void setDurum(GorevDurumu durum) {
        this.durum = durum;
        this.guncellemeTarihi = LocalDateTime.now();

        // Duruma göre tarihleri güncelle
        switch (durum) {
            case BASLADI:
                if (this.gerceklesenBaslangic == null) {
                    this.gerceklesenBaslangic = LocalDateTime.now();
                }
                break;
            case TAMAMLANDI:
                if (this.gerceklesenVaris == null) {
                    this.gerceklesenVaris = LocalDateTime.now();
                }
                if (this.tamamlanmaTarihi == null) {
                    this.tamamlanmaTarihi = LocalDateTime.now();
                }
                break;
            case IPTAL_EDILDI:
                if (this.iptalTarihi == null) {
                    this.iptalTarihi = LocalDateTime.now();
                }
                break;
        }
    }

    public List<Mahkum> getMahkumlar() {
        return mahkumlar;
    }

    public void setMahkumlar(List<Mahkum> mahkumlar) {
        this.mahkumlar = mahkumlar;
    }

    public List<Gorevli> getGorevliler() {
        return gorevliler;
    }

    public void setGorevliler(List<Gorevli> gorevliler) {
        this.gorevliler = gorevliler;
    }

    public Arac getArac() {
        return arac;
    }

    public void setArac(Arac arac) {
        this.arac = arac;
    }

    public LocalDateTime getOlusturulmaTarihi() {
        return olusturulmaTarihi;
    }

    public void setOlusturulmaTarihi(LocalDateTime olusturulmaTarihi) {
        this.olusturulmaTarihi = olusturulmaTarihi;
    }

    public LocalDateTime getGuncellemeTarihi() {
        return guncellemeTarihi;
    }

    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) {
        this.guncellemeTarihi = guncellemeTarihi;
    }

    public LocalDateTime getTamamlanmaTarihi() {
        return tamamlanmaTarihi;
    }

    public void setTamamlanmaTarihi(LocalDateTime tamamlanmaTarihi) {
        this.tamamlanmaTarihi = tamamlanmaTarihi;
    }

    public LocalDateTime getIptalTarihi() {
        return iptalTarihi;
    }

    public void setIptalTarihi(LocalDateTime iptalTarihi) {
        this.iptalTarihi = iptalTarihi;
    }

    public LocalDateTime getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public void setBaslangicTarihi(LocalDateTime baslangicTarihi) {
        this.baslangicTarihi = baslangicTarihi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Double getToplamMesafe() {
        return toplamMesafe;
    }

    public void setToplamMesafe(Double toplamMesafe) {
        this.toplamMesafe = toplamMesafe;
    }

    public Integer getTahminiSureDakika() {
        return tahminiSureDakika;
    }

    public void setTahminiSureDakika(Integer tahminiSureDakika) {
        this.tahminiSureDakika = tahminiSureDakika;
    }

    public Integer getGerceklesenSureDakika() {
        return gerceklesenSureDakika;
    }

    public void setGerceklesenSureDakika(Integer gerceklesenSureDakika) {
        this.gerceklesenSureDakika = gerceklesenSureDakika;
    }

    public Double getOrtalamaHiz() {
        return ortalamaHiz;
    }

    public void setOrtalamaHiz(Double ortalamaHiz) {
        this.ortalamaHiz = ortalamaHiz;
    }

    public Double getMaksimumHiz() {
        return maksimumHiz;
    }

    public void setMaksimumHiz(Double maksimumHiz) {
        this.maksimumHiz = maksimumHiz;
    }

    public Integer getToplamIhlalSayisi() {
        return toplamIhlalSayisi;
    }

    public void setToplamIhlalSayisi(Integer toplamIhlalSayisi) {
        this.toplamIhlalSayisi = toplamIhlalSayisi;
    }

    public Boolean getAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(Boolean aktifMi) {
        this.aktifMi = aktifMi;
    }

    public Integer getOncelikSeviyesi() {
        return oncelikSeviyesi;
    }

    public void setOncelikSeviyesi(Integer oncelikSeviyesi) {
        this.oncelikSeviyesi = oncelikSeviyesi;
    }

    public String getGuvenlikSeviyesi() {
        return guvenlikSeviyesi;
    }

    public void setGuvenlikSeviyesi(String guvenlikSeviyesi) {
        this.guvenlikSeviyesi = guvenlikSeviyesi;
    }

    public String getRotaVerisi() {
        return rotaVerisi;
    }

    public void setRotaVerisi(String rotaVerisi) {
        this.rotaVerisi = rotaVerisi;
    }

    public List<GPSKonum> getGpsKonumlar() {
        return gpsKonumlar;
    }

    public void setGpsKonumlar(List<GPSKonum> gpsKonumlar) {
        this.gpsKonumlar = gpsKonumlar;
    }

    public List<Ihlal> getIhlaller() {
        return ihlaller;
    }

    public void setIhlaller(List<Ihlal> ihlaller) {
        this.ihlaller = ihlaller;
    }

    // Helper methods
    public boolean isBaslatilabilir() {
        return this.durum == GorevDurumu.PLANLANDI || this.durum == GorevDurumu.HAZIR &&
                this.mahkumlar != null && !this.mahkumlar.isEmpty() &&
                this.arac != null && this.arac.isAktif();
    }

    public boolean isTamamlanabilir() {
        return this.durum == GorevDurumu.DEVAM_EDIYOR || this.durum == GorevDurumu.BASLADI;
    }

    public boolean isIptalEdilebilir() {
        return this.durum != GorevDurumu.TAMAMLANDI &&
                this.durum != GorevDurumu.IPTAL_EDILDI;
    }

    public void tamamla() {
        this.durum = GorevDurumu.TAMAMLANDI;
        this.tamamlanmaTarihi = LocalDateTime.now();
        this.gerceklesenVaris = LocalDateTime.now();
        this.guncellemeTarihi = LocalDateTime.now();
    }

    public void baslat() {
        if (this.durum == GorevDurumu.PLANLANDI || this.durum == GorevDurumu.HAZIR) {
            this.durum = GorevDurumu.BASLADI;
            this.gerceklesenBaslangic = LocalDateTime.now();
            this.baslangicTarihi = LocalDateTime.now();
            this.guncellemeTarihi = LocalDateTime.now();
        }
    }

    public void devamEttir() {
        if (this.durum == GorevDurumu.BASLADI) {
            this.durum = GorevDurumu.DEVAM_EDIYOR;
            this.guncellemeTarihi = LocalDateTime.now();
        }
    }

    public void iptalEt() {
        if (this.isIptalEdilebilir()) {
            this.durum = GorevDurumu.IPTAL_EDILDI;
            this.iptalTarihi = LocalDateTime.now();
            this.guncellemeTarihi = LocalDateTime.now();
        }
    }

    public Long getTamamlanmaSuresiDakika() {
        if (this.tamamlanmaTarihi != null && this.gerceklesenBaslangic != null) {
            java.time.Duration duration = java.time.Duration.between(this.gerceklesenBaslangic, this.tamamlanmaTarihi);
            return duration.toMinutes();
        }
        return null;
    }

    public String getGorevOzetti() {
        return String.format("Görev #%d: %s → %s | %s",
                id, baslangicNoktasi, varisNoktasi, durum.getDisplayName());
    }

    public String getAracBilgisi() {
        return arac != null ? arac.getPlaka() + " - " + arac.getModel() : "Araç atanmamış";
    }

    public int getMahkumSayisi() {
        return mahkumlar != null ? mahkumlar.size() : 0;
    }

    public int getGorevliSayisi() {
        return gorevliler != null ? gorevliler.size() : 0;
    }

    // Yeni metod: Görev durumunu string olarak döndür
    public String getDurumMetni() {
        return durum.getDisplayName();
    }

    // Yeni metod: Görev süresi formatı
    public String getGorevSuresiFormati() {
        if (gerceklesenSureDakika != null) {
            int saat = gerceklesenSureDakika / 60;
            int dakika = gerceklesenSureDakika % 60;
            return String.format("%02d:%02d", saat, dakika);
        }
        return "00:00";
    }

    // Yeni metod: Görev ilerleme oranı (GPS konumlarına göre)
    public Double getIlerlemeOrani() {
        if (gpsKonumlar != null && !gpsKonumlar.isEmpty()) {
            GPSKonum sonKonum = gpsKonumlar.get(gpsKonumlar.size() - 1);
            if (sonKonum.getTamamlanmaOrani() != null) {
                return sonKonum.getTamamlanmaOrani();
            }
        }
        return 0.0;
    }

    @PrePersist
    protected void onCreate() {
        if (olusturulmaTarihi == null) {
            olusturulmaTarihi = LocalDateTime.now();
        }
        if (guncellemeTarihi == null) {
            guncellemeTarihi = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        guncellemeTarihi = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "NakilGorevi{" +
                "id=" + id +
                ", baslangicNoktasi='" + baslangicNoktasi + '\'' +
                ", varisNoktasi='" + varisNoktasi + '\'' +
                ", durum=" + durum +
                ", tamamlanmaTarihi=" + tamamlanmaTarihi +
                '}';
    }
}