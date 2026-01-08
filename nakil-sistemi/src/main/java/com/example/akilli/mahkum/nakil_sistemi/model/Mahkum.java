package com.example.akilli.mahkum.nakil_sistemi.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mahkumlar")
public class Mahkum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ad;

    @Column(nullable = false)
    private String soyad;

    @Column(nullable = false, unique = true, length = 11)
    private String tcKimlik;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskSeviyesi riskSeviyesi;

    @Column(nullable = false)
    private String sucTipi;

    @Column(nullable = false)
    private boolean aktif = true;

    @Column(nullable = false)
    private LocalDateTime kayitTarihi = LocalDateTime.now();

    public enum RiskSeviyesi {
        DÜŞÜK,
        ORTA,
        YÜKSEK
    }

    // Constructors
    public Mahkum() {}

    public Mahkum(String ad, String soyad, String tcKimlik, RiskSeviyesi riskSeviyesi, String sucTipi) {
        this.ad = ad;
        this.soyad = soyad;
        this.tcKimlik = tcKimlik;
        this.riskSeviyesi = riskSeviyesi;
        this.sucTipi = sucTipi;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getTcKimlik() {
        return tcKimlik;
    }

    public void setTcKimlik(String tcKimlik) {
        this.tcKimlik = tcKimlik;
    }

    public RiskSeviyesi getRiskSeviyesi() {
        return riskSeviyesi;
    }

    public void setRiskSeviyesi(RiskSeviyesi riskSeviyesi) {
        this.riskSeviyesi = riskSeviyesi;
    }

    public String getSucTipi() {
        return sucTipi;
    }

    public void setSucTipi(String sucTipi) {
        this.sucTipi = sucTipi;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public LocalDateTime getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDateTime kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    // Helper method
    public String getAdSoyad() {
        return ad + " " + soyad;
    }

    @Override
    public String toString() {
        return "Mahkum{" +
                "id=" + id +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", tcKimlik='" + tcKimlik + '\'' +
                ", riskSeviyesi=" + riskSeviyesi +
                ", sucTipi='" + sucTipi + '\'' +
                ", aktif=" + aktif +
                ", kayitTarihi=" + kayitTarihi +
                '}';
    }

}