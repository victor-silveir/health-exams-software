package br.com.victor.health.exams.software.entities.enums;

import java.util.Arrays;

public enum Gender {
	
	MALE(1, "Male"),
	FEMALE(2, "Female");
	
	private int cod;
	private String description;
	
	private Gender(int cod, String description) {
		this.cod = cod;
		this.description = description;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return description;
	}

	public static Gender toEnum(int cod ) {
		return Arrays.stream(values()).filter(gender -> gender.cod == cod).findFirst().orElseThrow(() -> new IllegalArgumentException("Code invalid" + cod));
		
}

}
