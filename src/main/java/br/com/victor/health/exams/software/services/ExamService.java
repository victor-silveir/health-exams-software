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
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;
import br.com.victor.health.exams.software.services.exceptions.NotEnoughtPixeonCoinsException;
import br.com.victor.health.exams.software.services.exceptions.ObjectAlreadySavedException;
import br.com.victor.health.exams.software.services.exceptions.ObjectNotFoundException;
import br.com.victor.health.exams.software.services.exceptions.PermissionDeniedException;

@Service
public class ExamService {

	@Autowired
	private ExamRepository examRepository;

	@Autowired
	private HealthcareInstitutionRepository healthcareInstitutionRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	private final int PIXEON_COIN_AMOUNT = 1;
	
	public List<Exam> findAllExamsByInstitution(HealthcareInstitution healthcareInstitution) {

		healthcareInstitutionRepository.findById(healthcareInstitution.getId());

		return examRepository.findByHealthcareInstitution(healthcareInstitution);

	}

	public Exam findExamById(Integer id, Integer healthcareInstitutionId) {

		Optional<Exam> optionalExam = examRepository.findById(id);

		Exam exam = optionalExam.orElseThrow(() -> new ObjectNotFoundException("Exam not found! Id: " + id + ", Type: " + Exam.class.getSimpleName()));

		if (!healthcareInstitutionId.equals(exam.getHealthcareInstitution().getId())) {
			throw new PermissionDeniedException("Permission to access this exam is denied!");
		}

		if (!exam.isRequested() && exam.getHealthcareInstitution().checkPixeonBalance(PIXEON_COIN_AMOUNT)) {
			throw new NotEnoughtPixeonCoinsException("Not enought Pixeon coins to execute this operation!");
		}

		if (!exam.isRequested()) {
			exam.getHealthcareInstitution().chargePixeonCoins(PIXEON_COIN_AMOUNT);
			exam.setRequested(true);
			examRepository.save(exam);
			healthcareInstitutionRepository.save(exam.getHealthcareInstitution());
		}

		return exam;

	}

	public Exam saveExam(ExamDto examDto) {
		
		if (healthcareInstitutionRepository.findById(examDto.getHealthcareInstitutionId()) == null || healthcareInstitutionRepository.findById(examDto.getHealthcareInstitutionId()).isEmpty()) {
			throw  new ObjectNotFoundException("Healthcare Institution not found! Id: " + examDto.getHealthcareInstitutionId() + ", Type: " + HealthcareInstitution.class.getSimpleName());
		}
		
		Exam exam = toExam(examDto);
		
		if (examRepository.findByHealthcareInstitutionAndPatientNameAndProcedureNameAndPhysicianName(exam.getHealthcareInstitution(), exam.getPatientName(), exam.getProcedureName(), exam.getPhysicianName()) != null) {
			throw new ObjectAlreadySavedException("This exam already exists!");
		}


		if (exam.getHealthcareInstitution().checkPixeonBalance(PIXEON_COIN_AMOUNT)) {
			throw new NotEnoughtPixeonCoinsException("Not enought Pixeon coins to execute this operation!");
		}

		exam.getHealthcareInstitution().chargePixeonCoins(PIXEON_COIN_AMOUNT);
		
		healthcareInstitutionRepository.save(exam.getHealthcareInstitution());

		return examRepository.save(exam);

	}

	public Exam updateExam(UpdateExamDto examDto, Integer id, Integer healthcareInstitutionId) {

		Exam exam = toExam(examDto);
		
		exam.setId(id);

		exam.setHealthcareInstitution(examRepository.findById(exam.getId()).get().getHealthcareInstitution());
		
		if (!healthcareInstitutionId.equals(exam.getHealthcareInstitution().getId())) {
			throw new PermissionDeniedException("Permission to access this exam is denied!");
		}
		
		findExamById(exam.getId(), exam.getHealthcareInstitution().getId());

		return examRepository.save(exam);
	}

	public void deleteExam(ExamDto examDto, Integer id, Integer healthcareInstitutionId) {
		
		examDto.setId(id);

		Exam exam = toExam(examDto);
		
		if (!healthcareInstitutionId.equals(exam.getHealthcareInstitution().getId())) {
			throw new PermissionDeniedException("Permission to access this exam is denied!");
		}
		
		findExamById(id, exam.getHealthcareInstitution().getId());
		
		examRepository.delete(exam);		
		
	}
	
	public Exam toExam(ExamDto examDto) {

		HealthcareInstitution healthcareInstitution = healthcareInstitutionRepository.findById(examDto.getHealthcareInstitutionId()).get();
		
		Exam exam =  modelMapper.map(examDto, Exam.class);
		
		exam.setHealthcareInstitution(healthcareInstitution);
		
		return exam;
	}
	
	public Exam toExam(UpdateExamDto examDto) {
		return modelMapper.map(examDto, Exam.class);
	}
	
	public GetExamDto toExamDto(Exam exam) {
		return modelMapper.map(exam, GetExamDto.class);
	}

}
