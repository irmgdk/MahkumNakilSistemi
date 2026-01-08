package com.example.akilli.mahkum.nakil_sistemi.controller;

import com.example.akilli.mahkum.nakil_sistemi.model.Arac;
import com.example.akilli.mahkum.nakil_sistemi.model.Gorevli;
import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import com.example.akilli.mahkum.nakil_sistemi.model.NakilGorevi;
import com.example.akilli.mahkum.nakil_sistemi.service.AracService;
import com.example.akilli.mahkum.nakil_sistemi.service.GorevliService;
import com.example.akilli.mahkum.nakil_sistemi.service.MahkumService;
import com.example.akilli.mahkum.nakil_sistemi.service.NakilGoreviService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/nakil-gorevi")
public class NakilGoreviController {

    private final NakilGoreviService nakilGoreviService;
    private final MahkumService mahkumService;
    private final AracService aracService;
    private final GorevliService gorevliService;

    public NakilGoreviController(NakilGoreviService nakilGoreviService,
                                 MahkumService mahkumService,
                                 AracService aracService,
                                 GorevliService gorevliService) {
        this.nakilGoreviService = nakilGoreviService;
        this.mahkumService = mahkumService;
        this.aracService = aracService;
        this.gorevliService = gorevliService;
    }

    @GetMapping("/list")
    public String listGorevler(Model model) {
        try {
            List<NakilGorevi> gorevler = nakilGoreviService.tumGorevler();
            model.addAttribute("gorevler", gorevler);
            return "nakil-gorevi/list";
        } catch (Exception e) {
            model.addAttribute("error", "Görevler yüklenirken hata: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        try {
            model.addAttribute("gorev", new NakilGorevi());
            model.addAttribute("durumlar", NakilGorevi.GorevDurumu.values());
            model.addAttribute("mahkumlar", mahkumService.aktifMahkumlar());
            model.addAttribute("araclar", aracService.aktifAraclar());
            model.addAttribute("gorevliler", gorevliService.aktifGorevliler());
            model.addAttribute("seciliMahkumIds", new ArrayList<Long>());
            model.addAttribute("seciliGorevliIds", new ArrayList<Long>());
            return "nakil-gorevi/form";
        } catch (Exception e) {
            model.addAttribute("error", "Form yüklenirken hata: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/save")
    public String saveGorev(@ModelAttribute NakilGorevi gorev,
                            @RequestParam(value = "aracId", required = false) Long aracId,
                            @RequestParam(value = "mahkumIds", required = false) List<Long> mahkumIds,
                            @RequestParam(value = "gorevliIds", required = false) List<Long> gorevliIds,
                            RedirectAttributes redirectAttributes) {
        try {
            // DEBUG için
            System.out.println("DEBUG - Formdan gelen görev ID: " + gorev.getId());
            System.out.println("DEBUG - ID tipi: " + (gorev.getId() != null ? gorev.getId().getClass().getName() : "null"));

            // Null kontrolü
            if (mahkumIds == null) {
                mahkumIds = new ArrayList<>();
            }
            if (gorevliIds == null) {
                gorevliIds = new ArrayList<>();
            }

            // Araç ID kontrolü
            if (aracId == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Araç seçimi zorunludur!");
                redirectAttributes.addFlashAttribute("gorev", gorev);
                // Hem null hem de 0 (sıfır) kontrolü
                if (gorev.getId() == null || gorev.getId() == 0L) {
                    return "redirect:/nakil-gorevi/add";
                } else {
                    return "redirect:/nakil-gorevi/edit/" + gorev.getId();
                }
            }

            // Service metodunu çağır (aracId parametresi ile)
            NakilGorevi savedGorev = nakilGoreviService.gorevKaydet(gorev, aracId, mahkumIds, gorevliIds);

            // DÜZELTME: Hem null hem de 0 (sıfır) kontrolü - Long tipi için 0L kullan
            String message;
            if (gorev.getId() == null || gorev.getId() == 0L) {
                message = "Görev başarıyla eklendi!";
                System.out.println("DEBUG - Yeni görev eklendi");
            } else {
                message = "Görev başarıyla güncellendi! (ID: " + gorev.getId() + ")";
                System.out.println("DEBUG - Görev güncellendi, ID: " + gorev.getId());
            }

            redirectAttributes.addFlashAttribute("successMessage", message);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "İşlem sırasında bir hata oluştu: " + e.getMessage());

            // Hata durumunda form sayfasına geri dön
            redirectAttributes.addFlashAttribute("gorev", gorev);
            // Hem null hem de 0 (sıfır) kontrolü
            if (gorev.getId() == null || gorev.getId() == 0L) {
                return "redirect:/nakil-gorevi/add";
            } else {
                return "redirect:/nakil-gorevi/edit/" + gorev.getId();
            }
        }
        return "redirect:/nakil-gorevi/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            NakilGorevi gorev = nakilGoreviService.gorevBul(id)
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz görev ID: " + id));

            model.addAttribute("gorev", gorev);
            model.addAttribute("durumlar", NakilGorevi.GorevDurumu.values());
            model.addAttribute("mahkumlar", mahkumService.aktifMahkumlar());
            model.addAttribute("araclar", aracService.aktifAraclar());
            model.addAttribute("gorevliler", gorevliService.aktifGorevliler());

            // Seçili mahkum ve görevli ID'lerini ekle (null kontrolü ile)
            List<Long> seciliMahkumIds = gorev.getMahkumlar() != null ?
                    gorev.getMahkumlar().stream()
                            .map(Mahkum::getId)
                            .toList() : new ArrayList<>();
            model.addAttribute("seciliMahkumIds", seciliMahkumIds);

            List<Long> seciliGorevliIds = gorev.getGorevliler() != null ?
                    gorev.getGorevliler().stream()
                            .map(Gorevli::getId)
                            .toList() : new ArrayList<>();
            model.addAttribute("seciliGorevliIds", seciliGorevliIds);

            return "nakil-gorevi/form";
        } catch (Exception e) {
            model.addAttribute("error", "Görev bulunamadı veya yüklenirken hata: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteGorev(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nakilGoreviService.gorevSil(id);
            redirectAttributes.addFlashAttribute("successMessage", "Görev başarıyla silindi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Silme işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/nakil-gorevi/list";
    }

    @GetMapping("/start/{id}")
    public String startGorev(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nakilGoreviService.gorevBaslat(id);
            redirectAttributes.addFlashAttribute("successMessage", "Görev başlatıldı!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Görev başlatılırken hata: " + e.getMessage());
        }
        return "redirect:/nakil-gorevi/list";
    }

    @GetMapping("/complete/{id}")
    public String completeGorev(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nakilGoreviService.gorevTamamla(id);
            redirectAttributes.addFlashAttribute("successMessage", "Görev tamamlandı!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Görev tamamlanırken hata: " + e.getMessage());
        }
        return "redirect:/nakil-gorevi/list";
    }
}