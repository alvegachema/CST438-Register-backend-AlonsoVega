package com.cst438;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import com.cst438.controller.AdminController;
import static org.junit.Assert.assertEquals;
import com.cst438.domain.StudentRepository;
import static org.mockito.BDDMockito.given;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cst438.domain.StudentDTO;
import org.junit.jupiter.api.Test;
import com.cst438.domain.Student;
import java.util.Optional;

@ContextConfiguration(classes = { AdminController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest

public class JunitTestAdmin {
	
	static final String URL = "http://localhost:8080";
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";
	public static final String TEST_STUDENT_EMAIL_UNIQUE = "test2@csumb.edu";
	public static final String TEST_STATUS = "Hold";
	public static final String TEST_STATUS_CODE_HOLD = "1";
	public static final int TEST_STATUS_CODE_NO_HOLD = 0;
	
	@MockBean
	StudentRepository studentRepository;
	
	@Autowired
	private MockMvc mvc;
	
	//The following is the first unit test to test the add student and this unit will send a POST request and it should expect a response confirming true
	@Test
	public void addStudent() throws Exception {
		
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(TEST_STATUS_CODE_NO_HOLD);
		student.setStudent_id(1);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		given(studentRepository.save(any(Student.class))).willReturn(student);
		
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.studentName = TEST_STUDENT_NAME;
		studentDTO.studentEmail = TEST_STUDENT_EMAIL_UNIQUE;
		studentDTO.statusCode = TEST_STATUS_CODE_HOLD;
		
		response = mvc.perform(
				MockMvcRequestBuilders
				.post("/addStudent")
				.content(asJsonString(studentDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals( 0  , result.id);
		
		verify(studentRepository).save(any(Student.class));
	}
	
	//The following is the second test and this will test the REST API to check if the student hold updates correctly. 
	@Test
	public void changeHold() throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(TEST_STATUS_CODE_NO_HOLD);
		student.setStudent_id(1);
		
		given(studentRepository.findById(1)).willReturn(Optional.of(student));
		given(studentRepository.save(any(Student.class))).willReturn(student);
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.id = 1;
		studentDTO.status = TEST_STATUS;
		studentDTO.statusCode = TEST_STATUS_CODE_HOLD;
		
		response = mvc.perform(
				MockMvcRequestBuilders
				.put("/hold/1")
				.content(asJsonString(studentDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals( 0  , result.id);
		
		verify(studentRepository).save(any(Student.class));
	}
	
	//The following code will change objects into json format/strings or json to object. 
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}