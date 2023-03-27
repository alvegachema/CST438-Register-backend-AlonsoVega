package com.cst438.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import com.cst438.domain.EnrollmentDTO;

public class GradebookServiceREST extends GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	String gradebook_url;
	
	public GradebookServiceREST() {
		System.out.println("REST grade book service");
	}

	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		
		
		//The following line of code will initialize parameters of enrollmentdto,
		//It will also send a POST to gradebook enroll DTO
		EnrollmentDTO enrollmentDTO = new EnrollmentDTO(student_email, student_name, course_id);
		
		//The following line of code will do a post dto object to the other service; gradebook
		restTemplate.postForEntity(gradebook_url + "/enrollment", enrollmentDTO, EnrollmentDTO.class);
		
		//The following code can also be used in case there is any problems with gradebook backend
		
		//EnrollmentDTO enrollment = new EnrollmentDTO();
		//enrollment.course_id = course_id;
		//enrollment.studentEmail = student_email;
		//enrollment.studentName = student_name;
		//System.out.println("Post to gradebook "+enrollment);
		//EnrollmentDTO response = restTemplate.postForObject(gradebook_url+"/enrollment", enrollment, enrollmentDTO.class);
		//System.out.println("Response from gradebook "+response);
		
	}

}
