package br.com.victor.health.exams.software.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class HealthcareInstitutionControllerTest {
	
	@Autowired
	HealthcareInstitutionRepository healthcareInstitutionRepository;
	
	
	
	@Test
	public void postHealthcareInstitution() {
		
		
	}
	
	
	
	public HealthcareInstitution createHealthcareInstitution() {
		
		HealthcareInstitution institution = new HealthcareInstitution(null, "Hemocentro", "54481434000180");
		
		return institution;
		
	}

}
