package com.example.akilli.mahkum.nakil_sistemi.controller;
import com.example.akilli.mahkum.nakil_sistemi.model.Ihlal;
import com.example.akilli.mahkum.nakil_sistemi.service.IhlalService;
import com.example.akilli.mahkum.nakil_sistemi.service.NakilGoreviService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/ihlal")
public class IhlalController {

    private final IhlalService ihlalService;
    private final NakilGoreviService nakilGoreviService;

    public IhlalController(IhlalService ihlalService,
                           NakilGoreviService nakilGoreviService) {
        this.ihlalService = ihlalService;
        this.nakilGoreviService = nakilGoreviService;
    }

    @GetMapping("/list")
    public String listIhlaller(Model model) {
        List<Ihlal> ihlaller = ihlalService.tumIhlaller();
        model.addAttribute("ihlaller", ihlaller);
        return "ihlal/list";
    }

    @GetMapping("/gorev/{gorevId}")
    public String gorevIhlalleri(@PathVariable Long gorevId, Model model) {
        model.addAttribute("gorev", nakilGoreviService.gorevBul(gorevId).orElse(null));
        model.addAttribute("ihlaller", ihlalService.goreveGoreIhlaller(gorevId));
        return "ihlal/list";
    }

    @GetMapping("/update-status/{id}/{status}")
    public String updateIhlalStatus(@PathVariable Long id,
                                    @PathVariable Ihlal.IhlalDurumu status) {
        ihlalService.ihlalDurumGuncelle(id, status);
        return "redirect:/ihlal/list";
    }
}
