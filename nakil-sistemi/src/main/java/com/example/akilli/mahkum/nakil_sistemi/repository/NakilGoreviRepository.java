package com.example.akilli.mahkum.nakil_sistemi.repository;

import com.example.akilli.mahkum.nakil_sistemi.model.NakilGorevi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NakilGoreviRepository extends JpaRepository<NakilGorevi, Long> {

    // Duruma göre görevler
    List<NakilGorevi> findByDurum(NakilGorevi.GorevDurumu durum);

    // Durum hariç görevler
    List<NakilGorevi> findByDurumNot(NakilGorevi.GorevDurumu durum);

    // Durumun haricinde olan görevler (DEVAM_EDIYOR hariç TAMAMLANDI ve PLANLANDI)
    @Query("SELECT g FROM NakilGorevi g WHERE g.durum <> :durum")
    List<NakilGorevi> findByDurumNotEqual(@Param("durum") NakilGorevi.GorevDurumu durum);

    // Tarihe göre sıralı
    List<NakilGorevi> findByOrderByPlanlananBaslangicAsc();

    // Belirli tarih aralığındaki görevler
    List<NakilGorevi> findByPlanlananBaslangicBetween(LocalDateTime baslangic, LocalDateTime son);

    // Aktif görevler (planlanan veya devam eden)
    @Query("SELECT g FROM NakilGorevi g WHERE g.durum IN ('PLANLANDI', 'DEVAM_EDIYOR') ORDER BY g.planlananBaslangic ASC")
    List<NakilGorevi> findAktifGorevler();

    // Sadece DEVAM_EDIYOR durumundaki görevler
    @Query("SELECT g FROM NakilGorevi g WHERE g.durum = 'DEVAM_EDIYOR' ORDER BY g.planlananBaslangic ASC")
    List<NakilGorevi> findDevamEdenGorevler();

    // Araç plakasına göre görevler
    @Query("SELECT g FROM NakilGorevi g WHERE g.arac.plaka = :plaka")
    List<NakilGorevi> findByAracPlaka(@Param("plaka") String plaka);

    // Mahkum ID'sine göre görevler
    @Query("SELECT g FROM NakilGorevi g JOIN g.mahkumlar m WHERE m.id = :mahkumId")
    List<NakilGorevi> findByMahkumId(@Param("mahkumId") Long mahkumId);

    // Görevli ID'sine göre görevler
    @Query("SELECT g FROM NakilGorevi g JOIN g.gorevliler gl WHERE gl.id = :gorevliId")
    List<NakilGorevi> findByGorevliId(@Param("gorevliId") Long gorevliId);

    // Bugünün görevleri
    @Query("SELECT g FROM NakilGorevi g WHERE g.planlananBaslangic >= :startOfDay AND g.planlananBaslangic < :endOfDay ORDER BY g.planlananBaslangic ASC")
    List<NakilGorevi> findBugunkuGorevler(@Param("startOfDay") LocalDateTime startOfDay,
                                          @Param("endOfDay") LocalDateTime endOfDay);

    // Gelecek görevler
    @Query("SELECT g FROM NakilGorevi g WHERE g.planlananBaslangic > CURRENT_TIMESTAMP AND g.durum = 'PLANLANDI' ORDER BY g.planlananBaslangic ASC")
    List<NakilGorevi> findGelecekGorevler();

    // Tamamlanan görevler (son 30 gün)
    @Query("SELECT g FROM NakilGorevi g WHERE g.durum = 'TAMAMLANDI' AND g.planlananVaris > :tarih ORDER BY g.planlananVaris DESC")
    List<NakilGorevi> findSonTamamlananGorevler(@Param("tarih") LocalDateTime tarih);

    // Plakaya göre ve duruma göre görevler
    @Query("SELECT g FROM NakilGorevi g WHERE g.arac.plaka = :plaka AND g.durum = :durum")
    List<NakilGorevi> findByAracPlakaAndDurum(@Param("plaka") String plaka,
                                              @Param("durum") NakilGorevi.GorevDurumu durum);

    // Başlangıç noktasına göre görevler
    @Query("SELECT g FROM NakilGorevi g WHERE g.baslangicNoktasi = :baslangicNoktasi")
    List<NakilGorevi> findByBaslangicNoktasi(@Param("baslangicNoktasi") String baslangicNoktasi);

    // Varış noktasına göre görevler
    @Query("SELECT g FROM NakilGorevi g WHERE g.varisNoktasi = :varisNoktasi")
    List<NakilGorevi> findByVarisNoktasi(@Param("varisNoktasi") String varisNoktasi);

    // Aktif görevleri tarihe göre sıralı getir
    @Query("SELECT g FROM NakilGorevi g WHERE g.durum IN ('PLANLANDI', 'DEVAM_EDIYOR') AND g.planlananBaslangic <= :tarih ORDER BY g.planlananBaslangic ASC")
    List<NakilGorevi> findAktifVeBaslayanGorevler(@Param("tarih") LocalDateTime tarih);

    // İptal edilebilir görevler (PLANLANDI durumundaki)
    @Query("SELECT g FROM NakilGorevi g WHERE g.durum = 'PLANLANDI' AND g.planlananBaslangic > CURRENT_TIMESTAMP")
    List<NakilGorevi> findIptalEdilebilirGorevler();

    // GPS takibi için aktif görevler (sadece DEVAM_EDIYOR)
    @Query("SELECT g FROM NakilGorevi g WHERE g.durum = 'DEVAM_EDIYOR'")
    List<NakilGorevi> findGpsTakipIcinAktifGorevler();
}