package com.example.akilli.mahkum.nakil_sistemi.repository;
import com.example.akilli.mahkum.nakil_sistemi.model.Ihlal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IhlalRepository extends JpaRepository<Ihlal, Long> {
}
