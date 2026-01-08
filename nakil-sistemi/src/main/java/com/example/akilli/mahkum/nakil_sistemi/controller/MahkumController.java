package com.example.akilli.mahkum.nakil_sistemi.controller;

import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import com.example.akilli.mahkum.nakil_sistemi.service.MahkumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mahkum")
public class MahkumController {

    private final MahkumService mahkumService;

    public MahkumController(MahkumService mahkumService) {
        this.mahkumService = mahkumService;
    }

    @GetMapping("/list")
    public String listMahkumlar(Model model) {
        List<Mahkum> mahkumlar = mahkumService.tumMahkumlar();
        model.addAttribute("mahkumlar", mahkumlar);
        return "mahkum/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("mahkum", new Mahkum());
        model.addAttribute("riskSeviyeleri", Mahkum.RiskSeviyesi.values());
        return "mahkum/form";
    }

    @PostMapping("/save")
    public String saveMahkum(@ModelAttribute Mahkum mahkum, RedirectAttributes redirectAttributes) {
        try {
            mahkumService.mahkumKaydet(mahkum);
            redirectAttributes.addFlashAttribute("successMessage",
                    mahkum.getId() == null ? "Mahkum başarıyla eklendi!" : "Mahkum başarıyla güncellendi!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("mahkum", mahkum);
            // Hata durumunda form sayfasına geri dön
            if (mahkum.getId() == null) {
                redirectAttributes.addFlashAttribute("riskSeviyeleri", Mahkum.RiskSeviyesi.values());
                return "redirect:/mahkum/add";
            } else {
                redirectAttributes.addFlashAttribute("riskSeviyeleri", Mahkum.RiskSeviyesi.values());
                return "redirect:/mahkum/edit/" + mahkum.getId();
            }
        }
        return "redirect:/mahkum/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Mahkum mahkum = mahkumService.mahkumBul(id)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz mahkum ID: " + id));
        model.addAttribute("mahkum", mahkum);
        model.addAttribute("riskSeviyeleri", Mahkum.RiskSeviyesi.values());
        return "mahkum/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteMahkum(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mahkumService.mahkumSil(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mahkum başarıyla silindi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Silme işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/mahkum/list";
    }

    // Opsiyonel: Risk seviyesine göre arama
    @GetMapping("/search")
    public String searchByRisk(@RequestParam("risk") String riskSeviyesi, Model model) {
        try {
            Mahkum.RiskSeviyesi risk = Mahkum.RiskSeviyesi.valueOf(riskSeviyesi);
            List<Mahkum> mahkumlar = mahkumService.riskeGoreAra(risk);
            model.addAttribute("mahkumlar", mahkumlar);
            model.addAttribute("selectedRisk", riskSeviyesi);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Geçersiz risk seviyesi!");
            model.addAttribute("mahkumlar", mahkumService.tumMahkumlar());
        }
        return "mahkum/list";
    }
}