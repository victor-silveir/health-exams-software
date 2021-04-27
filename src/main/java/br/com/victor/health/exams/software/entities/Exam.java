package br.com.victor.health.exams.software.entities;

public class Exam {

	private Integer id;
	private String procedureName;
	private String PatientName;
	private int PatientAge;
	private String physicianName;
	private String physicianCRM;

	private boolean requested;

	{
		requested = false;
	}

	public Exam() {
	}

	public Exam(Integer id, String procedureName, String patientName, int patientAge, String physicianName,
			String physicianCRM, boolean requested) {
		this.id = id;
		this.procedureName = procedureName;
		PatientName = patientName;
		PatientAge = patientAge;
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
		return PatientName;
	}

	public void setPatientName(String patientName) {
		PatientName = patientName;
	}

	public int getPatientAge() {
		return PatientAge;
	}

	public void setPatientAge(int patientAge) {
		PatientAge = patientAge;
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
