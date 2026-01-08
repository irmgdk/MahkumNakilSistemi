package com.example.akilli.mahkum.nakil_sistemi.controller;


import com.example.akilli.mahkum.nakil_sistemi.model.Arac;
import com.example.akilli.mahkum.nakil_sistemi.service.AracService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AracController.class)
class AracControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AracService aracService;

    private Arac arac;

    @BeforeEach
    void setUp() {
        arac = new Arac();
        arac.setId(1L);
        arac.setPlaka("34ABC123");
        arac.setModel("Mercedes Sprinter");
        arac.setKapasite(10);
        arac.setTip(Arac.AracTipi.NAKİL);
        arac.setAktif(true);
    }

    @Test
    void listAraclar_ShouldReturnAracListPage() throws Exception {
        // Arrange
        when(aracService.tumAraclar()).thenReturn(Arrays.asList(arac));
        when(aracService.toplamAracSayisi()).thenReturn(5L);
        when(aracService.aktifAracSayisi()).thenReturn(3L);
        when(aracService.musaitAracSayisi()).thenReturn(2L);

        // Act & Assert
        mockMvc.perform(get("/arac/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("arac/list"))
                .andExpect(model().attributeExists("araclar"))
                .andExpect(model().attributeExists("toplamArac"))
                .andExpect(model().attributeExists("aktifArac"))
                .andExpect(model().attributeExists("musaitArac"));

        verify(aracService, times(1)).tumAraclar();
    }

    @Test
    void showAddForm_ShouldReturnAddFormPage() throws Exception {
        // Arrange
        when(aracService.getAracTipleri()).thenReturn(Arac.AracTipi.values());
        when(aracService.getYakitTurleri()).thenReturn(Arrays.asList("Dizel", "Benzin"));

        // Act & Assert
        mockMvc.perform(get("/arac/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("arac/form"))
                .andExpect(model().attributeExists("arac"))
                .andExpect(model().attributeExists("aracTipleri"))
                .andExpect(model().attributeExists("yakitTurleri"));

        verify(aracService, times(1)).getAracTipleri();
        verify(aracService, times(1)).getYakitTurleri();
    }

    @Test
    void saveArac_NewArac_ShouldSaveAndRedirect() throws Exception {
        // Arrange
        when(aracService.aracKaydet(any(Arac.class))).thenReturn(arac);

        // Act & Assert
        mockMvc.perform(post("/arac/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("plaka", "34ABC123")
                        .param("model", "Mercedes Sprinter")
                        .param("kapasite", "10")
                        .param("tip", "NAKİL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/arac/list"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(aracService, times(1)).aracKaydet(any(Arac.class));
    }

    @Test
    void saveArac_InvalidPlaka_ShouldReturnToFormWithError() throws Exception {
        // Arrange
        when(aracService.aracKaydet(any(Arac.class)))
                .thenThrow(new RuntimeException("Geçersiz plaka formatı!"));

        when(aracService.getAracTipleri()).thenReturn(Arac.AracTipi.values());
        when(aracService.getYakitTurleri()).thenReturn(Arrays.asList("Dizel", "Benzin"));

        // Act & Assert
        mockMvc.perform(post("/arac/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("plaka", "INVALID")
                        .param("model", "Test Model"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("errorMessage"));

        verify(aracService, times(1)).aracKaydet(any(Arac.class));
    }

    @Test
    void showEditForm_ValidId_ShouldReturnEditForm() throws Exception {
        // Arrange
        when(aracService.aracBulById(1L)).thenReturn(Optional.of(arac));
        when(aracService.getAracTipleri()).thenReturn(Arac.AracTipi.values());
        when(aracService.getYakitTurleri()).thenReturn(Arrays.asList("Dizel", "Benzin"));

        // Act & Assert
        mockMvc.perform(get("/arac/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("arac/form"))
                .andExpect(model().attributeExists("arac"))
                .andExpect(model().attributeExists("aracTipleri"))
                .andExpect(model().attributeExists("yakitTurleri"));

        verify(aracService, times(1)).aracBulById(1L);
    }

    @Test
    void deleteArac_ValidId_ShouldDeleteAndRedirect() throws Exception {
        // Arrange
        when(aracService.aracSil(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/arac/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/arac/list"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(aracService, times(1)).aracSil(1L);
    }

    @Test
    void toggleAracStatus_ValidId_ShouldToggleStatus() throws Exception {
        // Arrange
        when(aracService.aracBulById(1L)).thenReturn(Optional.of(arac));
        when(aracService.aracDurumGuncelle(eq(1L), anyBoolean())).thenReturn(arac);

        // Act & Assert
        mockMvc.perform(get("/arac/toggle-status/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/arac/list"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(aracService, times(1)).aracBulById(1L);
        verify(aracService, times(1)).aracDurumGuncelle(eq(1L), anyBoolean());
    }


    @Test
    void searchAraclar_WithKeyword_ShouldReturnFilteredAraclar() throws Exception {
        // Arrange
        when(aracService.aramaYap("Mercedes")).thenReturn(Arrays.asList(arac));

        // Act & Assert
        mockMvc.perform(get("/arac/search")
                        .param("keyword", "Mercedes"))
                .andExpect(status().isOk())
                .andExpect(view().name("arac/list"))
                .andExpect(model().attributeExists("araclar"))
                .andExpect(model().attributeExists("keyword"));

        verify(aracService, times(1)).aramaYap("Mercedes");
    }


    @Test
    void getAraclarJson_ShouldReturnJsonList() throws Exception {
        // Arrange
        when(aracService.tumAraclar()).thenReturn(Arrays.asList(arac));

        // Act & Assert
        mockMvc.perform(get("/arac/api/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].plaka").value("34ABC123"))
                .andExpect(jsonPath("$[0].model").value("Mercedes Sprinter"));

        verify(aracService, times(1)).tumAraclar();
    }
}