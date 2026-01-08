package com.example.akilli.mahkum.nakil_sistemi.service;
import com.example.akilli.mahkum.nakil_sistemi.model.Gorevli;
import com.example.akilli.mahkum.nakil_sistemi.model.Ihlal;
import com.example.akilli.mahkum.nakil_sistemi.model.NakilGorevi;
import com.example.akilli.mahkum.nakil_sistemi.repository.IhlalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class IhlalTespitService {

    private final IhlalRepository ihlalRepository;

    public IhlalTespitService(IhlalRepository ihlalRepository) {
        this.ihlalRepository = ihlalRepository;
    }

    public Ihlal rotaDisinaCikmaTespit(NakilGorevi gorev, Double enlem, Double boylam, Gorevli bildirenGorevli) {
        Ihlal ihlal = new Ihlal();
        ihlal.setTip(Ihlal.IhlalTipi.ROTA_DISINA_CIKMA);
        ihlal.setAciklama("Araç planlanan rotanın dışına çıktı. Enlem: " + enlem + ", Boylam: " + boylam);
        ihlal.setEnlem(enlem);
        ihlal.setBoylam(boylam);
        ihlal.setNakilGorevi(gorev);
        ihlal.setBildirenGorevli(bildirenGorevli);

        return ihlalRepository.save(ihlal);
    }

    public Ihlal hizAsimiTespit(NakilGorevi gorev, Double hiz, Double maksimumHiz, Double enlem, Double boylam, LocalDateTime zamanDamgasi) {
        Ihlal ihlal = new Ihlal();
        ihlal.setTip(Ihlal.IhlalTipi.HIZ_ASIMI);
        ihlal.setAciklama("Hız sınırı aşıldı. Mevcut hız: " + hiz + " km/s, Maksimum hız: " + maksimumHiz + " km/s");
        ihlal.setEnlem(enlem);
        ihlal.setBoylam(boylam);
        ihlal.setNakilGorevi(gorev);

        return ihlalRepository.save(ihlal);
    }

    public Ihlal acilDurumButonuTespit(NakilGorevi gorev, Gorevli bildirenGorevli, Double enlem, Double boylam) {
        Ihlal ihlal = new Ihlal();
        ihlal.setTip(Ihlal.IhlalTipi.ACIL_DURUM_BUTONU);
        ihlal.setAciklama("Acil durum butonuna basıldı!");
        ihlal.setEnlem(enlem);
        ihlal.setBoylam(boylam);
        ihlal.setNakilGorevi(gorev);
        ihlal.setBildirenGorevli(bildirenGorevli);

        return ihlalRepository.save(ihlal);
    }
}