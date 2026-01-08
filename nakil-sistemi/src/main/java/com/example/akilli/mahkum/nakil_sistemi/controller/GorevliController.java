package com.example.akilli.mahkum.nakil_sistemi.controller;



import com.example.akilli.mahkum.nakil_sistemi.model.Gorevli;
import com.example.akilli.mahkum.nakil_sistemi.service.GorevliService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/gorevli")
public class GorevliController {

    private final GorevliService gorevliService;

    public GorevliController(GorevliService gorevliService) {
        this.gorevliService = gorevliService;
    }

    @GetMapping("/list")
    public String listGorevliler(Model model) {
        List<Gorevli> gorevliler = gorevliService.tumGorevliler();
        model.addAttribute("gorevliler", gorevliler);
        return "gorevli/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("gorevli", new Gorevli());
        model.addAttribute("roller", Gorevli.GorevliRol.values());
        return "gorevli/form";
    }

    @PostMapping("/save")
    public String saveGorevli(@ModelAttribute Gorevli gorevli, RedirectAttributes redirectAttributes) {
        try {
            gorevliService.gorevliKaydet(gorevli);
            String message = gorevli.getId() == null ?
                    "Görevli başarıyla eklendi!" : "Görevli başarıyla güncellendi!";
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("gorevli", gorevli);
            redirectAttributes.addFlashAttribute("roller", Gorevli.GorevliRol.values());
            return gorevli.getId() == null ? "redirect:/gorevli/add" : "redirect:/gorevli/edit/" + gorevli.getId();
        }
        return "redirect:/gorevli/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Gorevli gorevli = gorevliService.gorevliBul(id)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz görevli ID: " + id));
        model.addAttribute("gorevli", gorevli);
        model.addAttribute("roller", Gorevli.GorevliRol.values());
        return "gorevli/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteGorevli(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            gorevliService.gorevliSil(id);
            redirectAttributes.addFlashAttribute("successMessage", "Görevli başarıyla silindi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/gorevli/list";
    }

    @PostMapping("/aktif/{id}")
    public String toggleAktif(@PathVariable Long id,
                              @RequestParam boolean aktif,
                              RedirectAttributes redirectAttributes) {
        try {
            gorevliService.gorevliDurumGuncelle(id, aktif);
            String durum = aktif ? "aktif" : "pasif";
            redirectAttributes.addFlashAttribute("successMessage", "Görevli " + durum + " yapıldı!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/gorevli/list";
    }

    @PostMapping("/müsait/{id}")
    public String toggleMüsait(@PathVariable Long id,
                               @RequestParam boolean müsait,
                               RedirectAttributes redirectAttributes) {
        try {
            gorevliService.gorevliMüsaitlikGuncelle(id, müsait);
            String durum = müsait ? "müsait" : "meşgul";
            redirectAttributes.addFlashAttribute("successMessage", "Görevli " + durum + " yapıldı!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/gorevli/list";
    }
}