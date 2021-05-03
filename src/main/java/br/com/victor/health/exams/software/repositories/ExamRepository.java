package br.com.victor.health.exams.software.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.victor.health.exams.software.entities.Exam;
import br.com.victor.health.exams.software.entities.HealthcareInstitution;

public interface ExamRepository extends JpaRepository<Exam, Integer>{

	public List<Exam> findByHealthcareInstitutionId(Integer healthcareInstitutionId);
	
	public Exam findByHealthcareInstitutionAndPatientNameAndProcedureNameAndPhysicianName(HealthcareInstitution healthcareInstitution, String patientName, String procedureName, String physicianName);
}
