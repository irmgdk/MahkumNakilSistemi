package com.example.akilli.mahkum.nakil_sistemi.service;

import com.example.akilli.mahkum.nakil_sistemi.model.Gorevli;
import com.example.akilli.mahkum.nakil_sistemi.repository.GorevliRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GorevliService {

    private final GorevliRepository gorevliRepository;

    public GorevliService(GorevliRepository gorevliRepository) {
        this.gorevliRepository = gorevliRepository;
    }

    public List<Gorevli> tumGorevliler() {
        return gorevliRepository.findAll();
    }

    public Optional<Gorevli> gorevliBul(Long id) {
        return gorevliRepository.findById(id);
    }

    public Gorevli gorevliKaydet(Gorevli gorevli) {
        // TC Kimlik kontrolü
        if (gorevli.getId() == null) {
            // Yeni kayıt
            boolean tcVarMi = gorevliRepository.existsByTcKimlik(gorevli.getTcKimlik());
            if (tcVarMi) {
                throw new RuntimeException("Bu TC Kimlik numarası ile kayıtlı görevli zaten var: " + gorevli.getTcKimlik());
            }
        } else {
            // Güncelleme
            Optional<Gorevli> mevcutGorevli = gorevliRepository.findById(gorevli.getId());
            if (mevcutGorevli.isPresent()) {
                Gorevli eskiGorevli = mevcutGorevli.get();
                if (!eskiGorevli.getTcKimlik().equals(gorevli.getTcKimlik())) {
                    boolean tcVarMi = gorevliRepository.existsByTcKimlik(gorevli.getTcKimlik());
                    if (tcVarMi) {
                        throw new RuntimeException("Bu TC Kimlik numarası ile kayıtlı başka görevli var: " + gorevli.getTcKimlik());
                    }
                }
            }
        }

        // TC Kimlik format kontrolü
        if (gorevli.getTcKimlik() != null && !gorevli.getTcKimlik().matches("\\d{11}")) {
            throw new RuntimeException("TC Kimlik numarası 11 haneli olmalıdır!");
        }

        return gorevliRepository.save(gorevli);
    }

    public void gorevliSil(Long id) {
        if (!gorevliRepository.existsById(id)) {
            throw new RuntimeException("ID'si " + id + " olan görevli bulunamadı!");
        }
        gorevliRepository.deleteById(id);
    }

    public List<Gorevli> roleGoreAra(Gorevli.GorevliRol rol) {
        return gorevliRepository.findByRol(rol);
    }

    public List<Gorevli> aktifGorevliler() {
        return gorevliRepository.findByAktifTrue();
    }

    public List<Gorevli> müsaitGorevliler() {
        return gorevliRepository.findByAktifTrueAndMüsaitTrue();
    }

    public Gorevli gorevliDurumGuncelle(Long id, boolean aktif) {
        Gorevli gorevli = gorevliRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görevli bulunamadı: " + id));
        gorevli.setAktif(aktif);
        return gorevliRepository.save(gorevli);
    }

    public Gorevli gorevliMüsaitlikGuncelle(Long id, boolean müsait) {
        Gorevli gorevli = gorevliRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görevli bulunamadı: " + id));
        gorevli.setMüsait(müsait);
        return gorevliRepository.save(gorevli);
    }

    public long toplamGorevliSayisi() {
        return gorevliRepository.count();
    }

    public long aktifGorevliSayisi() {
        return gorevliRepository.countByAktifTrue();
    }

    public long roluneGoreSayi(Gorevli.GorevliRol rol) {
        return gorevliRepository.countByRol(rol);
    }

    public Optional<Gorevli> tcKimligeGoreBul(String tcKimlik) {
        return gorevliRepository.findByTcKimlik(tcKimlik);
    }

    public List<Gorevli> adVeyaSoyadaGoreAra(String aramaKelimesi) {
        return gorevliRepository.findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(
                aramaKelimesi, aramaKelimesi);
    }
}