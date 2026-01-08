package com.example.akilli.mahkum.nakil_sistemi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ihlaller")
public class Ihlal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private IhlalTipi tip;

    private String aciklama;
    private Double enlem;
    private Double boylam;

    @ManyToOne
    @JoinColumn(name = "gorev_id")
    private NakilGorevi nakilGorevi;

    @ManyToOne
    @JoinColumn(name = "gorevli_id")
    private Gorevli bildirenGorevli;

    @ManyToOne
    @JoinColumn(name = "arac_id")
    private Arac arac; // Bu alanı ekleyin

    private LocalDateTime olusturulmaTarihi = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private IhlalDurumu durum = IhlalDurumu.INCELENIYOR;

    public enum IhlalTipi {
        ROTA_DISINA_CIKMA,
        HIZ_ASIMI,
        ACIL_DURUM_BUTONU,
        DURAKLAMA_IHLAVI,
        ARAC_ARIZASI,
        DİĞER
    }

    public enum IhlalDurumu {
        INCELENIYOR,
        ONAYLANDI,
        REDDEDILDI,
        COZULDU
    }

    // Constructor
    public Ihlal() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IhlalTipi getTip() {
        return tip;
    }

    public void setTip(IhlalTipi tip) {
        this.tip = tip;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
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

    public NakilGorevi getNakilGorevi() {
        return nakilGorevi;
    }

    public void setNakilGorevi(NakilGorevi nakilGorevi) {
        this.nakilGorevi = nakilGorevi;
    }

    public Gorevli getBildirenGorevli() {
        return bildirenGorevli;
    }

    public void setBildirenGorevli(Gorevli bildirenGorevli) {
        this.bildirenGorevli = bildirenGorevli;
    }

    // Arac getter ve setter'larını ekleyin
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

    public IhlalDurumu getDurum() {
        return durum;
    }

    public void setDurum(IhlalDurumu durum) {
        this.durum = durum;
    }
}