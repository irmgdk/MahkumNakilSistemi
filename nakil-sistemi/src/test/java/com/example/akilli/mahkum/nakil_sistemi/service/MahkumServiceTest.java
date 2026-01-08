package com.example.akilli.mahkum.nakil_sistemi.service;


import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import com.example.akilli.mahkum.nakil_sistemi.repository.MahkumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MahkumServiceTest {

    @Mock
    private MahkumRepository mahkumRepository;

    @InjectMocks
    private MahkumService mahkumService;

    private Mahkum mahkum1;
    private Mahkum mahkum2;

    @BeforeEach
    void setUp() {
        mahkum1 = new Mahkum();
        mahkum1.setId(1L);
        mahkum1.setAd("Ahmet");
        mahkum1.setSoyad("Kara");
        mahkum1.setTcKimlik("12345678901");
        mahkum1.setRiskSeviyesi(Mahkum.RiskSeviyesi.ORTA);
        mahkum1.setAktif(true);

        mahkum2 = new Mahkum();
        mahkum2.setId(2L);
        mahkum2.setAd("Mehmet");
        mahkum2.setSoyad("Beyaz");
        mahkum2.setTcKimlik("98765432109");
        mahkum2.setRiskSeviyesi(Mahkum.RiskSeviyesi.YÜKSEK);
        mahkum2.setAktif(true);
    }

    @Test
    void tumMahkumlar_ShouldReturnAllMahkumlar() {
        // Arrange
        when(mahkumRepository.findAll()).thenReturn(Arrays.asList(mahkum1, mahkum2));

        // Act
        List<Mahkum> result = mahkumService.tumMahkumlar();

        // Assert
        assertEquals(2, result.size());
        verify(mahkumRepository, times(1)).findAll();
    }

    @Test
    void mahkumBul_WithValidId_ShouldReturnMahkum() {
        // Arrange
        when(mahkumRepository.findById(1L)).thenReturn(Optional.of(mahkum1));

        // Act
        Optional<Mahkum> result = mahkumService.mahkumBul(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Ahmet", result.get().getAd());
        verify(mahkumRepository, times(1)).findById(1L);
    }

    @Test
    void mahkumBul_WithInvalidId_ShouldReturnEmpty() {
        // Arrange
        when(mahkumRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Mahkum> result = mahkumService.mahkumBul(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(mahkumRepository, times(1)).findById(99L);
    }

    @Test
    void mahkumKaydet_NewMahkum_ShouldSaveSuccessfully() {
        // Arrange
        Mahkum newMahkum = new Mahkum();
        newMahkum.setAd("Yeni");
        newMahkum.setSoyad("Mahkum");
        newMahkum.setTcKimlik("11111111111");

        when(mahkumRepository.existsByTcKimlik("11111111111")).thenReturn(false);
        when(mahkumRepository.save(any(Mahkum.class))).thenReturn(newMahkum);

        // Act
        Mahkum result = mahkumService.mahkumKaydet(newMahkum);

        // Assert
        assertNotNull(result);
        assertEquals("Yeni", result.getAd());
        verify(mahkumRepository, times(1)).existsByTcKimlik("11111111111");
        verify(mahkumRepository, times(1)).save(newMahkum);
    }

    @Test
    void mahkumKaydet_DuplicateTcKimlik_ShouldThrowException() {
        // Arrange
        Mahkum newMahkum = new Mahkum();
        newMahkum.setAd("Yeni");
        newMahkum.setSoyad("Mahkum");
        newMahkum.setTcKimlik("12345678901");

        when(mahkumRepository.existsByTcKimlik("12345678901")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mahkumService.mahkumKaydet(newMahkum);
        });

        assertTrue(exception.getMessage().contains("TC Kimlik numarası ile kayıtlı mahkum zaten var"));
        verify(mahkumRepository, times(1)).existsByTcKimlik("12345678901");
        verify(mahkumRepository, never()).save(any());
    }

    @Test
    void mahkumKaydet_InvalidTcKimlikFormat_ShouldThrowException() {
        // Arrange
        Mahkum newMahkum = new Mahkum();
        newMahkum.setAd("Yeni");
        newMahkum.setSoyad("Mahkum");
        newMahkum.setTcKimlik("123"); // 11 haneli değil

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mahkumService.mahkumKaydet(newMahkum);
        });

        assertTrue(exception.getMessage().contains("TC Kimlik numarası 11 haneli olmalıdır"));
    }

    @Test
    void mahkumSil_ValidId_ShouldDeleteSuccessfully() {
        // Arrange
        when(mahkumRepository.existsById(1L)).thenReturn(true);

        // Act
        mahkumService.mahkumSil(1L);

        // Assert
        verify(mahkumRepository, times(1)).existsById(1L);
        verify(mahkumRepository, times(1)).deleteById(1L);
    }

    @Test
    void mahkumSil_InvalidId_ShouldThrowException() {
        // Arrange
        when(mahkumRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mahkumService.mahkumSil(99L);
        });

        assertTrue(exception.getMessage().contains("bulunamadı"));
        verify(mahkumRepository, times(1)).existsById(99L);
        verify(mahkumRepository, never()).deleteById(any());
    }

    @Test
    void riskeGoreAra_ShouldReturnFilteredMahkumlar() {
        // Arrange
        when(mahkumRepository.findByRiskSeviyesi(Mahkum.RiskSeviyesi.ORTA))
                .thenReturn(Arrays.asList(mahkum1));

        // Act
        List<Mahkum> result = mahkumService.riskeGoreAra(Mahkum.RiskSeviyesi.ORTA);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Mahkum.RiskSeviyesi.ORTA, result.get(0).getRiskSeviyesi());
        verify(mahkumRepository, times(1)).findByRiskSeviyesi(Mahkum.RiskSeviyesi.ORTA);
    }

    @Test
    void aktifMahkumlar_ShouldReturnOnlyActiveMahkumlar() {
        // Arrange
        Mahkum pasifMahkum = new Mahkum();
        pasifMahkum.setId(3L);
        pasifMahkum.setAktif(false);

        when(mahkumRepository.findByAktifTrue()).thenReturn(Arrays.asList(mahkum1, mahkum2));

        // Act
        List<Mahkum> result = mahkumService.aktifMahkumlar();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Mahkum::isAktif));
        verify(mahkumRepository, times(1)).findByAktifTrue();
    }

    @Test
    void adVeyaSoyadaGoreAra_ShouldReturnMatchingMahkumlar() {
        // Arrange
        String aramaKelimesi = "Ahmet";
        when(mahkumRepository.findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(aramaKelimesi, aramaKelimesi))
                .thenReturn(Arrays.asList(mahkum1));

        // Act
        List<Mahkum> result = mahkumService.adVeyaSoyadaGoreAra(aramaKelimesi);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getAd().contains(aramaKelimesi));
        verify(mahkumRepository, times(1))
                .findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(aramaKelimesi, aramaKelimesi);
    }

    @Test
    void toplamMahkumSayisi_ShouldReturnCount() {
        // Arrange
        when(mahkumRepository.count()).thenReturn(10L);

        // Act
        long result = mahkumService.toplamMahkumSayisi();

        // Assert
        assertEquals(10L, result);
        verify(mahkumRepository, times(1)).count();
    }
}