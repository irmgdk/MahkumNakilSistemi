package com.example.akilli.mahkum.nakil_sistemi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gorevliler")
public class Gorevli {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sicilNo; // Yeni eklendi

    @Column(nullable = false)
    private String ad;

    @Column(nullable = false)
    private String soyad;

    @Column(nullable = false, unique = true, length = 11)
    private String tcKimlik;

    @Column(nullable = false)
    private String gorev;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GorevliRol rol;

    @Column(nullable = false)
    private String telefon;

    @Column(nullable = false)
    private String email; // Eposta yerine email olarak değiştirildi

    @Column(nullable = false)
    private boolean aktif = true;

    @Column(nullable = false)
    private boolean müsait = true;

    @Column(nullable = false)
    private LocalDateTime kayitTarihi = LocalDateTime.now();

    private String adres;
    private String aciklama;

    public enum GorevliRol {
        SOFOR,
        GARDIYAN,
        SAĞLIK_PERSONELİ,
        YÖNETİCİ,
        DİĞER
    }

    // Constructors
    public Gorevli() {}

    public Gorevli(String sicilNo, String ad, String soyad, String tcKimlik, String gorev, GorevliRol rol, String email) {
        this.sicilNo = sicilNo;
        this.ad = ad;
        this.soyad = soyad;
        this.tcKimlik = tcKimlik;
        this.gorev = gorev;
        this.rol = rol;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSicilNo() {
        return sicilNo;
    }

    public void setSicilNo(String sicilNo) {
        this.sicilNo = sicilNo;
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

    public String getGorev() {
        return gorev;
    }

    public void setGorev(String gorev) {
        this.gorev = gorev;
    }

    public GorevliRol getRol() {
        return rol;
    }

    public void setRol(GorevliRol rol) {
        this.rol = rol;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public boolean isMüsait() {
        return müsait;
    }

    public void setMüsait(boolean müsait) {
        this.müsait = müsait;
    }

    public LocalDateTime getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDateTime kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    // Helper methods
    public String getAdSoyad() {
        return ad + " " + soyad;
    }

    public String getFullInfo() {
        return sicilNo + " - " + ad + " " + soyad + " (" + rol + ")";
    }

    public String getDurum() {
        if (!aktif) return "PASİF";
        if (!müsait) return "MEŞGUL";
        return "MÜSAİT";
    }

    @Override
    public String toString() {
        return "Gorevli{" +
                "id=" + id +
                ", sicilNo='" + sicilNo + '\'' +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", tcKimlik='" + tcKimlik + '\'' +
                ", gorev='" + gorev + '\'' +
                ", rol=" + rol +
                ", email='" + email + '\'' +
                ", aktif=" + aktif +
                ", müsait=" + müsait +
                '}';
    }
}