package br.com.victor.health.exams.software.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.victor.health.exams.software.dtos.ExamDto;
import br.com.victor.health.exams.software.dtos.UpdateExamDto;
import br.com.victor.health.exams.software.entities.Exam;
import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.entities.enums.Gender;
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;
import br.com.victor.health.exams.software.services.exceptions.NotEnoughtPixeonCoinsException;
import br.com.victor.health.exams.software.services.exceptions.ObjectAlreadySavedException;
import br.com.victor.health.exams.software.services.exceptions.ObjectNotFoundException;
import br.com.victor.health.exams.software.services.exceptions.PermissionDeniedException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamServiceTest {

	@Autowired
	private ExamService examService;

	@Autowired
	private HealthcareInstitutionRepository healthcareInstitutionRepository;

	private HealthcareInstitution institution = new HealthcareInstitution(null, "Hemocentro de brasília",
			"29003675000176");

	private HealthcareInstitution noCoinsInstitution = new HealthcareInstitution(null, "Laboratório Maria do Socorro",
			"80575258000155");

	@Before
	public void initialize() {

		noCoinsInstitution.chargePixeonCoins(19);

		healthcareInstitutionRepository.save(institution);

		healthcareInstitutionRepository.save(noCoinsInstitution);

	}

	@Test
	public void chargesPixeonCoinsAndCreateExam() {

		ExamDto exam = mockExam(1, institution.getId());

		Exam savedExam = examService.saveExam(exam);

		HealthcareInstitution testInstitution = healthcareInstitutionRepository
				.findById(exam.getHealthcareInstitutionId()).get();

		assertNotEquals(null, savedExam.getId());

		assertEquals(19, testInstitution.getPixeonCoins());

	}

	@Test
	public void chargesPixeonCoinsAndGetExam() {

		ExamDto exam = mockExam(1, institution.getId());

		Exam savedExam = examService.saveExam(exam);

		assertEquals(false, savedExam.isRequested());

		Exam requestedExam = examService.findExamById(savedExam.getId(), exam.getHealthcareInstitutionId());

		HealthcareInstitution testInstitution = healthcareInstitutionRepository
				.findById(exam.getHealthcareInstitutionId()).get();

		assertEquals(true, requestedExam.isRequested());

		assertEquals(18, testInstitution.getPixeonCoins());

	}

	@Test(expected = NotEnoughtPixeonCoinsException.class)
	public void InsufficientPixeonCoinsToCreateExam() {

		noCoinsInstitution.chargePixeonCoins(1);
		
		healthcareInstitutionRepository.save(noCoinsInstitution);

		ExamDto exam = mockExam(1, noCoinsInstitution.getId());

		examService.saveExam(exam);

	}

	@Test(expected = NotEnoughtPixeonCoinsException.class)
	public void insufficientPixeonCoinsToGetExam() {

		ExamDto exam = mockExam(1, noCoinsInstitution.getId());

		Exam savedExam = examService.saveExam(exam);

		examService.findExamById(savedExam.getId(), exam.getHealthcareInstitutionId());

	}

	@Test(expected = NotEnoughtPixeonCoinsException.class)
	public void testChargeCoins() {

		for (int i = 1; i <= 20; i++) {

			ExamDto exam = mockExam(i, institution.getId());

			examService.saveExam(exam);
		}

		ExamDto exam = mockExam(21, institution.getId());

		examService.saveExam(exam);
	}

	@Test
	public void getAlreadyRequestedExamAndDontChargePixeonCoins() {

		ExamDto exam = mockExam(1, institution.getId());

		Exam savedExam = examService.saveExam(exam);

		examService.findExamById(savedExam.getId(), exam.getHealthcareInstitutionId());
		Exam requestedExam = examService.findExamById(savedExam.getId(), exam.getHealthcareInstitutionId());

		HealthcareInstitution testInstitution = healthcareInstitutionRepository
				.findById(exam.getHealthcareInstitutionId()).get();

		assertEquals(true, requestedExam.isRequested());

		assertEquals(18, testInstitution.getPixeonCoins());

	}

	@Test
	public void updateExam() {

		ExamDto exam = mockExam(1, institution.getId());

		Exam savedExam = examService.saveExam(exam);

		UpdateExamDto examData = new UpdateExamDto(1, "Maria do Carmo", 13, Gender.FEMALE, "José", "3214 AM", "Hemograma completo");

		Exam updatedExam = examService.updateExam(examData, savedExam.getId(), savedExam.getHealthcareInstitution().getId());

		assertEquals(exam.getHealthcareInstitutionId(), updatedExam.getHealthcareInstitution().getId());

		assertEquals("Hemograma completo", updatedExam.getProcedureName());
		assertEquals("Maria do Carmo", updatedExam.getPatientName());
		assertEquals(13, updatedExam.getPatientAge());
		assertEquals("José", updatedExam.getPhysicianName());
		assertEquals("3214 AM", updatedExam.getPhysicianCRM());
		assertEquals(Gender.FEMALE, updatedExam.getPatientGender());

		assertNotSame(examData, updatedExam, "The objects are the same!");
	}

	@Test(expected = ObjectNotFoundException.class)
	public void deleteExam() {

		ExamDto exam = mockExam(1, institution.getId());

		Exam savedExam = examService.saveExam(exam);

		examService.deleteExam(exam, savedExam.getId(), savedExam.getHealthcareInstitution().getId());

		examService.findExamById(savedExam.getId(), exam.getHealthcareInstitutionId());

	}


	@Test(expected = PermissionDeniedException.class)
	public void permissionDeniedToAccessExam() {

		ExamDto exam = mockExam(1, institution.getId());

		Exam savedExam = examService.saveExam(exam);

		assertEquals(1, institution.getId());

		examService.findExamById(savedExam.getId(), 2);
	}

	@Test(expected = ObjectAlreadySavedException.class)
	public void examAlreadySaved() {

		ExamDto exam = mockExam(1, institution.getId());

		examService.saveExam(exam);
		
		examService.saveExam(exam);
		
	}

	@Test(expected = ObjectNotFoundException.class)
	public void examWithIdNotFound() {
		
		ExamDto exam = mockExam(1, institution.getId());
				
		examService.saveExam(exam);
				
		examService.findExamById(3, exam.getHealthcareInstitutionId());
		
	}
	
	
	public ExamDto mockExam(int i, int institutionId) {

		ExamDto exam = new ExamDto(null, "Victor Bruno Alves de Freitas Silveira", 24, Gender.MALE, "Roberto Carlos", "123456 DF", "Antitransglutaminase" + i, institutionId);
			
		return exam;
	}

}
