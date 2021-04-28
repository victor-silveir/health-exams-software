package br.com.victor.health.exams.software.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.victor.health.exams.software.dtos.ExamDto;
import br.com.victor.health.exams.software.dtos.UpdateExamDto;
import br.com.victor.health.exams.software.entities.Exam;
import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.repositories.ExamRepository;

@Service
public class ExamService {

	@Autowired
	private ExamRepository examRepository;

	@Autowired
	private HealthcareInstitutionService healthcareInstitutionService;

	@Autowired
	private ModelMapper modelMapper;
	
	public List<Exam> findAllExamsByInstitution(HealthcareInstitution healthcareInstitution) {

		healthcareInstitutionService.findInstitutionById(healthcareInstitution.getId());

		return examRepository.findByHealthcareInstitution(healthcareInstitution);

	}

	public Exam findExamById(Integer id, Integer institutionId) {

		Optional<Exam> optionalExam = examRepository.findById(id);

		Exam exam = optionalExam.orElseThrow();

		if (!institutionId.equals(exam.getHealthcareInstitution().getId())) {
			throw new Error();
		}

		if (!exam.isRequested() && exam.getHealthcareInstitution().checkPixeonBalance(1)) {
			throw new Error();
		}

		if (!exam.isRequested()) {
			exam.getHealthcareInstitution().chargePixeonCoins(1);
			exam.setRequested(true);
			examRepository.save(exam);
			healthcareInstitutionService.saveInstitution(exam.getHealthcareInstitution());
		}

		return exam;

	}

	public Exam saveExam(Exam exam) {

		exam.setId(null);
		
		if (examRepository.findByHealthcareInstitutionAndPatientNameAndProcedureNameAndPhysicianName(exam.getHealthcareInstitution(), exam.getPatientName(), exam.getProcedureName(), exam.getPhysicianName()) != null) {
			throw new Error();
		}

		if (healthcareInstitutionService.findInstitutionById(exam.getHealthcareInstitution().getId()) == null) {
			throw new Error();
		}

		if (exam.getHealthcareInstitution().checkPixeonBalance(1)) {
			throw new Error();
		}

		exam.setHealthcareInstitution(exam.getHealthcareInstitution());
		exam.getHealthcareInstitution().chargePixeonCoins(1);
		healthcareInstitutionService.saveInstitution(exam.getHealthcareInstitution());

		return examRepository.save(exam);

	}

	public Exam updateExam(Exam exam, Integer id) {
		
		exam.setId(id);

		findExamById(id, exam.getHealthcareInstitution().getId());

		return examRepository.save(exam);
	}

	public void deleteExam(Exam exam, Integer id) {
		
		findExamById(id, exam.getHealthcareInstitution().getId());
		
		examRepository.delete(exam);		
		
	}
	
	public Exam toExam(ExamDto examDto) {
		return modelMapper.map(examDto, Exam.class);
	}
	
	public Exam toExam(UpdateExamDto examDto) {
		return modelMapper.map(examDto, Exam.class);
	}
	
	public ExamDto toExamDto(Exam exam) {
		return modelMapper.map(exam, ExamDto.class);
	}

}
