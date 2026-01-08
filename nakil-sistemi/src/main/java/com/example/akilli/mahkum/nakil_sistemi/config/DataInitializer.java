package com.example.akilli.mahkum.nakil_sistemi.config;

import com.example.akilli.mahkum.nakil_sistemi.model.Arac;
import com.example.akilli.mahkum.nakil_sistemi.model.Gorevli;
import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import com.example.akilli.mahkum.nakil_sistemi.model.NakilGorevi;
import com.example.akilli.mahkum.nakil_sistemi.repository.AracRepository;
import com.example.akilli.mahkum.nakil_sistemi.repository.GorevliRepository;
import com.example.akilli.mahkum.nakil_sistemi.repository.MahkumRepository;
import com.example.akilli.mahkum.nakil_sistemi.repository.NakilGoreviRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MahkumRepository mahkumRepository;
    private final AracRepository aracRepository;
    private final GorevliRepository gorevliRepository;
    private final NakilGoreviRepository nakilGoreviRepository;

    public DataInitializer(MahkumRepository mahkumRepository,
                           AracRepository aracRepository,
                           GorevliRepository gorevliRepository,
                           NakilGoreviRepository nakilGoreviRepository) {
        this.mahkumRepository = mahkumRepository;
        this.aracRepository = aracRepository;
        this.gorevliRepository = gorevliRepository;
        this.nakilGoreviRepository = nakilGoreviRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Eğer veritabanında hiç veri yoksa test verileri oluştur
        if (mahkumRepository.count() == 0 &&
                aracRepository.count() == 0 &&
                gorevliRepository.count() == 0) {
            createTestData();
        }
    }

    private void createTestData() {
        System.out.println("Test verileri oluşturuluyor...");

        // Araçlar
        Arac arac1 = new Arac();
        arac1.setPlaka("34ABC123");
        arac1.setModel("Mercedes Sprinter");
        arac1.setKapasite(10);
        arac1.setTip(Arac.AracTipi.NAKİL);
        arac1.setAktif(true);
        aracRepository.save(arac1);

        Arac arac2 = new Arac();
        arac2.setPlaka("34XYZ789");
        arac2.setModel("Ford Transit");
        arac2.setKapasite(8);
        arac2.setTip(Arac.AracTipi.ACİL);
        arac2.setAktif(true);
        aracRepository.save(arac2);

        // Görevliler
        Gorevli sofor = new Gorevli();
        sofor.setSicilNo("SOF001");
        sofor.setAd("Mehmet");
        sofor.setSoyad("Yılmaz");
        sofor.setTcKimlik("11111111111");
        sofor.setGorev("Şoför");
        sofor.setTelefon("5551234567");
        sofor.setEmail("mehmet@nakil.gov.tr");
        sofor.setRol(Gorevli.GorevliRol.SOFOR); // SOFOR olarak düzeltildi
        sofor.setAktif(true);
        sofor.setMüsait(true);
        gorevliRepository.save(sofor);

        Gorevli gardiyan = new Gorevli();
        gardiyan.setSicilNo("GRD001");
        gardiyan.setAd("Ali");
        gardiyan.setSoyad("Demir");
        gardiyan.setTcKimlik("22222222222");
        gardiyan.setGorev("Gardiyan");
        gardiyan.setTelefon("5559876543");
        gardiyan.setEmail("ali@nakil.gov.tr");
        gardiyan.setRol(Gorevli.GorevliRol.GARDIYAN); // GARDIYAN olarak düzeltildi
        gardiyan.setAktif(true);
        gardiyan.setMüsait(true);
        gorevliRepository.save(gardiyan);

        Gorevli yonetici = new Gorevli();
        yonetici.setSicilNo("YON001");
        yonetici.setAd("Ayşe");
        yonetici.setSoyad("Kaya");
        yonetici.setTcKimlik("33333333333");
        yonetici.setGorev("Yönetici");
        yonetici.setTelefon("5554443322");
        yonetici.setEmail("ayse@nakil.gov.tr");
        yonetici.setRol(Gorevli.GorevliRol.YÖNETİCİ);
        yonetici.setAktif(true);
        yonetici.setMüsait(true);
        gorevliRepository.save(yonetici);

        // Mahkumlar
        Mahkum mahkum1 = new Mahkum();
        mahkum1.setAd("Ahmet");
        mahkum1.setSoyad("Kara");
        mahkum1.setTcKimlik("12345678901");
        mahkum1.setRiskSeviyesi(Mahkum.RiskSeviyesi.ORTA);
        mahkum1.setSucTipi("Hırsızlık");
        mahkum1.setAktif(true);
        mahkumRepository.save(mahkum1);

        Mahkum mahkum2 = new Mahkum();
        mahkum2.setAd("Veli");
        mahkum2.setSoyad("Beyaz");
        mahkum2.setTcKimlik("98765432109");
        mahkum2.setRiskSeviyesi(Mahkum.RiskSeviyesi.YÜKSEK);
        mahkum2.setSucTipi("Dolandırıcılık");
        mahkum2.setAktif(true);
        mahkumRepository.save(mahkum2);

        // Nakil Görevi
        NakilGorevi gorev = new NakilGorevi();
        gorev.setBaslangicNoktasi("Silivri Cezaevi");
        gorev.setVarisNoktasi("Kartal Cezaevi");
        gorev.setPlanlananBaslangic(LocalDateTime.now().plusHours(1));
        gorev.setPlanlananVaris(LocalDateTime.now().plusHours(3));
        gorev.setArac(arac1);
        gorev.setMahkumlar(Arrays.asList(mahkum1, mahkum2));
        gorev.setGorevliler(Arrays.asList(sofor, gardiyan));
        gorev.setDurum(NakilGorevi.GorevDurumu.PLANLANDI);
        nakilGoreviRepository.save(gorev);

        System.out.println("Test verileri başarıyla oluşturuldu!");
    }
}