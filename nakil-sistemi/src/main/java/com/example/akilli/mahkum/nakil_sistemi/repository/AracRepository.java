package com.example.akilli.mahkum.nakil_sistemi.repository;

import com.example.akilli.mahkum.nakil_sistemi.model.Arac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AracRepository extends JpaRepository<Arac, Long> {

    // Plaka ile araç bul
    Optional<Arac> findByPlaka(String plaka);

    // Plaka var mı kontrol et
    boolean existsByPlaka(String plaka);

    // Aktif araçları getir
    List<Arac> findByAktifTrue();

    // Bakımdaki araçları getir
    List<Arac> findByBakimdaTrue();

    // Müsait araçları getir (aktif, bakımda değil, serviste değil)
    List<Arac> findByAktifTrueAndBakimdaFalseAndServisteFalse();

    // Kapasiteye göre araçları getir
    List<Arac> findByKapasiteGreaterThanEqual(int kapasite);

    // Tipe göre araçları getir
    List<Arac> findByTip(Arac.AracTipi tip);

    // Araç ara (plaka, model veya marka)
    @Query("SELECT a FROM Arac a WHERE LOWER(a.plaka) LIKE LOWER(CONCAT('%', :kelime, '%')) OR " +
            "LOWER(a.model) LIKE LOWER(CONCAT('%', :kelime, '%')) OR " +
            "LOWER(a.marka) LIKE LOWER(CONCAT('%', :kelime, '%')) ORDER BY a.plaka")
    List<Arac> findByPlakaContainingOrModelContainingOrMarkaContainingAllIgnoreCase(
            @Param("kelime") String kelime1,
            @Param("kelime") String kelime2,
            @Param("kelime") String kelime3);

    // İstatistikler için count metodları
    long countByAktifTrue();
    long countByAktifTrueAndBakimdaFalseAndServisteFalse();
    long countByServisteTrue();
    long countByTip(Arac.AracTipi tip);

    // Son iletişim tarihine göre count
    long countBySonIletisimAfter(LocalDateTime tarih);

    // Plaka içeren araçları getir
    List<Arac> findByPlakaContaining(String plaka);

    // Model içeren araçları getir
    List<Arac> findByModelContainingIgnoreCase(String model);

    // Tüm araçları plakaya göre sıralı getir
    List<Arac> findAllByOrderByPlakaAsc();

    // Aktif araçları plakaya göre sıralı getir
    List<Arac> findByAktifTrueOrderByPlakaAsc();

    // Bakımdaki araçları plakaya göre sıralı getir
    List<Arac> findByBakimdaTrueOrderByPlakaAsc();

    // Tipe göre sıralı getir
    List<Arac> findByTipOrderByPlakaAsc(Arac.AracTipi tip);

    List<Arac> findByAktifTrueAndBakimdaFalseAndServisteFalseOrderByPlakaAsc();
}