package br.com.victor.health.exams.software.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.victor.health.exams.software.entities.HealthcareInstitution;

public interface HealthcareInstitutionRepository extends JpaRepository<HealthcareInstitution, Integer>{

}
