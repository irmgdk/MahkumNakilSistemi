package com.example.akilli.mahkum.nakil_sistemi.service;


import com.example.akilli.mahkum.nakil_sistemi.model.Mahkum;
import com.example.akilli.mahkum.nakil_sistemi.repository.MahkumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MahkumService {

    private final MahkumRepository mahkumRepository;

    public MahkumService(MahkumRepository mahkumRepository) {
        this.mahkumRepository = mahkumRepository;
    }

    public List<Mahkum> tumMahkumlar() {
        return mahkumRepository.findAll();
    }

    public Optional<Mahkum> mahkumBul(Long id) {
        return mahkumRepository.findById(id);
    }

    public Mahkum mahkumKaydet(Mahkum mahkum) {
        // TC Kimlik numarası kontrolü
        if (mahkum.getId() == null) {
            // Yeni kayıt - TC Kimlik kontrolü
            boolean tcVarMi = mahkumRepository.existsByTcKimlik(mahkum.getTcKimlik());
            if (tcVarMi) {
                throw new RuntimeException("Bu TC Kimlik numarası ile kayıtlı mahkum zaten var: " + mahkum.getTcKimlik());
            }
        } else {
            // Güncelleme - Aynı TC'ye sahip başka kayıt kontrolü
            Optional<Mahkum> mevcutMahkum = mahkumRepository.findById(mahkum.getId());
            if (mevcutMahkum.isPresent()) {
                Mahkum eskiMahkum = mevcutMahkum.get();
                if (!eskiMahkum.getTcKimlik().equals(mahkum.getTcKimlik())) {
                    boolean tcVarMi = mahkumRepository.existsByTcKimlik(mahkum.getTcKimlik());
                    if (tcVarMi) {
                        throw new RuntimeException("Bu TC Kimlik numarası ile kayıtlı başka mahkum var: " + mahkum.getTcKimlik());
                    }
                }
            }
        }

        // TC Kimlik numarası format kontrolü (opsiyonel)
        if (mahkum.getTcKimlik() != null && !mahkum.getTcKimlik().matches("\\d{11}")) {
            throw new RuntimeException("TC Kimlik numarası 11 haneli olmalıdır!");
        }

        return mahkumRepository.save(mahkum);
    }

    public void mahkumSil(Long id) {
        if (!mahkumRepository.existsById(id)) {
            throw new RuntimeException("ID'si " + id + " olan mahkum bulunamadı!");
        }
        mahkumRepository.deleteById(id);
    }

    public List<Mahkum> riskeGoreAra(Mahkum.RiskSeviyesi riskSeviyesi) {
        return mahkumRepository.findByRiskSeviyesi(riskSeviyesi);
    }

    public List<Mahkum> aktifMahkumlar() {
        return mahkumRepository.findByAktifTrue();
    }

    public long toplamMahkumSayisi() {
        return mahkumRepository.count();
    }

    public List<Mahkum> adVeyaSoyadaGoreAra(String aramaKelimesi) {
        return mahkumRepository.findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(
                aramaKelimesi, aramaKelimesi);
    }
}
