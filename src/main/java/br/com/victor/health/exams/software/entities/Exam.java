package br.com.victor.health.exams.software.entities;

import javax.persistence.Convert;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.victor.health.exams.software.entities.converters.GenderConverter;
import br.com.victor.health.exams.software.entities.enums.Gender;

public class Exam {

	private Integer id;
	private String procedureName;
	private String patientName;
	private int patientAge;
	private String physicianName;
	private String physicianCRM;
	private boolean requested;
	
	@Convert(converter = GenderConverter.class)
	private Gender patientGender;
	
	@ManyToOne
	@JoinColumn(name = "healthcareInstitution_id")
	private HealthcareInstitution healthcareInstitution;

	{
		requested = false;
	}

	public Exam() {
	}

	public Exam(Integer id, String procedureName, String patientName, int patientAge, String physicianName,
			String physicianCRM, boolean requested, Gender patientGender) {
		this.id = id;
		this.procedureName = procedureName;
		this.patientName = patientName;
		this.patientGender = patientGender;
		this.patientAge = patientAge;
		this.physicianName = physicianName;
		this.physicianCRM = physicianCRM;
		this.requested = requested;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public int getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(int patientAge) {
		this.patientAge = patientAge;
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

	public boolean isRequested() {
		return requested;
	}

	public void setRequested(boolean requested) {
		this.requested = requested;
	}
	
	public Gender getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(Gender patientGender) {
		this.patientGender = patientGender;
	}

	public HealthcareInstitution getHealthcareInstitution() {
		return healthcareInstitution;
	}

	public void setHealthcareInstitution(HealthcareInstitution healthcareInstitution) {
		this.healthcareInstitution = healthcareInstitution;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exam other = (Exam) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
