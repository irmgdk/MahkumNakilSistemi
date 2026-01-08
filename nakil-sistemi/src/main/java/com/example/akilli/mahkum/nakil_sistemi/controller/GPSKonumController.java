package com.example.akilli.mahkum.nakil_sistemi.controller;

import com.example.akilli.mahkum.nakil_sistemi.model.GPSKonum;
import com.example.akilli.mahkum.nakil_sistemi.service.GPSKonumService;
import com.example.akilli.mahkum.nakil_sistemi.service.NakilGoreviService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/gps")
public class GPSKonumController {

    private final GPSKonumService gpsKonumService;
    private final NakilGoreviService nakilGoreviService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public GPSKonumController(GPSKonumService gpsKonumService,
                              NakilGoreviService nakilGoreviService) {
        this.gpsKonumService = gpsKonumService;
        this.nakilGoreviService = nakilGoreviService;
    }

    // Canlı takip sayfası (tek çalışan sayfa)
    @GetMapping("/canli/{gorevId}")
    public String canliTakip(@PathVariable Long gorevId, Model model) {
        try {
            var gorevOpt = nakilGoreviService.gorevBul(gorevId);
            if (gorevOpt.isEmpty()) {
                model.addAttribute("error", "Görev bulunamadı: " + gorevId);
                return "error";
            }

            var gorev = gorevOpt.get();
            GPSKonum sonKonum = gpsKonumService.sonKonumuGetir(gorevId);
            double tamamlanmaOrani = gpsKonumService.rotaTamamlamaOrani(gorevId);

            model.addAttribute("gorev", gorev);
            model.addAttribute("sonKonum", sonKonum);
            model.addAttribute("tamamlanmaOrani", String.format("%.1f", tamamlanmaOrani));

            return "gps/canli";

        } catch (Exception e) {
            model.addAttribute("error", "Hata: " + e.getMessage());
            return "error";
        }
    }

    // Demo gösterim başlat
    @PostMapping("/canli/demo/baslat/{gorevId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> demoBaslat(@PathVariable Long gorevId) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Demo gösterim başlatılıyor: " + gorevId);
            gpsKonumService.demoSimulasyonBaslat(gorevId);

            response.put("success", true);
            response.put("message", "Demo gösterim başlatıldı.");
            response.put("timestamp", LocalDateTime.now().format(formatter));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Demo başlatma hatası: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Demo gösterim durdur
    @PostMapping("/canli/demo/durdur/{gorevId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> demoDurdur(@PathVariable Long gorevId) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Demo gösterim durduruluyor: " + gorevId);
            gpsKonumService.demoSimulasyonDurdur(gorevId);

            response.put("success", true);
            response.put("message", "Demo gösterim durduruldu.");
            response.put("timestamp", LocalDateTime.now().format(formatter));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Demo durdurma hatası: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Demo konum güncelle
    @PostMapping("/canli/demo/guncelle/{gorevId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> demoKonumGuncelle(@PathVariable Long gorevId) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Demo konum güncelleniyor: " + gorevId);
            GPSKonum yeniKonum = gpsKonumService.demoSimulasyonKonumuGuncelle(gorevId);

            response.put("success", true);
            response.put("message", "Demo konum güncellendi.");
            response.put("konum", yeniKonum);
            response.put("timestamp", LocalDateTime.now().format(formatter));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Demo konum güncelleme hatası: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Demo gösterim sıfırla
    @PostMapping("/canli/demo/sifirla/{gorevId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> demoSifirla(@PathVariable Long gorevId) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Demo gösterim sıfırlanıyor: " + gorevId);
            gpsKonumService.demoSimulasyonSifirla(gorevId);

            response.put("success", true);
            response.put("message", "Demo gösterim sıfırlandı.");
            response.put("timestamp", LocalDateTime.now().format(formatter));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Demo sıfırlama hatası: " + e.getMessage());

            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Canlı takip için son konum endpoint'i
    @GetMapping(value = "/canli/api/son-konum/{gorevId}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCanliSonKonum(@PathVariable Long gorevId) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Canlı API - Son konum çağrıldı: " + gorevId);

            GPSKonum sonKonum = gpsKonumService.sonKonumuGetir(gorevId);
            double tamamlanmaOrani = gpsKonumService.rotaTamamlamaOrani(gorevId);

            System.out.println("Son konum: " + (sonKonum != null ? "Var" : "Yok"));

            response.put("success", true);
            response.put("sonKonum", sonKonum);
            response.put("tamamlanmaOrani", tamamlanmaOrani);
            response.put("timestamp", LocalDateTime.now().format(formatter));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Canlı API Hatası - Son konum: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", "Son konum yüklenirken hata: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now().format(formatter));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Hata sayfası için
    @GetMapping("/error")
    public String errorPage(Model model) {
        return "error";
    }

    // Test endpoint'i
    @GetMapping("/api/test")
    @ResponseBody
    public Map<String, Object> testApi() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "API çalışıyor");
        response.put("timestamp", LocalDateTime.now().format(formatter));
        return response;
    }
}