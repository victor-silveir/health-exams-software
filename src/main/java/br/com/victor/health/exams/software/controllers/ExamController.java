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
@RequestMapping("/healthcareinstutions")
public class ExamController {

	@Autowired
	private ExamService examService;

	@PostMapping
	public ResponseEntity<Exam> post(@Valid @RequestBody ExamDto examDto) {
		Exam exam = examService.toExam(examDto);
		exam = examService.saveExam(exam);

		URI ExameURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(exam.getId())
				.toUri();
		return ResponseEntity.created(ExameURI).body(exam);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Exam> findById(@PathVariable("id") Integer id,
			@PathVariable("healthcareInstitutionId") Integer healthcareInstitutionId) {
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
	public ResponseEntity<Exam> putExame(@PathVariable Integer id, @Valid @RequestBody UpdateExamDto examDto) {
		Exam exam = examService.updateExam(examService.toExam(examDto), id);
		return ResponseEntity.ok().body(exam);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteExame(@PathVariable Integer id, @Valid @RequestBody Exam exam) {
		examService.deleteExam(exam, id);
		return ResponseEntity.noContent().build();
	}

}
