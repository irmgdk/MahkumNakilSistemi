package com.example.akilli.mahkum.nakil_sistemi.model;
import com.example.akilli.mahkum.nakil_sistemi.model.Arac;
import com.example.akilli.mahkum.nakil_sistemi.model.NakilGorevi;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gps_konumlar")
public class GPSKonum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enlem", nullable = false)
    private Double enlem;

    @Column(name = "boylam", nullable = false)
    private Double boylam;

    @Column(name = "hiz")
    private Double hiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nakil_gorevi_id", nullable = false)
    private NakilGorevi nakilGorevi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arac_id", nullable = false)
    private Arac arac;

    @Column(name = "ihlal_var")
    private Boolean ihlalVar = false;

    @Column(name = "ihlal_aciklama", length = 500)
    private String ihlalAciklama;

    @Column(name = "zaman_damgasi", nullable = false)
    private LocalDateTime zamanDamgasi = LocalDateTime.now();

    @Column(name = "gps_accuracy")
    private Double gpsAccuracy;

    @Column(name = "yolcu_sayisi")
    private Integer yolcuSayisi;

    @Column(name = "motor_sicakligi")
    private Double motorSicakligi;

    @Column(name = "yakıt_seviyesi")
    private Double yakitSeviyesi;

    @Column(name = "mesafe_katedildi")
    private Double mesafeKatedildi;

    @Column(name = "toplam_mesafe")
    private Double toplamMesafe;

    @Column(name = "tamamlanma_orani")
    private Double tamamlanmaOrani;

    @Column(name = "tahmini_varis_suresi")
    private String tahminiVarisSuresi;

    @Column(name = "konum_kalitesi")
    private String konumKalitesi;

    @Column(name = "veri_kaynağı")
    private String veriKaynagi;

    @Column(name = "son_haberlesme")
    private LocalDateTime sonHaberlesme;

    @Column(name = "aktif_mi")
    private Boolean aktifMi = true;

    @Column(name = "aciklama", length = 1000)
    private String aciklama;

    @Column(name = "olusturma_tarihi")
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @Column(name = "guncelleme_tarihi")
    private LocalDateTime guncellemeTarihi = LocalDateTime.now();

    // Constructor
    public GPSKonum() {
    }

    // Parametreli Constructor
    public GPSKonum(Double enlem, Double boylam, Double hiz, NakilGorevi nakilGorevi, Arac arac) {
        this.enlem = enlem;
        this.boylam = boylam;
        this.hiz = hiz;
        this.nakilGorevi = nakilGorevi;
        this.arac = arac;
        this.zamanDamgasi = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEnlem() {
        return enlem;
    }

    public void setEnlem(Double enlem) {
        this.enlem = enlem;
    }

    public Double getBoylam() {
        return boylam;
    }

    public void setBoylam(Double boylam) {
        this.boylam = boylam;
    }

    public Double getHiz() {
        return hiz;
    }

    public void setHiz(Double hiz) {
        this.hiz = hiz;
    }

    public NakilGorevi getNakilGorevi() {
        return nakilGorevi;
    }

    public void setNakilGorevi(NakilGorevi nakilGorevi) {
        this.nakilGorevi = nakilGorevi;
    }

    public Arac getArac() {
        return arac;
    }

    public void setArac(Arac arac) {
        this.arac = arac;
    }

    public Boolean getIhlalVar() {
        return ihlalVar;
    }

    public void setIhlalVar(Boolean ihlalVar) {
        this.ihlalVar = ihlalVar;
    }

    public String getIhlalAciklama() {
        return ihlalAciklama;
    }

    public void setIhlalAciklama(String ihlalAciklama) {
        this.ihlalAciklama = ihlalAciklama;
    }

    public LocalDateTime getZamanDamgasi() {
        return zamanDamgasi;
    }

    public void setZamanDamgasi(LocalDateTime zamanDamgasi) {
        this.zamanDamgasi = zamanDamgasi;
    }

    public Double getGpsAccuracy() {
        return gpsAccuracy;
    }

    public void setGpsAccuracy(Double gpsAccuracy) {
        this.gpsAccuracy = gpsAccuracy;
    }

    public Integer getYolcuSayisi() {
        return yolcuSayisi;
    }

    public void setYolcuSayisi(Integer yolcuSayisi) {
        this.yolcuSayisi = yolcuSayisi;
    }

    public Double getMotorSicakligi() {
        return motorSicakligi;
    }

    public void setMotorSicakligi(Double motorSicakligi) {
        this.motorSicakligi = motorSicakligi;
    }

    public Double getYakitSeviyesi() {
        return yakitSeviyesi;
    }

    public void setYakitSeviyesi(Double yakitSeviyesi) {
        this.yakitSeviyesi = yakitSeviyesi;
    }

    public Double getMesafeKatedildi() {
        return mesafeKatedildi;
    }

    public void setMesafeKatedildi(Double mesafeKatedildi) {
        this.mesafeKatedildi = mesafeKatedildi;
    }

    public Double getToplamMesafe() {
        return toplamMesafe;
    }

    public void setToplamMesafe(Double toplamMesafe) {
        this.toplamMesafe = toplamMesafe;
    }

    public Double getTamamlanmaOrani() {
        return tamamlanmaOrani;
    }

    public void setTamamlanmaOrani(Double tamamlanmaOrani) {
        this.tamamlanmaOrani = tamamlanmaOrani;
    }

    public String getTahminiVarisSuresi() {
        return tahminiVarisSuresi;
    }

    public void setTahminiVarisSuresi(String tahminiVarisSuresi) {
        this.tahminiVarisSuresi = tahminiVarisSuresi;
    }

    public String getKonumKalitesi() {
        return konumKalitesi;
    }

    public void setKonumKalitesi(String konumKalitesi) {
        this.konumKalitesi = konumKalitesi;
    }

    public String getVeriKaynagi() {
        return veriKaynagi;
    }

    public void setVeriKaynagi(String veriKaynagi) {
        this.veriKaynagi = veriKaynagi;
    }

    public LocalDateTime getSonHaberlesme() {
        return sonHaberlesme;
    }

    public void setSonHaberlesme(LocalDateTime sonHaberlesme) {
        this.sonHaberlesme = sonHaberlesme;
    }

    public Boolean getAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(Boolean aktifMi) {
        this.aktifMi = aktifMi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public LocalDateTime getGuncellemeTarihi() {
        return guncellemeTarihi;
    }

    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) {
        this.guncellemeTarihi = guncellemeTarihi;
    }

    // İhlal tespiti için yardımcı metod
    public boolean isHizIhlali(double maksimumHiz) {
        return this.hiz != null && this.hiz > maksimumHiz;
    }

    // Koordinatları string olarak döndüren metod
    public String getKoordinatlar() {
        return String.format("%.6f, %.6f", enlem, boylam);
    }

    // Hız formatı
    public String getHizFormati() {
        if (hiz == null) return "0 km/s";
        return String.format("%.1f km/s", hiz);
    }

    // GPS doğruluğu formatı
    public String getGpsAccuracyFormati() {
        if (gpsAccuracy == null) return "Bilinmiyor";
        return String.format("%.0f m", gpsAccuracy);
    }

    // Tamamlanma yüzdesi formatı
    public String getTamamlanmaOraniFormati() {
        if (tamamlanmaOrani == null) return "0%";
        return String.format("%.1f%%", tamamlanmaOrani);
    }

    // Zaman formatı
    public String getZamanFormati() {
        if (zamanDamgasi == null) return "";
        return zamanDamgasi.toString();
    }

    // Kalan mesafe hesaplama
    public Double getKalanMesafe() {
        if (toplamMesafe == null || mesafeKatedildi == null) {
            return null;
        }
        return toplamMesafe - mesafeKatedildi;
    }

    // Yakıt seviyesi kontrolü
    public boolean isYakitDusuk() {
        return yakitSeviyesi != null && yakitSeviyesi < 20.0;
    }

    // Motor sıcaklığı kontrolü
    public boolean isMotorAsiriIsinma() {
        return motorSicakligi != null && motorSicakligi > 100.0;
    }

    // GPS kalite seviyesi
    public String getGpsKaliteSeviyesi() {
        if (gpsAccuracy == null) return "Bilinmiyor";
        if (gpsAccuracy <= 10) return "Yüksek";
        if (gpsAccuracy <= 50) return "Orta";
        return "Düşük";
    }

    @PrePersist
    protected void onCreate() {
        if (olusturmaTarihi == null) {
            olusturmaTarihi = LocalDateTime.now();
        }
        if (guncellemeTarihi == null) {
            guncellemeTarihi = LocalDateTime.now();
        }
        if (zamanDamgasi == null) {
            zamanDamgasi = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        guncellemeTarihi = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "GPSKonum{" +
                "id=" + id +
                ", enlem=" + enlem +
                ", boylam=" + boylam +
                ", hiz=" + hiz +
                ", nakilGorevi=" + (nakilGorevi != null ? nakilGorevi.getId() : null) +
                ", arac=" + (arac != null ? arac.getPlaka() : null) +
                ", ihlalVar=" + ihlalVar +
                ", zamanDamgasi=" + zamanDamgasi +
                '}';
    }
}