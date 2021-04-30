package br.com.victor.health.exams.software.controllers;

import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;
import br.com.victor.health.exams.software.services.exceptions.JsonParserError;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class HealthcareInstitutionControllerTest {

	@Autowired
	HealthcareInstitutionRepository healthcareInstitutionRepository;

	@Autowired
	MockMvc mockMvc;

	@After
	public void end() {
		healthcareInstitutionRepository.deleteAll();
	}

	@Test
	public void postHealthcareInstitution() throws Exception {

		HealthcareInstitution institution = createHealthcareInstitution("54481434000180");

		mockMvc.perform(MockMvcRequestBuilders.post("/healthcareinstitutions").content(toJson(institution))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty()).andReturn().getResponse()
				.getContentAsString();

	}

	@Test
	public void getHealthcareInstitutionById() throws Exception {

		HealthcareInstitution institution = createHealthcareInstitution("54481434000180");

		HealthcareInstitution savedInstitution = healthcareInstitutionRepository.save(institution);

		mockMvc.perform(MockMvcRequestBuilders.get("/healthcareinstitutions/" + savedInstitution.getId())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Hemocentro"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.cnpj").value("54481434000180")).andReturn().getResponse()
				.getContentAsString();

	}

	@Test
	public void getAllHealthcareInstitution() throws Exception {

		HealthcareInstitution institution1 = createHealthcareInstitution("54481434000180");
		HealthcareInstitution institution2 = createHealthcareInstitution("24634950000136");
		HealthcareInstitution institution3 = createHealthcareInstitution("50470403000172");

		healthcareInstitutionRepository.save(institution1);
		healthcareInstitutionRepository.save(institution2);
		healthcareInstitutionRepository.save(institution3);

		mockMvc.perform(MockMvcRequestBuilders.get("/healthcareinstitutions").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Hemocentro")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].cnpj", is("54481434000180")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("Hemocentro")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].cnpj", is("24634950000136")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].name", is("Hemocentro")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].cnpj", is("50470403000172"))).andReturn().getResponse()
				.getContentAsString();

	}

	@Test
	public void healthcareInstitutionIdNotFound() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/healthcareinstitutions/9999").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.msg")
						.value("Healthcare Institution not found! Id: 9999, Type: "
								+ HealthcareInstitution.class.getSimpleName()))
				.andReturn().getResponse().getContentAsString();

	}

	@Test
	public void healthcareInstitutionAlreadySaved() throws Exception {

		HealthcareInstitution institution = createHealthcareInstitution("54481434000180");

		mockMvc.perform(MockMvcRequestBuilders.post("/healthcareinstitutions").content(toJson(institution))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty()).andReturn().getResponse()
				.getContentAsString();

		mockMvc.perform(MockMvcRequestBuilders.post("/healthcareinstitutions").content(toJson(institution))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.msg")
						.value("Healthcare Institution with this CNPJ already exists! CNPJ: " + institution.getCnpj()))
				.andReturn().getResponse().getContentAsString();

	}

	@Test
	public void validationWhenNullFields() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/healthcareinstitutions").content(toJson(new Object()))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Erro de Validação"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors",
						Matchers.containsInAnyOrder("Institution CNPJ is required.", "Institution Name is required.")))
				.andReturn().getResponse().getContentAsString();

	}

	@Test
	public void invalidCpfWhenTryingToSaveHealthcareInstitution() throws Exception {
	
		HealthcareInstitution institution = createHealthcareInstitution("111111");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/healthcareinstitutions").content(toJson(institution))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Erro de Validação"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.containsInAnyOrder(
						"Invalid CNPJ!")))
				.andReturn().getResponse()
				.getContentAsString();
		
	}

	public HealthcareInstitution createHealthcareInstitution(String cnpj) {

		HealthcareInstitution institution = new HealthcareInstitution(null, "Hemocentro", cnpj);

		return institution;

	}

	public String toJson(Object object) {

		Gson gson = new Gson();

		try {

			String json = gson.toJson(object);
			return json;

		} catch (JsonParseException e) {

			throw new JsonParserError("Error during Json parse");

		}
	}

}
