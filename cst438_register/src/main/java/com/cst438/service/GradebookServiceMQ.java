package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.CourseDTOG.GradeDTO;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		
		//This code will use DTO objects and will be sent to gradebook.
		EnrollmentDTO enrollmentDTO = new EnrollmentDTO(student_email, student_name, course_id);
		rabbitTemplate.convertAndSend(gradebookQueue.getName(), enrollmentDTO);
		
	}
	
	@RabbitListener(queues = "registration-queue")
	@Transactional
	public void receive(CourseDTOG courseDTOG) {
		
		//This code will update grades for all students in the course
		int course_id = courseDTOG.course_id;
		for (GradeDTO gradeDTO: courseDTOG.grades)
		{
			Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(gradeDTO.student_email, course_id);
			enrollment.setCourseGrade(gradeDTO.grade);
			enrollmentRepository.save(enrollment);
		}
	}
	
}