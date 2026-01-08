package com.example.akilli.mahkum.nakil_sistemi.repository;

import com.example.akilli.mahkum.nakil_sistemi.model.Gorevli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GorevliRepository extends JpaRepository<Gorevli, Long> {

    // TC Kimlik kontrolü
    boolean existsByTcKimlik(String tcKimlik);

    // TC Kimlik ile bul
    Optional<Gorevli> findByTcKimlik(String tcKimlik);

    // Role göre filtreleme
    List<Gorevli> findByRol(Gorevli.GorevliRol rol);

    // Aktif görevliler
    List<Gorevli> findByAktifTrue();

    // Müsait görevliler
    List<Gorevli> findByAktifTrueAndMüsaitTrue();

    // Sayım metodları
    long countByAktifTrue();
    long countByRol(Gorevli.GorevliRol rol);

    // Ad veya soyad ile arama
    List<Gorevli> findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(String ad, String soyad);

    // Göreve göre filtreleme
    List<Gorevli> findByGorev(String gorev);

    // Telefon numarası ile arama
    Optional<Gorevli> findByTelefon(String telefon);
}