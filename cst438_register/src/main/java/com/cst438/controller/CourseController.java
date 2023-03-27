
package com.cst438.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import com.cst438.domain.Enrollment;
import com.cst438.domain.CourseDTOG.GradeDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.CourseDTOG;

@RestController
public class CourseController {
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	/*
	 * endpoint used by gradebook service to transfer final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody CourseDTOG courseDTOG, @PathVariable("course_id") int course_id) {
		
		//The following code will change/update the grades for all students in class, it will also look for the students in class and will send a request to update the database/
		//It will receive courseDTOG and update the table with the course grades
		for (GradeDTO gradeDTO: courseDTOG.grades)
		{
			Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(gradeDTO.student_email, course_id);
			enrollment.setCourseGrade(gradeDTO.grade);
			enrollmentRepository.save(enrollment);
		}
	}

}