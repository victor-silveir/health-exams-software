package br.com.victor.health.exams.software.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

public class HealthcareInstitution {

	private String id;
	private String name;
	private String cnpj;
	private int PixeonCoins;
	
	@OneToMany(mappedBy = "healthcareInstitution", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Exam> exams;

	{
		PixeonCoins = 20;
	}

	public HealthcareInstitution() {
	}

	public HealthcareInstitution(String id, String name, String cnpj) {
		super();
		this.id = id;
		this.name = name;
		this.cnpj = cnpj;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public int getPixeonCoins() {
		return PixeonCoins;
	}

	public void chargePixeonCoins(int pixeonCoins) {
		PixeonCoins = PixeonCoins - pixeonCoins;
	}
	
	public boolean checkPixeonBalance(int pixeonCoins) {
		return PixeonCoins - pixeonCoins < 0;
	}
	
	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}
	
	public List<Exam> getExams() {
		return exams;
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
		HealthcareInstitution other = (HealthcareInstitution) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
