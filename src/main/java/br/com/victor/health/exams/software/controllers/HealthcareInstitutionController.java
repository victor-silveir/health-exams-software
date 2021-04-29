package br.com.victor.health.exams.software.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.services.HealthcareInstitutionService;

@Validated
@RestController
@RequestMapping("/healthcareinstitutions")
public class HealthcareInstitutionController {
	@Autowired
	private HealthcareInstitutionService healthcareInstitutionService;

	@PostMapping
	public ResponseEntity<HealthcareInstitution> post(@Valid @RequestBody HealthcareInstitution healthcareInstitution) {

		healthcareInstitution = healthcareInstitutionService.saveInstitution(healthcareInstitution);

		URI clienteURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(healthcareInstitution.getId())
				.toUri();
		return ResponseEntity.created(clienteURI).body(healthcareInstitution);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<HealthcareInstitution> findClienteById(@PathVariable Integer id) {
		HealthcareInstitution healthcareInstitution = healthcareInstitutionService.findInstitutionById(id);
		return ResponseEntity.ok().body(healthcareInstitution);
	}

}
