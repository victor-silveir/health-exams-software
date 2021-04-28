package br.com.victor.health.exams.software.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;

@Service
public class HealthcareInstitutionService {
	
	@Autowired
	private HealthcareInstitutionRepository healthcareInstitutionRepository;
	
	public HealthcareInstitution findInstitutionById(Integer id) {
		
		Optional<HealthcareInstitution> optionalInstitution = healthcareInstitutionRepository.findById(id);
		
		return optionalInstitution.orElseThrow();
	}
	
	public HealthcareInstitution saveInstitution(HealthcareInstitution healthcareInstitution) {
		
		healthcareInstitution.setId(null);
		
		if (healthcareInstitutionRepository.findByCnpj(healthcareInstitution.getCnpj()) != null) {
			throw new Error();
		}
		
		return healthcareInstitutionRepository.save(healthcareInstitution);		
	}

}
