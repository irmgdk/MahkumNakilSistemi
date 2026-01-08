package com.example.akilli.mahkum.nakil_sistemi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "araclar")
public class Arac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plaka", nullable = false, unique = true, length = 20)
    private String plaka;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "marka", length = 100)
    private String marka;

    @Column(name = "yil")
    private Integer yil;

    @Column(name = "renk", length = 50)
    private String renk;

    @Column(name = "kapasite", nullable = false)
    private int kapasite;

    @Column(name = "mevcut_yolcu_sayisi")
    private Integer mevcutYolcuSayisi = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip", nullable = false)
    private AracTipi tip;

    @Column(name = "aktif", nullable = false)
    private boolean aktif = true;

    @Column(name = "bakimda")
    private boolean bakimda = false;

    @Column(name = "serviste")
    private boolean serviste = false;

    @Column(name = "kayit_tarihi", nullable = false)
    private LocalDateTime kayitTarihi = LocalDateTime.now();

    @Column(name = "guncelleme_tarihi")
    private LocalDateTime guncellemeTarihi = LocalDateTime.now();

    @Column(name = "son_bakim_tarihi")
    private LocalDateTime sonBakimTarihi;

    @Column(name = "sonraki_bakim_tarihi")
    private LocalDateTime sonrakiBakimTarihi;

    @Column(name = "km")
    private Long km = 0L;

    @Column(name = "yakit_turu", length = 50)
    private String yakitTuru;

    @Column(name = "yakit_seviyesi")
    private Double yakitSeviyesi;

    @Column(name = "ortalama_yakit_tuketimi")
    private Double ortalamaYakitTuketimi;

    @Column(name = "motor_no", length = 100)
    private String motorNo;

    @Column(name = "sasi_no", length = 100)
    private String sasiNo;

    @Column(name = "sigorta_bitis_tarihi")
    private LocalDateTime sigortaBitisTarihi;

    @Column(name = "muayene_bitis_tarihi")
    private LocalDateTime muayeneBitisTarihi;

    @Column(name = "aciklama", length = 2000)
    private String aciklama;

    @Column(name = "gps_cihaz_no", length = 100)
    private String gpsCihazNo;

    @Column(name = "kamera_sistemi")
    private Boolean kameraSistemi = false;

    @Column(name = "guvenlik_sistemi")
    private Boolean guvenlikSistemi = false;

    @Column(name = "iletisim_sistemi")
    private Boolean iletisimSistemi = false;

    @Column(name = "son_konum_enlem")
    private Double sonKonumEnlem;

    @Column(name = "son_konum_boylam")
    private Double sonKonumBoylam;

    @Column(name = "son_konum_zamani")
    private LocalDateTime sonKonumZamani;

    @Column(name = "son_hiz")
    private Double sonHiz;

    @Column(name = "son_iletisim")
    private LocalDateTime sonIletisim;

    @OneToMany(mappedBy = "arac", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GPSKonum> gpsKonumlar = new ArrayList<>();

    @OneToMany(mappedBy = "arac", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NakilGorevi> gorevler = new ArrayList<>();

    @OneToMany(mappedBy = "arac", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ihlal> ihlaller = new ArrayList<>();



    public enum AracTipi {
        NAKİL("Nakil Aracı"),
        ACİL("Acil Aracı"),
        YÖNETİCİ("Yönetici Aracı"),
        DESTEK("Destek Aracı"),
        KORUMA("Koruma Aracı"),
        İLETİŞİM("İletişim Aracı"),
        DİĞER("Diğer");

        private final String displayName;

        AracTipi(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public Arac() {}

    public Arac(String plaka, String model, int kapasite, AracTipi tip) {
        this.plaka = plaka;
        this.model = model;
        this.kapasite = kapasite;
        this.tip = tip;
        this.kayitTarihi = LocalDateTime.now();
        this.guncellemeTarihi = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public Integer getYil() {
        return yil;
    }

    public void setYil(Integer yil) {
        this.yil = yil;
    }

    public String getRenk() {
        return renk;
    }

    public void setRenk(String renk) {
        this.renk = renk;
    }

    public int getKapasite() {
        return kapasite;
    }

    public void setKapasite(int kapasite) {
        this.kapasite = kapasite;
    }

    public Integer getMevcutYolcuSayisi() {
        return mevcutYolcuSayisi;
    }

    public void setMevcutYolcuSayisi(Integer mevcutYolcuSayisi) {
        this.mevcutYolcuSayisi = mevcutYolcuSayisi;
    }

    public AracTipi getTip() {
        return tip;
    }

    public void setTip(AracTipi tip) {
        this.tip = tip;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
        this.guncellemeTarihi = LocalDateTime.now();
    }

    public boolean isBakimda() {
        return bakimda;
    }

    public void setBakimda(boolean bakimda) {
        this.bakimda = bakimda;
        this.guncellemeTarihi = LocalDateTime.now();
    }

    public boolean isServiste() {
        return serviste;
    }

    public void setServiste(boolean serviste) {
        this.serviste = serviste;
        this.guncellemeTarihi = LocalDateTime.now();
    }

    public LocalDateTime getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDateTime kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    public LocalDateTime getGuncellemeTarihi() {
        return guncellemeTarihi;
    }

    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) {
        this.guncellemeTarihi = guncellemeTarihi;
    }

    public LocalDateTime getSonBakimTarihi() {
        return sonBakimTarihi;
    }

    public void setSonBakimTarihi(LocalDateTime sonBakimTarihi) {
        this.sonBakimTarihi = sonBakimTarihi;
    }

    public LocalDateTime getSonrakiBakimTarihi() {
        return sonrakiBakimTarihi;
    }

    public void setSonrakiBakimTarihi(LocalDateTime sonrakiBakimTarihi) {
        this.sonrakiBakimTarihi = sonrakiBakimTarihi;
    }

    public Long getKm() {
        return km;
    }

    public void setKm(Long km) {
        this.km = km;
    }

    public String getYakitTuru() {
        return yakitTuru;
    }

    public void setYakitTuru(String yakitTuru) {
        this.yakitTuru = yakitTuru;
    }

    public Double getYakitSeviyesi() {
        return yakitSeviyesi;
    }

    public void setYakitSeviyesi(Double yakitSeviyesi) {
        this.yakitSeviyesi = yakitSeviyesi;
    }

    public Double getOrtalamaYakitTuketimi() {
        return ortalamaYakitTuketimi;
    }

    public void setOrtalamaYakitTuketimi(Double ortalamaYakitTuketimi) {
        this.ortalamaYakitTuketimi = ortalamaYakitTuketimi;
    }

    public String getMotorNo() {
        return motorNo;
    }

    public void setMotorNo(String motorNo) {
        this.motorNo = motorNo;
    }

    public String getSasiNo() {
        return sasiNo;
    }

    public void setSasiNo(String sasiNo) {
        this.sasiNo = sasiNo;
    }

    public LocalDateTime getSigortaBitisTarihi() {
        return sigortaBitisTarihi;
    }

    public void setSigortaBitisTarihi(LocalDateTime sigortaBitisTarihi) {
        this.sigortaBitisTarihi = sigortaBitisTarihi;
    }

    public LocalDateTime getMuayeneBitisTarihi() {
        return muayeneBitisTarihi;
    }

    public void setMuayeneBitisTarihi(LocalDateTime muayeneBitisTarihi) {
        this.muayeneBitisTarihi = muayeneBitisTarihi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getGpsCihazNo() {
        return gpsCihazNo;
    }

    public void setGpsCihazNo(String gpsCihazNo) {
        this.gpsCihazNo = gpsCihazNo;
    }

    public Boolean getKameraSistemi() {
        return kameraSistemi;
    }

    public void setKameraSistemi(Boolean kameraSistemi) {
        this.kameraSistemi = kameraSistemi;
    }

    public Boolean getGuvenlikSistemi() {
        return guvenlikSistemi;
    }

    public void setGuvenlikSistemi(Boolean guvenlikSistemi) {
        this.guvenlikSistemi = guvenlikSistemi;
    }

    public Boolean getIletisimSistemi() {
        return iletisimSistemi;
    }

    public void setIletisimSistemi(Boolean iletisimSistemi) {
        this.iletisimSistemi = iletisimSistemi;
    }

    public Double getSonKonumEnlem() {
        return sonKonumEnlem;
    }

    public void setSonKonumEnlem(Double sonKonumEnlem) {
        this.sonKonumEnlem = sonKonumEnlem;
    }

    public Double getSonKonumBoylam() {
        return sonKonumBoylam;
    }

    public void setSonKonumBoylam(Double sonKonumBoylam) {
        this.sonKonumBoylam = sonKonumBoylam;
    }

    public LocalDateTime getSonKonumZamani() {
        return sonKonumZamani;
    }

    public void setSonKonumZamani(LocalDateTime sonKonumZamani) {
        this.sonKonumZamani = sonKonumZamani;
    }

    public Double getSonHiz() {
        return sonHiz;
    }

    public void setSonHiz(Double sonHiz) {
        this.sonHiz = sonHiz;
    }

    public LocalDateTime getSonIletisim() {
        return sonIletisim;
    }

    public void setSonIletisim(LocalDateTime sonIletisim) {
        this.sonIletisim = sonIletisim;
    }

    public List<GPSKonum> getGpsKonumlar() {
        return gpsKonumlar;
    }

    public void setGpsKonumlar(List<GPSKonum> gpsKonumlar) {
        this.gpsKonumlar = gpsKonumlar;
    }

    public List<NakilGorevi> getGorevler() {
        return gorevler;
    }

    public void setGorevler(List<NakilGorevi> gorevler) {
        this.gorevler = gorevler;
    }

    public List<Ihlal> getIhlaller() {
        return ihlaller;
    }

    public void setIhlaller(List<Ihlal> ihlaller) {
        this.ihlaller = ihlaller;
    }

    // Helper methods
    public boolean isMüsait() {
        return aktif && !bakimda && !serviste;
    }

    public String getDurum() {
        if (!aktif) return "PASİF";
        if (bakimda) return "BAKIMDA";
        if (serviste) return "SERVİSTE";
        return "MÜSAİT";
    }

    public String getDurumRenk() {
        if (!aktif) return "danger";
        if (bakimda || serviste) return "warning";
        return "success";
    }

    public String getTamModel() {
        return (marka != null ? marka + " " : "") + model + (yil != null ? " (" + yil + ")" : "");
    }

    public Double getBosKoltukSayisi() {
        return (double) (kapasite - (mevcutYolcuSayisi != null ? mevcutYolcuSayisi : 0));
    }

    public boolean isYakitDusuk() {
        return yakitSeviyesi != null && yakitSeviyesi < 20.0;
    }

    public boolean isBakimGerekli() {
        if (sonrakiBakimTarihi == null) return false;
        return LocalDateTime.now().isAfter(sonrakiBakimTarihi);
    }

    public boolean isSigortaGecerli() {
        if (sigortaBitisTarihi == null) return true;
        return LocalDateTime.now().isBefore(sigortaBitisTarihi);
    }

    public boolean isMuayeneGecerli() {
        if (muayeneBitisTarihi == null) return true;
        return LocalDateTime.now().isBefore(muayeneBitisTarihi);
    }

    public String getSonKonumKoordinatlari() {
        if (sonKonumEnlem != null && sonKonumBoylam != null) {
            return String.format("%.6f, %.6f", sonKonumEnlem, sonKonumBoylam);
        }
        return "Konum yok";
    }

    public String getSonKonumZamaniFormati() {
        if (sonKonumZamani != null) {
            return sonKonumZamani.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        }
        return "Bilinmiyor";
    }

    @PrePersist
    protected void onCreate() {
        if (kayitTarihi == null) {
            kayitTarihi = LocalDateTime.now();
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
        return "Arac{" +
                "id=" + id +
                ", plaka='" + plaka + '\'' +
                ", model='" + model + '\'' +
                ", kapasite=" + kapasite +
                ", tip=" + tip +
                ", aktif=" + aktif +
                ", bakimda=" + bakimda +
                '}';
    }
}