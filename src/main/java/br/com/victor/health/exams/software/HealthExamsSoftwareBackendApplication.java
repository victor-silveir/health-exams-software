package br.com.victor.health.exams.software;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.victor.health.exams.software.entities.Exam;
import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.entities.enums.Gender;
import br.com.victor.health.exams.software.repositories.ExamRepository;
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;

@SpringBootApplication
public class HealthExamsSoftwareBackendApplication implements CommandLineRunner{
	
	@Autowired
	private ExamRepository examRepository;
	
	@Autowired
	private HealthcareInstitutionRepository healthcareInstitutionRepository;

	public static void main(String[] args) {
		SpringApplication.run(HealthExamsSoftwareBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		HealthcareInstitution h1 = new HealthcareInstitution(null, "Hemocentro", "00130166000122");
		HealthcareInstitution h2 = new HealthcareInstitution(null, "Laboratório Sabin", "96374910000103");
		HealthcareInstitution h3 = new HealthcareInstitution(null, "Laboratório Exame", "35976016000119");
		HealthcareInstitution h4 = new HealthcareInstitution(null, "Laboratório Falido", "82104000000104");
		
		h2.chargePixeonCoins(6);
		h3.chargePixeonCoins(1);
		h4.chargePixeonCoins(20);
		
		Exam e1 = new Exam(null, "Hemograma completo", "Cícero", 24, "Juan Gonzales", "123456 DF", false, Gender.MALE);
		Exam e2 = new Exam(null, "Antitransglutaminase igg", "Victor Bruno Alves de Freitas Silveira", 24, "Roberto Carlos", "234567 DF", false, Gender.MALE);
		Exam e3 = new Exam(null, "Endoscopia", "Maria do Carmo", 75, "Bon Jovi", "3654789 UK", false, Gender.FEMALE);
		Exam e4 = new Exam(null, "Colonoscopia", "Matheus", 52, "Mariah Carey", "1233124 GE", false, Gender.FEMALE);
		Exam e5 = new Exam(null, "Exame HIV", "Rodrigo", 17, "Madonna", "123545 GE", false, Gender.FEMALE);
		Exam e6 = new Exam(null, "Teste gravidez", "Rosa Maria", 52, "BloodMary", "11234 AM", false, Gender.FEMALE);
		
		e1.setHealthcareInstitution(h1);
		e2.setHealthcareInstitution(h1);
		e3.setHealthcareInstitution(h3);
		e4.setHealthcareInstitution(h1);
		e5.setHealthcareInstitution(h2);
		e6.setHealthcareInstitution(h1);
		
		List<Exam> exams = new ArrayList<Exam>();
		exams.add(e1);
		exams.add(e2);
		exams.add(e3);
		exams.add(e4);
		exams.add(e5);
		exams.add(e6);
		
		List<HealthcareInstitution> institutions = new ArrayList<HealthcareInstitution>();
		institutions.add(h1);
		institutions.add(h2);
		institutions.add(h3);
		institutions.add(h4);

		
		healthcareInstitutionRepository.saveAll(institutions);
		examRepository.saveAll(exams);


	}

}
