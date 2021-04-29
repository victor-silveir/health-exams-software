package br.com.victor.health.exams.software.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;
import br.com.victor.health.exams.software.services.exceptions.ObjectAlreadySavedException;
import br.com.victor.health.exams.software.services.exceptions.ObjectNotFoundException;

@Service
public class HealthcareInstitutionService {
	
	@Autowired
	private HealthcareInstitutionRepository healthcareInstitutionRepository;
	
	public HealthcareInstitution findInstitutionById(Integer id) {
		
		Optional<HealthcareInstitution> optionalInstitution = healthcareInstitutionRepository.findById(id);
		
		return optionalInstitution.orElseThrow(() -> new ObjectNotFoundException("Healthcare Institution not found! Id: " + id + ", Type: " + HealthcareInstitution.class.getSimpleName()));
	}
	
	public List<HealthcareInstitution> findAllInstitutions() {
		return healthcareInstitutionRepository.findAll();
	}
	
	public HealthcareInstitution saveInstitution(HealthcareInstitution healthcareInstitution) {
		
		healthcareInstitution.setId(null);
		
		if (healthcareInstitutionRepository.findByCnpj(healthcareInstitution.getCnpj()) != null) {
			throw new ObjectAlreadySavedException("Healthcare Institution with this CNPJ already exists! CNPJ: " +  healthcareInstitution.getCnpj());
		}
		
		return healthcareInstitutionRepository.save(healthcareInstitution);		
	}

}
