package com.example.akilli.mahkum.nakil_sistemi.service;

import com.example.akilli.mahkum.nakil_sistemi.model.*;
import com.example.akilli.mahkum.nakil_sistemi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NakilGoreviServiceTest {

    @Mock
    private NakilGoreviRepository nakilGoreviRepository;

    @Mock
    private MahkumRepository mahkumRepository;

    @Mock
    private AracRepository aracRepository;

    @Mock
    private GorevliRepository gorevliRepository;

    @InjectMocks
    private NakilGoreviService nakilGoreviService;

    private NakilGorevi gorev;
    private Arac arac;
    private Mahkum mahkum1;
    private Mahkum mahkum2;
    private Gorevli sofor;
    private Gorevli gardiyan;

    @BeforeEach
    void setUp() {
        // Araç oluştur
        arac = new Arac();
        arac.setId(1L);
        arac.setPlaka("34ABC123");
        arac.setKapasite(10);
        arac.setAktif(true);
        arac.setBakimda(false);

        // Mahkumlar oluştur
        mahkum1 = new Mahkum();
        mahkum1.setId(1L);
        mahkum1.setAd("Ahmet");
        mahkum1.setSoyad("Kara");
        mahkum1.setAktif(true);

        mahkum2 = new Mahkum();
        mahkum2.setId(2L);
        mahkum2.setAd("Mehmet");
        mahkum2.setSoyad("Beyaz");
        mahkum2.setAktif(true);

        // Görevliler oluştur
        sofor = new Gorevli();
        sofor.setId(1L);
        sofor.setAd("Ali");
        sofor.setSoyad("Şoför");
        sofor.setRol(Gorevli.GorevliRol.SOFOR);
        sofor.setAktif(true);

        gardiyan = new Gorevli();
        gardiyan.setId(2L);
        gardiyan.setAd("Veli");
        gardiyan.setSoyad("Gardiyan");
        gardiyan.setRol(Gorevli.GorevliRol.GARDIYAN);
        gardiyan.setAktif(true);

        // Görev oluştur
        gorev = new NakilGorevi();
        gorev.setId(1L);
        gorev.setBaslangicNoktasi("Silivri Cezaevi");
        gorev.setVarisNoktasi("Kartal Cezaevi");
        gorev.setPlanlananBaslangic(LocalDateTime.now().plusHours(1));
        gorev.setPlanlananVaris(LocalDateTime.now().plusHours(3));
        gorev.setDurum(NakilGorevi.GorevDurumu.PLANLANDI);
    }

    @Test
    void tumGorevleriGetir_ShouldReturnAllGorevler() {
        // Arrange
        when(nakilGoreviRepository.findAll()).thenReturn(Arrays.asList(gorev));

        // Act
        List<NakilGorevi> result = nakilGoreviService.tumGorevleriGetir();

        // Assert
        assertEquals(1, result.size());
        verify(nakilGoreviRepository, times(1)).findAll();
    }

    @Test
    void gorevBul_WithValidId_ShouldReturnGorev() {
        // Arrange
        when(nakilGoreviRepository.findById(1L)).thenReturn(Optional.of(gorev));

        // Act
        Optional<NakilGorevi> result = nakilGoreviService.gorevBul(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Silivri Cezaevi", result.get().getBaslangicNoktasi());
        verify(nakilGoreviRepository, times(1)).findById(1L);
    }

    @Test
    void gorevKaydet_NewGorev_ShouldSaveSuccessfully() {
        // Arrange
        NakilGorevi newGorev = new NakilGorevi();
        newGorev.setBaslangicNoktasi("Yeni Başlangıç");
        newGorev.setVarisNoktasi("Yeni Varış");
        newGorev.setPlanlananBaslangic(LocalDateTime.now().plusHours(1));
        newGorev.setPlanlananVaris(LocalDateTime.now().plusHours(3));
        newGorev.setDurum(NakilGorevi.GorevDurumu.PLANLANDI);

        Long aracId = 1L;
        List<Long> mahkumIds = Arrays.asList(1L, 2L);
        List<Long> gorevliIds = Arrays.asList(1L, 2L);

        when(aracRepository.findById(aracId)).thenReturn(Optional.of(arac));
        when(mahkumRepository.findAllById(mahkumIds)).thenReturn(Arrays.asList(mahkum1, mahkum2));
        when(gorevliRepository.findAllById(gorevliIds)).thenReturn(Arrays.asList(sofor, gardiyan));
        when(nakilGoreviRepository.save(any(NakilGorevi.class))).thenReturn(newGorev);
        when(nakilGoreviRepository.findByMahkumId(anyLong())).thenReturn(Collections.emptyList());

        // Act
        NakilGorevi result = nakilGoreviService.gorevKaydet(newGorev, aracId, mahkumIds, gorevliIds);

        // Assert
        assertNotNull(result);
        verify(aracRepository, times(1)).findById(aracId);
        verify(mahkumRepository, times(1)).findAllById(mahkumIds);
        verify(gorevliRepository, times(1)).findAllById(gorevliIds);
        verify(nakilGoreviRepository, atLeastOnce()).save(any(NakilGorevi.class));
    }

    @Test
    void gorevKaydet_WithoutSofor_ShouldThrowException() {
        // Arrange
        NakilGorevi newGorev = new NakilGorevi();
        newGorev.setBaslangicNoktasi("Yeni Başlangıç");
        newGorev.setVarisNoktasi("Yeni Varış");
        newGorev.setPlanlananBaslangic(LocalDateTime.now().plusHours(1));
        newGorev.setPlanlananVaris(LocalDateTime.now().plusHours(3));
        newGorev.setDurum(NakilGorevi.GorevDurumu.PLANLANDI);

        Long aracId = 1L;
        List<Long> mahkumIds = Arrays.asList(1L, 2L);
        List<Long> gorevliIds = Arrays.asList(2L); // Sadece gardiyan, şoför yok

        when(aracRepository.findById(aracId)).thenReturn(Optional.of(arac));
        when(mahkumRepository.findAllById(mahkumIds)).thenReturn(Arrays.asList(mahkum1, mahkum2));
        when(gorevliRepository.findAllById(gorevliIds)).thenReturn(Arrays.asList(gardiyan));
        when(nakilGoreviRepository.findByMahkumId(anyLong())).thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            nakilGoreviService.gorevKaydet(newGorev, aracId, mahkumIds, gorevliIds);
        });

        assertTrue(exception.getMessage().contains("en az bir şoför"));
    }

    @Test
    void gorevBaslat_ValidGorev_ShouldUpdateStatus() {
        // Arrange
        gorev.setArac(arac);
        gorev.setMahkumlar(Arrays.asList(mahkum1, mahkum2));
        gorev.setGorevliler(Arrays.asList(sofor, gardiyan));

        when(nakilGoreviRepository.findById(1L)).thenReturn(Optional.of(gorev));
        when(nakilGoreviRepository.save(any(NakilGorevi.class))).thenReturn(gorev);

        // Act
        nakilGoreviService.gorevBaslat(1L);

        // Assert
        assertEquals(NakilGorevi.GorevDurumu.BASLADI, gorev.getDurum());
        verify(nakilGoreviRepository, times(1)).findById(1L);
        verify(nakilGoreviRepository, times(1)).save(gorev);
    }

    @Test
    void gorevBaslat_WithoutArac_ShouldThrowException() {
        // Arrange
        gorev.setArac(null); // Araç yok
        gorev.setMahkumlar(Arrays.asList(mahkum1, mahkum2));
        gorev.setGorevliler(Arrays.asList(sofor, gardiyan));

        when(nakilGoreviRepository.findById(1L)).thenReturn(Optional.of(gorev));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            nakilGoreviService.gorevBaslat(1L);
        });

        assertTrue(exception.getMessage().contains("Görevde araç bulunmuyor"));
        verify(nakilGoreviRepository, never()).save(any());
    }

    @Test
    void gorevTamamla_ValidGorev_ShouldUpdateStatus() {
        // Arrange
        gorev.setDurum(NakilGorevi.GorevDurumu.DEVAM_EDIYOR);

        when(nakilGoreviRepository.findById(1L)).thenReturn(Optional.of(gorev));
        when(nakilGoreviRepository.save(any(NakilGorevi.class))).thenReturn(gorev);

        // Act
        nakilGoreviService.gorevTamamla(1L);

        // Assert
        assertEquals(NakilGorevi.GorevDurumu.TAMAMLANDI, gorev.getDurum());
        assertNotNull(gorev.getTamamlanmaTarihi());
        verify(nakilGoreviRepository, times(1)).findById(1L);
        verify(nakilGoreviRepository, times(1)).save(gorev);
    }

    @Test
    void gorevTamamla_NotInProgress_ShouldThrowException() {
        // Arrange
        gorev.setDurum(NakilGorevi.GorevDurumu.PLANLANDI); // PLANLANDI durumunda

        when(nakilGoreviRepository.findById(1L)).thenReturn(Optional.of(gorev));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            nakilGoreviService.gorevTamamla(1L);
        });

        assertTrue(exception.getMessage().contains("Sadece devam eden veya başlamış görevler"));
        verify(nakilGoreviRepository, never()).save(any());
    }

    @Test
    void gorevSil_PlannedGorev_ShouldDeleteSuccessfully() {
        // Arrange
        gorev.setDurum(NakilGorevi.GorevDurumu.PLANLANDI);
        gorev.setMahkumlar(new ArrayList<>());
        gorev.setGorevliler(new ArrayList<>());

        when(nakilGoreviRepository.findById(1L)).thenReturn(Optional.of(gorev));

        // Act
        nakilGoreviService.gorevSil(1L);

        // Assert
        verify(nakilGoreviRepository, times(1)).delete(gorev);
    }

    @Test
    void gorevSil_CompletedGorev_ShouldThrowException() {
        // Arrange
        gorev.setDurum(NakilGorevi.GorevDurumu.TAMAMLANDI);

        when(nakilGoreviRepository.findById(1L)).thenReturn(Optional.of(gorev));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            nakilGoreviService.gorevSil(1L);
        });

        assertTrue(exception.getMessage().contains("Tamamlanan görevler silinemez"));
        verify(nakilGoreviRepository, never()).delete(any());
    }

    @Test
    void durumaGoreGorevler_ShouldReturnFilteredGorevler() {
        // Arrange
        NakilGorevi tamamlananGorev = new NakilGorevi();
        tamamlananGorev.setId(2L);
        tamamlananGorev.setDurum(NakilGorevi.GorevDurumu.TAMAMLANDI);

        when(nakilGoreviRepository.findByDurum(NakilGorevi.GorevDurumu.TAMAMLANDI))
                .thenReturn(Arrays.asList(tamamlananGorev));

        // Act
        List<NakilGorevi> result = nakilGoreviService.durumaGoreGorevler(NakilGorevi.GorevDurumu.TAMAMLANDI);

        // Assert
        assertEquals(1, result.size());
        assertEquals(NakilGorevi.GorevDurumu.TAMAMLANDI, result.get(0).getDurum());
        verify(nakilGoreviRepository, times(1)).findByDurum(NakilGorevi.GorevDurumu.TAMAMLANDI);
    }

    @Test
    void aktifGorevler_ShouldReturnActiveGorevler() {
        // Arrange
        NakilGorevi devamEdenGorev = new NakilGorevi();
        devamEdenGorev.setId(3L);
        devamEdenGorev.setDurum(NakilGorevi.GorevDurumu.DEVAM_EDIYOR);

        when(nakilGoreviRepository.findDevamEdenGorevler()).thenReturn(Arrays.asList(devamEdenGorev));

        // Act
        List<NakilGorevi> result = nakilGoreviService.aktifGorevler();

        // Assert
        assertEquals(1, result.size());
        assertEquals(NakilGorevi.GorevDurumu.DEVAM_EDIYOR, result.get(0).getDurum());
        verify(nakilGoreviRepository, times(1)).findDevamEdenGorevler();
    }

    @Test
    void getIstatistikler_ShouldReturnStatistics() {
        // Arrange
        // Test verilerini doğrudan oluştur
        NakilGorevi gorev1 = new NakilGorevi();
        gorev1.setDurum(NakilGorevi.GorevDurumu.PLANLANDI);

        NakilGorevi gorev2 = new NakilGorevi();
        gorev2.setDurum(NakilGorevi.GorevDurumu.BASLADI);

        NakilGorevi gorev3 = new NakilGorevi();
        gorev3.setDurum(NakilGorevi.GorevDurumu.DEVAM_EDIYOR);

        NakilGorevi gorev4 = new NakilGorevi();
        gorev4.setDurum(NakilGorevi.GorevDurumu.TAMAMLANDI);

        NakilGorevi gorev5 = new NakilGorevi();
        gorev5.setDurum(NakilGorevi.GorevDurumu.IPTAL_EDILDI);

        List<NakilGorevi> allGorevler = Arrays.asList(gorev1, gorev2, gorev3, gorev4, gorev5);

        when(nakilGoreviRepository.findAll()).thenReturn(allGorevler);

        // Act
        NakilGoreviService.NakilGoreviIstatistikleri result = nakilGoreviService.getIstatistikler();

        // Assert
        assertEquals(5, result.getToplamGorev());
        assertEquals(1, result.getToplamPlanlandi());
        assertEquals(1, result.getToplamBasladi());
        assertEquals(1, result.getToplamDevamEden());
        assertEquals(1, result.getToplamTamamlandi());
        assertEquals(1, result.getToplamIptalEdildi());
        verify(nakilGoreviRepository, times(1)).findAll();
    }
}