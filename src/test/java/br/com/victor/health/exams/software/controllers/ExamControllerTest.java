package br.com.victor.health.exams.software.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
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

import antlr.collections.List;
import br.com.victor.health.exams.software.dtos.ExamDto;
import br.com.victor.health.exams.software.dtos.UpdateExamDto;
import br.com.victor.health.exams.software.entities.Exam;
import br.com.victor.health.exams.software.entities.HealthcareInstitution;
import br.com.victor.health.exams.software.entities.enums.Gender;
import br.com.victor.health.exams.software.repositories.ExamRepository;
import br.com.victor.health.exams.software.repositories.HealthcareInstitutionRepository;
import br.com.victor.health.exams.software.services.ExamService;
import br.com.victor.health.exams.software.services.exceptions.JsonParserError;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ExamControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ExamRepository examRepository;

	@Autowired
	private HealthcareInstitutionRepository healthcareInstitutionRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	private HealthcareInstitution institution = new HealthcareInstitution(null, "Hemocentro de brasília",
			"29003675000176");

	private HealthcareInstitution noCoinsInstitution = new HealthcareInstitution(null, "Laboratório Maria do Socorro",
			"80575258000155");

	@Before
	public void initialize() {

		noCoinsInstitution.chargePixeonCoins(19);

		healthcareInstitutionRepository.save(institution);

		healthcareInstitutionRepository.save(noCoinsInstitution);

	}

	@Test
	public void postExam() throws Exception {

		ExamDto examDto = mockExamDto(institution.getId());

		mockMvc.perform(MockMvcRequestBuilders.post("/exams").content(toJson(examDto))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty()).andReturn().getResponse()
				.getContentAsString();
	}

	@Test
	public void getExamById() throws Exception {

		Exam exam = mockExam(institution);
	
		Exam savedExam = examRepository.save(exam);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/exams/" + savedExam.getId() + "?healthcareInstitutionId=" + institution.getId())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.patientName").value("Robertina"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.procedureName").value("Hemograma"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.physicianName").value("Juan Gonzales")).andReturn()
				.getResponse().getContentAsString();

	}

	@Test
	public void getAllExamsForHealthcareInstitution() throws Exception {

		Exam exam = mockExam(institution);
		Exam exam2 = new Exam(null, "Hemograma completo", "Maria do Carmo", 16, "José", "123456 DF", false, Gender.FEMALE);
		exam2.setHealthcareInstitution(institution);
		Exam exam3 = new Exam(null, "Antitransglutaminase", "Edgard", 26, "Camargo", "12345 DF", false, Gender.MALE);
		exam3.setHealthcareInstitution(institution);
		
		examRepository.save(exam);
		examRepository.save(exam2);
		examRepository.save(exam3);

		mockMvc.perform(MockMvcRequestBuilders.get("/exams").content(toJson(institution))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].patientName", is("Robertina")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].procedureName", is("Hemograma")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].requested", is(false)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].patientName", is("Maria do Carmo")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].procedureName", is("Hemograma completo")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].requested", is(false)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].patientName", is("Edgard")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].procedureName", is("Antitransglutaminase")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].requested", is(false)))
				.andReturn().getResponse().getContentAsString();

	}
	
	@Test
	public void updateExam() throws Exception {

		Exam exam = mockExam(institution);
		
		examRepository.save(exam);
		
		UpdateExamDto updateExamDto = modelMapper.map(exam, UpdateExamDto.class);
		
		updateExamDto.setPatientName("Maria Gonzales");
		updateExamDto.setProcedureName("Teste HPV");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/exams/" + updateExamDto.getId() + "?healthcareInstitutionId=" + institution.getId())
				.content(toJson(updateExamDto))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/exams/" + updateExamDto.getId() + "?healthcareInstitutionId=" + institution.getId())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.patientName").value("Maria Gonzales"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.procedureName").value("Teste HPV"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.physicianName").value("Juan Gonzales")).andReturn()
				.getResponse().getContentAsString();

	}
	
	@Test
	public void deleteExam() throws Exception {
		
		Exam exam = mockExam(institution);
		
		examRepository.save(exam);
		
		ExamDto examDto = modelMapper.map(exam, ExamDto.class);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/exams/" + examDto.getId() + "?healthcareInstitutionId=" + institution.getId())
				.content(toJson(examDto))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/exams/" + examDto.getId() + "?healthcareInstitutionId=" + institution.getId())
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				.andExpect(MockMvcResultMatchers.jsonPath("$.physicianName").value("Juan Gonzales")).andReturn()
				.getResponse().getContentAsString();
		
		
	}

	public ExamDto mockExamDto(int healthcareInstitutionId) {
		
		ExamDto exam = new ExamDto(null, "Victor Bruno", 24, Gender.MALE, "Roberto Carlos", "123456 DF",
				"Antitransglutaminase", healthcareInstitutionId);

		return exam;
	}
	
	public Exam mockExam(HealthcareInstitution healthcareInstitution) {

		Exam exam = new Exam(null, "Hemograma", "Robertina", 58, "Juan Gonzales", "21098 ES", false, Gender.FEMALE);
		exam.setHealthcareInstitution(healthcareInstitution);

		return exam;
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
