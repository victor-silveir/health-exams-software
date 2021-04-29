package br.com.victor.health.exams.software.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.victor.health.exams.software.dtos.ExamDto;
import br.com.victor.health.exams.software.dtos.GetExamDto;
import br.com.victor.health.exams.software.dtos.UpdateExamDto;
import br.com.victor.health.exams.software.entities.Exam;
import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.services.ExamService;

@Validated
@RestController
@RequestMapping("/exams")
public class ExamController {

	@Autowired
	private ExamService examService;

	@PostMapping
	public ResponseEntity<Exam> post(@Valid @RequestBody ExamDto examDto) {
		
		Exam exam = examService.saveExam(examDto);

		URI ExameURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(examDto.getId())
				.toUri();
		return ResponseEntity.created(ExameURI).body(exam);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Exam> findById(@PathVariable("id") Integer id,
			@RequestParam("healthcareInstitutionId") Integer healthcareInstitutionId) {
		Exam Exam = examService.findExamById(id, healthcareInstitutionId);
		return ResponseEntity.ok().body(Exam);
	}

	@GetMapping
	public ResponseEntity<List<GetExamDto>> findAll(@Valid @RequestBody HealthcareInstitution healthcareInstitution) {
		List<GetExamDto> exams = examService.findAllExamsByInstitution(healthcareInstitution).stream()
				.map(examService::toExamDto).collect(Collectors.toList());
		return ResponseEntity.ok().body(exams);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UpdateExamDto> putExame(@PathVariable Integer id, @Valid @RequestBody UpdateExamDto examDto, @RequestParam("healthcareInstitutionId") Integer healthcareInstitutionId) {
		examService.updateExam(examDto, id, healthcareInstitutionId);
		return ResponseEntity.ok().body(examDto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteExame(@PathVariable Integer id, @Valid @RequestBody ExamDto examDto, @RequestParam("healthcareInstitutionId") Integer healthcareInstitutionId) {
		examService.deleteExam(examDto, id, healthcareInstitutionId);
		return ResponseEntity.noContent().build();
	}

}
