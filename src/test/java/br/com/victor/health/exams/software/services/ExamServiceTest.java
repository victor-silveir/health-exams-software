package br.com.victor.health.exams.software.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.victor.health.exams.software.entities.Exam;
import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.entities.enums.Gender;
import br.com.victor.health.exams.software.repositories.ExamRepository;
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;
import br.com.victor.health.exams.software.services.exceptions.ObjectNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamServiceTest {
	
	@Autowired
	private ExamService examService;
	
	@Autowired
	private HealthcareInstitutionRepository healthcareInstitutionRepository;
	
	@Autowired
	private ExamRepository examRepository;
	
	private HealthcareInstitution institution = new HealthcareInstitution(null, "Hemocentro de brasília", "29003675000176");
	
	private HealthcareInstitution noCoinsInstitution = new HealthcareInstitution(null, "Laboratório Maria do Socorro", "80575258000155");
	
	@Before
	public void initialize() {
		
		noCoinsInstitution.chargePixeonCoins(20);
		
		healthcareInstitutionRepository.save(institution);
		
		healthcareInstitutionRepository.save(noCoinsInstitution);

	}
	
	@Test
	public void chargesPixeonCoinsAndCreateExam() {
		
		Exam exam = mockExam(1);
		
		exam.setHealthcareInstitution(institution);
		
		examService.saveExam(exam);
		
		HealthcareInstitution testInstitution = healthcareInstitutionRepository.findById(exam.getHealthcareInstitution().getId()).get();
		
		assertNotEquals(null, exam.getId());
		
		assertEquals(19, testInstitution.getPixeonCoins());
		
		
	}
	
	@Test
	public void chargesPixeonCoinsAndGetExam() {
		
		Exam exam = mockExam(1);
		
		exam.setHealthcareInstitution(institution);
		
		examService.saveExam(exam);
		
		assertEquals(false, exam.isRequested());
		
		Exam requestedExam = examService.findExamById(exam.getId(), exam.getHealthcareInstitution().getId());
		
		HealthcareInstitution testInstitution = healthcareInstitutionRepository.findById(exam.getHealthcareInstitution().getId()).get();
		
		assertEquals(true, requestedExam.isRequested());
				
		assertEquals(18, testInstitution.getPixeonCoins());
		
		
	}
	
	@Test
	public void getAlreadyRequestedExamAndDontChargePixeonCoins() {
		
		Exam exam = mockExam(1);
		
		exam.setHealthcareInstitution(institution);
		
		examService.saveExam(exam);
		
		examService.findExamById(exam.getId(), exam.getHealthcareInstitution().getId());
		Exam requestedExam = examService.findExamById(exam.getId(), exam.getHealthcareInstitution().getId());
		
		HealthcareInstitution testInstitution = healthcareInstitutionRepository.findById(exam.getHealthcareInstitution().getId()).get();
		
		assertEquals(true, requestedExam.isRequested());
				
		assertEquals(18, testInstitution.getPixeonCoins());

	}
	
	@Test
	public void updateExam() {
		
		Exam exam = mockExam(1);
		
		exam.setHealthcareInstitution(institution);
		
		examService.saveExam(exam);
		
		Exam examData = new Exam(1, "Hemograma completo", "Maria do Carmo", 13, "José", "3214 AM", true, Gender.FEMALE);
		
		Exam updatedExam = examService.updateExam(examData, exam.getId());
		
		assertEquals(exam.getHealthcareInstitution(), updatedExam.getHealthcareInstitution());
		
		assertEquals("Hemograma completo", updatedExam.getProcedureName());
		assertEquals("Maria do Carmo", updatedExam.getPatientName());
		assertEquals(13, updatedExam.getPatientAge());
		assertEquals("José", updatedExam.getPhysicianName());
		assertEquals("3214 AM", updatedExam.getPhysicianCRM());
		assertEquals(Gender.FEMALE, updatedExam.getPatientGender());

		assertNotSame(examData, updatedExam, "The objects are the same!");
	}
	
	@Test
	public void deleteExam() {
		
		Exam exam = mockExam(1);
		
		exam.setHealthcareInstitution(institution);
		
		examService.saveExam(exam);
			
		examService.deleteExam(exam, exam.getId());
		
		assertNull(exam);
				
	}
	
	public Exam mockExam(int i) {
		
		Exam exam = new Exam(null, "Antitransglutaminase:" + i, "Victor Bruno Alves de Freitas Silveira", 24, "Roberto Carlos", "1234 DF", false, Gender.MALE);
		
		return exam;
	}

}
