package com.example.akilli.mahkum.nakil_sistemi.repository;


import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MahkumRepository extends JpaRepository<Mahkum, Long> {

    // TC Kimlik ile sorgu
    boolean existsByTcKimlik(String tcKimlik);

    // Risk seviyesine göre filtreleme
    List<Mahkum> findByRiskSeviyesi(Mahkum.RiskSeviyesi riskSeviyesi);

    // Aktif mahkumları getir
    List<Mahkum> findByAktifTrue();

    // Ad veya soyad ile arama
    List<Mahkum> findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(String ad, String soyad);

    // TC Kimlik'e göre arama
    List<Mahkum> findByTcKimlikContaining(String tcKimlik);
}