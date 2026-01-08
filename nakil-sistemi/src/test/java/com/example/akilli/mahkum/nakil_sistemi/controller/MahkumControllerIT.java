package com.example.akilli.mahkum.nakil_sistemi.controller;


import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import com.example.akilli.mahkum.nakil_sistemi.service.MahkumService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MahkumController.class)
class MahkumControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MahkumService mahkumService;

    @Test
    void listMahkumlar_ShouldReturnMahkumListPage() throws Exception {
        // Arrange
        Mahkum mahkum = new Mahkum();
        mahkum.setId(1L);
        mahkum.setAd("Ahmet");
        mahkum.setSoyad("Kara");
        mahkum.setTcKimlik("12345678901");
        mahkum.setRiskSeviyesi(Mahkum.RiskSeviyesi.ORTA);

        when(mahkumService.tumMahkumlar()).thenReturn(Arrays.asList(mahkum));

        // Act & Assert
        mockMvc.perform(get("/mahkum/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("mahkum/list"))
                .andExpect(model().attributeExists("mahkumlar"));

        verify(mahkumService, times(1)).tumMahkumlar();
    }

    @Test
    void showAddForm_ShouldReturnAddFormPage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/mahkum/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("mahkum/form"))
                .andExpect(model().attributeExists("mahkum"))
                .andExpect(model().attributeExists("riskSeviyeleri"));
    }

    @Test
    void saveMahkum_NewMahkum_ShouldSaveAndRedirect() throws Exception {
        // Arrange
        Mahkum mahkum = new Mahkum();
        mahkum.setId(1L);

        when(mahkumService.mahkumKaydet(any(Mahkum.class))).thenReturn(mahkum);

        // Act & Assert
        mockMvc.perform(post("/mahkum/save")
                        .param("ad", "Ahmet")
                        .param("soyad", "Kara")
                        .param("tcKimlik", "12345678901")
                        .param("riskSeviyesi", "ORTA"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mahkum/list"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(mahkumService, times(1)).mahkumKaydet(any(Mahkum.class));
    }

    @Test
    void saveMahkum_InvalidData_ShouldReturnToFormWithError() throws Exception {
        // Arrange
        when(mahkumService.mahkumKaydet(any(Mahkum.class)))
                .thenThrow(new RuntimeException("TC Kimlik numarası 11 haneli olmalıdır!"));

        // Act & Assert
        mockMvc.perform(post("/mahkum/save")
                        .param("ad", "Ahmet")
                        .param("soyad", "Kara")
                        .param("tcKimlik", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("errorMessage"));

        verify(mahkumService, times(1)).mahkumKaydet(any(Mahkum.class));
    }

    @Test
    void showEditForm_ValidId_ShouldReturnEditForm() throws Exception {
        // Arrange
        Mahkum mahkum = new Mahkum();
        mahkum.setId(1L);
        mahkum.setAd("Ahmet");

        when(mahkumService.mahkumBul(1L)).thenReturn(Optional.of(mahkum));

        // Act & Assert
        mockMvc.perform(get("/mahkum/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("mahkum/form"))
                .andExpect(model().attributeExists("mahkum"));

        verify(mahkumService, times(1)).mahkumBul(1L);
    }


    @Test
    void deleteMahkum_ValidId_ShouldDeleteAndRedirect() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/mahkum/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mahkum/list"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(mahkumService, times(1)).mahkumSil(1L);
    }

    @Test
    void deleteMahkum_ServiceThrowsException_ShouldShowError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Mahkum bulunamadı"))
                .when(mahkumService).mahkumSil(Long.valueOf(99L));

        // Act & Assert
        mockMvc.perform(post("/mahkum/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mahkum/list"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(mahkumService, times(1)).mahkumSil(99L);
    }

    @Test
    void searchByRisk_ValidRisk_ShouldReturnFilteredList() throws Exception {
        // Arrange
        Mahkum mahkum = new Mahkum();
        mahkum.setId(1L);
        mahkum.setRiskSeviyesi(Mahkum.RiskSeviyesi.ORTA);

        when(mahkumService.riskeGoreAra(Mahkum.RiskSeviyesi.ORTA))
                .thenReturn(Arrays.asList(mahkum));

        // Act & Assert
        mockMvc.perform(get("/mahkum/search")
                        .param("risk", "ORTA"))
                .andExpect(status().isOk())
                .andExpect(view().name("mahkum/list"))
                .andExpect(model().attributeExists("mahkumlar"))
                .andExpect(model().attributeExists("selectedRisk"));

        verify(mahkumService, times(1)).riskeGoreAra(Mahkum.RiskSeviyesi.ORTA);
    }

    @Test
    void searchByRisk_InvalidRisk_ShouldShowAllWithError() throws Exception {
        // Arrange
        when(mahkumService.tumMahkumlar()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/mahkum/search")
                        .param("risk", "INVALID"))
                .andExpect(status().isOk())
                .andExpect(view().name("mahkum/list"))
                .andExpect(model().attributeExists("errorMessage"));

        verify(mahkumService, times(1)).tumMahkumlar();
    }
}