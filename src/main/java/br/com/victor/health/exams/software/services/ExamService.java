package br.com.victor.health.exams.software.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.victor.health.exams.software.dtos.ExamDto;
import br.com.victor.health.exams.software.dtos.GetExamDto;
import br.com.victor.health.exams.software.dtos.UpdateExamDto;
import br.com.victor.health.exams.software.entities.Exam;
import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.repositories.ExamRepository;
import br.com.victor.health.exams.software.services.exceptions.NotEnoughtPixeonCoinsException;
import br.com.victor.health.exams.software.services.exceptions.ObjectAlreadySavedException;
import br.com.victor.health.exams.software.services.exceptions.ObjectNotFoundException;
import br.com.victor.health.exams.software.services.exceptions.PermissionDeniedException;

@Service
public class ExamService {

	@Autowired
	private ExamRepository examRepository;

	@Autowired
	private HealthcareInstitutionService healthcareInstitutionService;

	@Autowired
	private ModelMapper modelMapper;
	
	private final int PIXEON_COIN_AMOUNT = 1;
	
	public List<Exam> findAllExamsByInstitution(HealthcareInstitution healthcareInstitution) {

		healthcareInstitutionService.findInstitutionById(healthcareInstitution.getId());

		return examRepository.findByHealthcareInstitution(healthcareInstitution);

	}

	public Exam findExamById(Integer id, Integer institutionId) {

		Optional<Exam> optionalExam = examRepository.findById(id);

		Exam exam = optionalExam.orElseThrow(() -> new ObjectNotFoundException("Exam not found! Id: " + id + ", Type: " + Exam.class.getSimpleName()));

		if (!institutionId.equals(exam.getHealthcareInstitution().getId())) {
			throw new PermissionDeniedException("Permission to access this exam is denied!");
		}

		if (!exam.isRequested() && exam.getHealthcareInstitution().checkPixeonBalance(PIXEON_COIN_AMOUNT)) {
			throw new NotEnoughtPixeonCoinsException("Not enought Pixeon coins to execute this operation!");
		}

		if (!exam.isRequested()) {
			exam.getHealthcareInstitution().chargePixeonCoins(PIXEON_COIN_AMOUNT);
			exam.setRequested(true);
			examRepository.save(exam);
			healthcareInstitutionService.saveInstitution(exam.getHealthcareInstitution());
		}

		return exam;

	}

	public Exam saveExam(Exam exam) {

		exam.setId(null);
		
		if (examRepository.findByHealthcareInstitutionAndPatientNameAndProcedureNameAndPhysicianName(exam.getHealthcareInstitution(), exam.getPatientName(), exam.getProcedureName(), exam.getPhysicianName()) != null) {
			throw new ObjectAlreadySavedException("This exam already exists!");
		}

		if (healthcareInstitutionService.findInstitutionById(exam.getHealthcareInstitution().getId()) == null) {
			throw  new ObjectNotFoundException("Healthcare Institution not found! Id: " + exam.getHealthcareInstitution().getId() + ", Type: " + HealthcareInstitution.class.getSimpleName());
		}

		if (exam.getHealthcareInstitution().checkPixeonBalance(PIXEON_COIN_AMOUNT)) {
			throw new NotEnoughtPixeonCoinsException("Not enought Pixeon coins to execute this operation!");
		}

		exam.setHealthcareInstitution(exam.getHealthcareInstitution());
		exam.getHealthcareInstitution().chargePixeonCoins(PIXEON_COIN_AMOUNT);
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
	
	public GetExamDto toExamDto(Exam exam) {
		return modelMapper.map(exam, GetExamDto.class);
	}

}
