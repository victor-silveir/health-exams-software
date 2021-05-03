package br.com.victor.health.exams.software.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CNPJ;

@Entity
public class HealthcareInstitution implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "Institution Name is required.")
	private String name;
	
	@NotEmpty(message = "Institution CNPJ is required.")
	@CNPJ(message = "Invalid CNPJ!")
	private String cnpj;
	private int PixeonCoins;
	

	@OneToMany(mappedBy = "healthcareInstitution", cascade = CascadeType.ALL)
	private List<Exam> exams;

	{
		PixeonCoins = 20;
	}

	public HealthcareInstitution() {
	}

	public HealthcareInstitution(Integer id, String name, String cnpj) {
		super();
		this.id = id;
		this.name = name;
		this.cnpj = cnpj;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	
	public void setPixeonCoins(int pixeonCoins) {
		PixeonCoins = pixeonCoins;
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
