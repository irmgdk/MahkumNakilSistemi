package com.example.akilli.mahkum.nakil_sistemi.repository;


import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MahkumRepositoryIntegrationTest {

    @Autowired
    private MahkumRepository mahkumRepository;

    @Test
    void findByTcKimlik_ShouldReturnMahkum() {
        // Arrange
        Mahkum mahkum = new Mahkum();
        mahkum.setAd("Test");
        mahkum.setSoyad("Mahkum");
        mahkum.setTcKimlik("11122233344");
        mahkum.setRiskSeviyesi(Mahkum.RiskSeviyesi.DÜŞÜK);
        mahkum.setSucTipi("Test Suç");
        mahkum.setAktif(true);
        mahkumRepository.save(mahkum);

        // Act
        boolean exists = mahkumRepository.existsByTcKimlik("11122233344");

        // Assert
        assertTrue(exists);
    }

    @Test
    void findByRiskSeviyesi_ShouldReturnFilteredMahkumlar() {
        // Arrange
        Mahkum mahkum1 = new Mahkum();
        mahkum1.setAd("Test1");
        mahkum1.setSoyad("Mahkum1");
        mahkum1.setTcKimlik("11111111111");
        mahkum1.setSucTipi("Hırsızlık");
        mahkum1.setRiskSeviyesi(Mahkum.RiskSeviyesi.DÜŞÜK);
        mahkum1.setAktif(true);

        Mahkum mahkum2 = new Mahkum();
        mahkum2.setAd("Test2");
        mahkum2.setSoyad("Mahkum2");
        mahkum2.setTcKimlik("22222222222");
        mahkum2.setRiskSeviyesi(Mahkum.RiskSeviyesi.YÜKSEK);
        mahkum2.setSucTipi("Cinayet");
        mahkum2.setAktif(true);

        mahkumRepository.save(mahkum1);
        mahkumRepository.save(mahkum2);

        // Act
        List<Mahkum> result = mahkumRepository.findByRiskSeviyesi(Mahkum.RiskSeviyesi.DÜŞÜK);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Mahkum.RiskSeviyesi.DÜŞÜK, result.get(0).getRiskSeviyesi());
    }



}