package com.example.akilli.mahkum.nakil_sistemi.service;

import com.example.akilli.mahkum.nakil_sistemi.model.Ihlal;
import com.example.akilli.mahkum.nakil_sistemi.repository.IhlalRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class IhlalService {

    private final IhlalRepository ihlalRepository;

    public IhlalService(IhlalRepository ihlalRepository) {
        this.ihlalRepository = ihlalRepository;
    }

    public List<Ihlal> tumIhlaller() {
        return ihlalRepository.findAll();
    }

    public Optional<Ihlal> ihlalBul(Long id) {
        return ihlalRepository.findById(id);
    }

    public Ihlal ihlalKaydet(Ihlal ihlal) {
        return ihlalRepository.save(ihlal);
    }

    public void ihlalSil(Long id) {
        ihlalRepository.deleteById(id);
    }

    public List<Ihlal> goreveGoreIhlaller(Long gorevId) {
        return ihlalRepository.findAll().stream()
                .filter(i -> i.getNakilGorevi() != null &&
                        i.getNakilGorevi().getId().equals(gorevId))
                .toList();
    }

    public List<Ihlal> durumaGoreIhlaller(Ihlal.IhlalDurumu durum) {
        return ihlalRepository.findAll().stream()
                .filter(i -> i.getDurum() == durum)
                .toList();
    }

    public Ihlal ihlalDurumGuncelle(Long ihlalId, Ihlal.IhlalDurumu yeniDurum) {
        Optional<Ihlal> optionalIhlal = ihlalRepository.findById(ihlalId);
        if (optionalIhlal.isPresent()) {
            Ihlal ihlal = optionalIhlal.get();
            ihlal.setDurum(yeniDurum);
            return ihlalRepository.save(ihlal);
        }
        return null;
    }
}
