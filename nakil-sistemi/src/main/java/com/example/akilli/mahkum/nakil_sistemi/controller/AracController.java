package com.example.akilli.mahkum.nakil_sistemi.controller;

import com.example.akilli.mahkum.nakil_sistemi.model.Arac;
import com.example.akilli.mahkum.nakil_sistemi.service.AracService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/arac")
public class AracController {

    private final AracService aracService;

    public AracController(AracService aracService) {
        this.aracService = aracService;
    }

    // 1. Tüm araçları listele
    @GetMapping("/list")
    public String listAraclar(Model model) {
        try {
            List<Arac> araclar = aracService.tumAraclar();
            model.addAttribute("araclar", araclar);
            model.addAttribute("pageTitle", "Araç Listesi");
            model.addAttribute("toplamArac", aracService.toplamAracSayisi());
            model.addAttribute("aktifArac", aracService.aktifAracSayisi());
            // DÜZELTİLDİ: 'müsaitArac' -> 'musaitArac'
            model.addAttribute("musaitArac", aracService.musaitAracSayisi());

            return "arac/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Araçlar listelenirken hata: " + e.getMessage());
            return "error";
        }
    }

    // 2. Araç ekleme formunu göster
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("arac", new Arac());
        model.addAttribute("aracTipleri", aracService.getAracTipleri());
        model.addAttribute("yakitTurleri", aracService.getYakitTurleri());
        model.addAttribute("pageTitle", "Yeni Araç Ekle");
        return "arac/form";
    }

    // 3. Araç düzenleme formunu göster (ID ile)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Arac arac = aracService.aracBulById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz araç ID: " + id));

            model.addAttribute("arac", arac);
            model.addAttribute("aracTipleri", aracService.getAracTipleri());
            model.addAttribute("yakitTurleri", aracService.getYakitTurleri());
            model.addAttribute("pageTitle", "Araç Düzenle - " + arac.getPlaka());
            return "arac/form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/arac/list";
        }
    }

    // 4. Araç kaydet/güncelle (tek metod)
    @PostMapping("/save")
    public String saveArac(@ModelAttribute Arac arac,
                           RedirectAttributes redirectAttributes) {
        try {
            aracService.aracKaydet(arac);
            redirectAttributes.addFlashAttribute("successMessage",
                    arac.getId() == null ? "Araç başarıyla eklendi!" : "Araç başarıyla güncellendi!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("arac", arac);

            // Hata durumunda form sayfasına geri dön
            if (arac.getId() == null) {
                redirectAttributes.addFlashAttribute("aracTipleri", aracService.getAracTipleri());
                redirectAttributes.addFlashAttribute("yakitTurleri", aracService.getYakitTurleri());
                return "redirect:/arac/add";
            } else {
                redirectAttributes.addFlashAttribute("aracTipleri", aracService.getAracTipleri());
                redirectAttributes.addFlashAttribute("yakitTurleri", aracService.getYakitTurleri());
                return "redirect:/arac/edit/" + arac.getId();
            }
        }
        return "redirect:/arac/list";
    }

    // 5. Araç sil - POST
    @PostMapping("/delete/{id}")
    public String deleteArac(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            aracService.aracSil(id);
            redirectAttributes.addFlashAttribute("successMessage", "Araç başarıyla silindi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Silme işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/arac/list";
    }

    // 6. Araç durumu güncelle (aktif/pasif)
    @GetMapping("/toggle-status/{id}")
    public String toggleAracStatus(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            Arac arac = aracService.aracBulById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            boolean yeniDurum = !arac.isAktif();
            aracService.aracDurumGuncelle(id, yeniDurum);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Araç durumu " + (yeniDurum ? "Aktif" : "Pasif") + " olarak güncellendi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Araç durumu güncellenemedi: " + e.getMessage());
        }
        return "redirect:/arac/list";
    }

    // 7. Araç bakım durumu güncelle
    @GetMapping("/toggle-bakim/{id}")
    public String toggleAracBakim(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            Arac arac = aracService.aracBulById(id)
                    .orElseThrow(() -> new RuntimeException("Araç bulunamadı: ID=" + id));

            boolean yeniDurum = !arac.isBakimda();
            aracService.aracBakimaAl(id, yeniDurum);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Araç bakım durumu " + (yeniDurum ? "Bakımda" : "Bakımda Değil") + " olarak güncellendi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Araç bakım durumu güncellenemedi: " + e.getMessage());
        }
        return "redirect:/arac/list";
    }

    // 8. Müsait araçları listele
    @GetMapping("/müsait")
    public String listMüsaitAraclar(Model model) {
        List<Arac> araclar = aracService.musaitAraclar();
        model.addAttribute("araclar", araclar);
        model.addAttribute("pageTitle", "Müsait Araçlar");
        // DÜZELTİLDİ: 'müsaitAracSayisi' -> 'musaitAracSayisi'
        model.addAttribute("musaitAracSayisi", araclar.size());
        return "arac/list";
    }

    // 9. Bakımdaki araçları listele
    @GetMapping("/bakimdaki")
    public String listBakimdakiAraclar(Model model) {
        List<Arac> araclar = aracService.bakimdakiAraclar();
        model.addAttribute("araclar", araclar);
        model.addAttribute("pageTitle", "Bakımdaki Araçlar");
        model.addAttribute("bakimdakiAracSayisi", araclar.size());
        return "arac/list";
    }

    // 10. Bakım gereken araçları listele
    @GetMapping("/bakim-gereken")
    public String listBakimGerekenAraclar(Model model) {
        List<Arac> araclar = aracService.bakimGerekenAraclar();
        model.addAttribute("araclar", araclar);
        model.addAttribute("pageTitle", "Bakım Gereken Araçlar");
        model.addAttribute("bakimGerekenAracSayisi", araclar.size());
        return "arac/list";
    }

    // 11. Araç arama
    @GetMapping("/search")
    public String searchAraclar(@RequestParam(required = false) String keyword,
                                Model model) {
        List<Arac> araclar;
        if (keyword != null && !keyword.trim().isEmpty()) {
            araclar = aracService.aramaYap(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            araclar = aracService.tumAraclar();
        }

        model.addAttribute("araclar", araclar);
        model.addAttribute("pageTitle", "Araç Arama");
        model.addAttribute("aracSayisi", araclar.size());
        return "arac/list";
    }

    // 12. Araç tipine göre listele
    @GetMapping("/tip/{tip}")
    public String listByTip(@PathVariable String tip, Model model) {
        try {
            Arac.AracTipi aracTipi = Arac.AracTipi.valueOf(tip);
            List<Arac> araclar = aracService.tipeGoreAraclar(aracTipi);

            model.addAttribute("araclar", araclar);
            model.addAttribute("pageTitle", tip + " Tipi Araçlar");
            model.addAttribute("tipAdi", aracTipi.getDisplayName());
            model.addAttribute("aracSayisi", araclar.size());

            return "arac/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Geçersiz araç tipi!");
            model.addAttribute("araclar", aracService.tumAraclar());
            return "arac/list";
        }
    }

    // 13. Araç istatistikleri
    @GetMapping("/istatistikler")
    public String showIstatistikler(Model model) {
        Map<String, Object> istatistikler = aracService.aracIstatistikleri();
        model.addAttribute("istatistikler", istatistikler);
        model.addAttribute("pageTitle", "Araç İstatistikleri");
        return "arac/istatistikler";
    }

    // 14. API: Araçları JSON olarak getir
    @GetMapping("/api/list")
    @ResponseBody
    public List<Arac> getAraclarJson() {
        return aracService.tumAraclar();
    }

    // 15. API: Müsait araçları JSON olarak getir
    // DÜZELTİLDİ: '/api/müsait' -> '/api/musait'
    @GetMapping("/api/musait")
    @ResponseBody
    public List<Arac> getMusaitAraclarJson() {
        return aracService.musaitAraclar();
    }
}