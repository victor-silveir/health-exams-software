package br.com.victor.health.exams.software.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.victor.health.exams.software.entities.HealthcareInstitution;

public class ExamDto {
	
	private Integer id;
	
	@NotEmpty(message = "Patient Name is required")
	@Size(min = 3, max = 100, message = "Patient Name must be between 3 and 100 characters.")
    private String patientName;

	@NotEmpty(message = "Patient Age is required")
	@Pattern(regexp="^(0|[1-9][0-9]*)$", message="Patient Age must be a number.")
    private Integer patientAge;

	@NotEmpty(message = "Patient Gender is required.")
    private String patientGender;

	@NotEmpty(message = "Physician Name is required.")
	@Size(min = 3, max = 100, message = "Physician Name must be between 3 and 100 characters.")
    private String physicianName;

	@NotEmpty(message = "Physician CRM is required.")
    private String physicianCRM;

	@NotEmpty(message = "Procedure Name is required.")
	@Size(max = 100, message = "Procedure Name must be at last 100 characters.")
    private String procedureName;

	@NotEmpty(message = "Healthcare Institution ID is required.")
    private HealthcareInstitution healthcareInstitution;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public String getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}

	public String getPhysicianName() {
		return physicianName;
	}

	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}

	public String getPhysicianCRM() {
		return physicianCRM;
	}

	public void setPhysicianCRM(String physicianCRM) {
		this.physicianCRM = physicianCRM;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public HealthcareInstitution getHealthcareInstitution() {
		return healthcareInstitution;
	}

	public void setHealthcareInstitution(HealthcareInstitution healthcareInstitution) {
		this.healthcareInstitution = healthcareInstitution;
	}
	
	

}
