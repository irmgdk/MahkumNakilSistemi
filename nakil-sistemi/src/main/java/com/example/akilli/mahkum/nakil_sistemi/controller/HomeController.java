package com.example.akilli.mahkum.nakil_sistemi.controller;

import com.example.akilli.mahkum.nakil_sistemi.model.NakilGorevi;
import com.example.akilli.mahkum.nakil_sistemi.service.AracService;
import com.example.akilli.mahkum.nakil_sistemi.service.IhlalService;
import com.example.akilli.mahkum.nakil_sistemi.service.MahkumService;
import com.example.akilli.mahkum.nakil_sistemi.service.NakilGoreviService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final MahkumService mahkumService;
    private final AracService aracService;
    private final NakilGoreviService nakilGoreviService;
    private final IhlalService ihlalService;

    public HomeController(MahkumService mahkumService,
                          AracService aracService,
                          NakilGoreviService nakilGoreviService,
                          IhlalService ihlalService) {
        this.mahkumService = mahkumService;
        this.aracService = aracService;
        this.nakilGoreviService = nakilGoreviService;
        this.ihlalService = ihlalService;
    }

    @GetMapping("/")
    public String home(Model model) {
        System.out.println("Ana sayfa yükleniyor");
        model.addAttribute("pageTitle", "Akıllı Mahkum Nakil Sistemi");
        model.addAttribute("message", "Hoş Geldiniz");
        model.addAttribute("content","home");

        // Layout için aktif görevleri ekle
        addDashboardStatsForLayout(model);
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        System.out.println("Dashboard yükleniyor");
        model.addAttribute("pageTitle", "Dashboard");

        try {
            // İstatistikler - tüm verileri servislerden al
            List<NakilGorevi> tumGorevler = nakilGoreviService.tumGorevleriGetir();

            model.addAttribute("toplamMahkum", mahkumService.tumMahkumlar().size());
            model.addAttribute("toplamArac", aracService.tumAraclar().size());
            model.addAttribute("toplamGorev", tumGorevler.size());
            model.addAttribute("toplamIhlal", ihlalService.tumIhlaller().size());

            // Aktif görevler (DEVAM_EDIYOR ve BASLADI durumundaki görevler)
            List<NakilGorevi> aktifGorevler = tumGorevler.stream()
                    .filter(g -> g.getDurum() == NakilGorevi.GorevDurumu.DEVAM_EDIYOR ||
                            g.getDurum() == NakilGorevi.GorevDurumu.BASLADI)
                    .collect(Collectors.toList());
            model.addAttribute("aktifGorevler", aktifGorevler);

            // Tamamlanan görevler (TAMAMLANDI durumundaki görevler, en yeni en üstte)
            List<NakilGorevi> tamamlananGorevler = tumGorevler.stream()
                    .filter(g -> g.getDurum() == NakilGorevi.GorevDurumu.TAMAMLANDI)
                    .sorted((g1, g2) -> {
                        // Önce tamamlanma tarihine göre sırala
                        if (g1.getTamamlanmaTarihi() != null && g2.getTamamlanmaTarihi() != null) {
                            return g2.getTamamlanmaTarihi().compareTo(g1.getTamamlanmaTarihi());
                        }
                        // Tarih yoksa oluşturulma tarihine göre sırala
                        if (g1.getOlusturulmaTarihi() != null && g2.getOlusturulmaTarihi() != null) {
                            return g2.getOlusturulmaTarihi().compareTo(g1.getOlusturulmaTarihi());
                        }
                        return 0;
                    })
                    .limit(10) // Son 10 tamamlanan görev
                    .collect(Collectors.toList());
            model.addAttribute("tamamlananGorevler", tamamlananGorevler);

            // Son ihlaller (en fazla 5 tane)
            model.addAttribute("sonIhlaller", ihlalService.tumIhlaller().stream()
                    .sorted((i1, i2) -> {
                        if (i1.getOlusturulmaTarihi() != null && i2.getOlusturulmaTarihi() != null) {
                            return i2.getOlusturulmaTarihi().compareTo(i1.getOlusturulmaTarihi());
                        }
                        return 0;
                    })
                    .limit(5)
                    .collect(Collectors.toList()));

            // Layout için aktif görevleri ekle
            addDashboardStatsForLayout(model);

            System.out.println("Dashboard başarıyla yüklendi");
            return "dashboard";

        } catch (Exception e) {
            System.err.println("Dashboard yüklenirken hata: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Dashboard yüklenirken hata: " + e.getMessage());
            model.addAttribute("pageTitle", "Hata - Dashboard");
            return "error";
        }
    }

    private void addDashboardStatsForLayout(Model model) {
        try {
            // Layout sidebar'ı için aktif görev listesi (sadece DEVAM_EDIYOR ve BASLADI durumundaki görevler)
            List<NakilGorevi> tumGorevler = nakilGoreviService.tumGorevleriGetir();

            List<NakilGorevi> aktifGorevlerListesi = tumGorevler.stream()
                    .filter(g -> g.getDurum() == NakilGorevi.GorevDurumu.DEVAM_EDIYOR ||
                            g.getDurum() == NakilGorevi.GorevDurumu.BASLADI)
                    .collect(Collectors.toList());

            model.addAttribute("aktifGorevlerListesi", aktifGorevlerListesi);

        } catch (Exception e) {
            System.err.println("Layout istatistikleri yüklenirken hata: " + e.getMessage());
            // Hata durumunda boş liste
            model.addAttribute("aktifGorevlerListesi", java.util.Collections.emptyList());
        }
    }

    // Hata sayfası
    @GetMapping("/error")
    public String errorPage(Model model) {
        System.out.println("Hata sayfası yükleniyor");
        model.addAttribute("pageTitle", "Hata");
        model.addAttribute("error", "Sayfa bulunamadı veya bir hata oluştu.");
        return "error";
    }
}