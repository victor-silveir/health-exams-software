package br.com.victor.health.exams.software.entities.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.com.victor.health.exams.software.entities.enums.Gender;

@Converter
public class GenderConverter implements AttributeConverter<Gender, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Gender gender) {

		if (gender == null) {
			return null;
		}
		return gender.getCod();
	}

	@Override
	public Gender convertToEntityAttribute(Integer cod) {

		if (cod == null) {
			return null;
		}
		return Gender.toEnum(cod);
	}

}
