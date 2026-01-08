package com.example.akilli.mahkum.nakil_sistemi.repository;

import com.example.akilli.mahkum.nakil_sistemi.model.GPSKonum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GPSKonumRepository extends JpaRepository<GPSKonum, Long> {

    // DÜZELTME: Metot adını doğru şekilde güncelle
    @Query("SELECT g FROM GPSKonum g WHERE g.nakilGorevi.id = :gorevId ORDER BY g.zamanDamgasi ASC")
    List<GPSKonum> findByNakilGoreviIdOrderByZamanDamgasiAsc(@Param("gorevId") Long gorevId);

    // Son konumu getir (ilkini al)
    @Query("SELECT g FROM GPSKonum g WHERE g.nakilGorevi.id = :gorevId ORDER BY g.zamanDamgasi DESC")
    List<GPSKonum> findTopByNakilGoreviIdOrderByZamanDamgasiDesc(@Param("gorevId") Long gorevId);

    // Bu metodu kaldır veya düzelt - "OrderByZaman" yanlış
    // List<GPSKonum> findByGorevIdOrderByZaman(Long gorevId); // BU SATIRI KALDIR

    // İhlal konumları
    @Query("SELECT g FROM GPSKonum g WHERE g.ihlalVar = true AND g.nakilGorevi.id = :gorevId")
    List<GPSKonum> findByIhlalVarTrueAndNakilGoreviId(@Param("gorevId") Long gorevId);

    // Diğer metodlar...
    List<GPSKonum> findByNakilGoreviIdAndZamanDamgasiBetweenOrderByZamanDamgasiAsc(
            Long gorevId, LocalDateTime baslangic, LocalDateTime bitis);

    Optional<GPSKonum> findFirstByNakilGoreviIdOrderByZamanDamgasiDesc(Long gorevId);

    List<GPSKonum> findByAracPlakaOrderByZamanDamgasiDesc(String plaka);
}