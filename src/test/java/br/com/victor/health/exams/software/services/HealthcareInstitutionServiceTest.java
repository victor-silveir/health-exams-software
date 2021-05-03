package br.com.victor.health.exams.software.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.services.exceptions.ObjectAlreadySavedException;
import br.com.victor.health.exams.software.services.exceptions.ObjectNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HealthcareInstitutionServiceTest {

	@Autowired
	private HealthcareInstitutionService healthcareInstitutionService;

	@Test
	public void createHealthcareInstitution() {

		HealthcareInstitution healthcareInstitution = new HealthcareInstitution(null, "Centro de coleta de sangue",
				"28839863000176");

		HealthcareInstitution savedInstitution = healthcareInstitutionService.saveInstitution(healthcareInstitution);

		assertNotEquals(null, savedInstitution.getId());

	}

	@Test
	public void findHealthcareInstitutionById() {

		HealthcareInstitution healthcareInstitution = new HealthcareInstitution(null, "Centro de coleta de sangue",
				"29328525000132");

		HealthcareInstitution savedInstitution = healthcareInstitutionService.saveInstitution(healthcareInstitution);

		HealthcareInstitution requestedInstitution = healthcareInstitutionService
				.findInstitutionById(savedInstitution.getId());

		assertSame(savedInstitution.getId(), requestedInstitution.getId());

	}

	@Test(expected = ObjectNotFoundException.class)
	public void healthCareInstitutionNotFound() {

		HealthcareInstitution healthcareInstitution = new HealthcareInstitution(null, "Centro de coleta de sangue",
				"30120971000136");

		HealthcareInstitution savedInstitution = healthcareInstitutionService.saveInstitution(healthcareInstitution);

		HealthcareInstitution requestedInstitution = healthcareInstitutionService
				.findInstitutionById(savedInstitution.getId());

		assertNotNull(requestedInstitution);

		healthcareInstitutionService.findInstitutionById(1231);

	}

	@Test(expected = ObjectAlreadySavedException.class)
	public void healthcareInstitutionAlreadySaved() {

		HealthcareInstitution healthcareInstitution = new HealthcareInstitution(null, "Centro de coleta de sangue",
				"70493332000130");

		healthcareInstitutionService.saveInstitution(healthcareInstitution);
		healthcareInstitutionService.saveInstitution(healthcareInstitution);


	}

}
