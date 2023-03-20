package com.cst438.controller;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;


/*
	2a. Registration app:  create REST apis to add or change a student record.
		The story is :   As an administrator, I can add a student to the system.  I input the student email and name.  The student email must not already exists in the system.
		As an administrator, I can put student registration on HOLD.
		As an administrator, I can release the HOLD on student registration.
*/

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class AdminController {
	@Autowired
	StudentRepository studentRepository;
	@PostMapping("/addStudent")
	@Transactional
	/*
	public StudentDTO addStudent (@RequestBody StudentDTO studentDTO) {
		//the following check if the student already on file
	    Student tempstudent = studentRepository.findByEmail(studentDTO.getStudentEmail());
	    
	    //if not student is found then one will be created
	    if(tempstudent == null && studentDTO.getStudentName() != null && studentDTO.getStudentEmail() != null) {
	        Student student = new Student();
	        student.setName(studentDTO.getStudentName());
	        student.setEmail(studentDTO.getStudentEmail());
	        student.setStatus(studentDTO.getStatus());
	        student.setStatusCode(Integer.parseInt(studentDTO.getStatusCode()));
	        //This will save the new student
	        Student savedStudent = studentRepository.save(student);
	        
	        StudentDTO result = createStudentDTO(savedStudent);
	        return result;
	    } else {
	    	//This throws an exception
	        throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid student email address.  ");
	    }
	}
	*/
	public StudentDTO addStudent( @RequestBody StudentDTO studentDTO) {
			
				
			Student tempstudent = new Student();
			tempstudent.setName(studentDTO.studentName);
			tempstudent.setEmail(studentDTO.studentEmail);
			tempstudent.setStatusCode(studentDTO.statusCode);
			
			Student check = studentRepository.findByEmail(studentDTO.studentEmail);
			
			if(check == null) {
				Student savedStudent = studentRepository.save(tempstudent);
				
				StudentDTO result = createStudentDTO(savedStudent);
				return result;	
			} else {
				//This throws an exception
		        throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid student email address.  ");
				}
			} 

	//This code take s an object and it will use PUt to update the hold of the student
	@PutMapping("/hold/{student_id}")
	public StudentDTO changeHold(@RequestBody StudentDTO studentDTO, @PathVariable("student_id") int student_id) {
	    Student student = studentRepository.findById(student_id).orElse(null);
	    
	    if (student != null) {
	        student.setStatus(studentDTO.getStatus());  
	        //student.setStatusCode(studentDTO.getStatus());
	        //student.setStatusCode(Integer.parseInt(studentDTO.getStatusCode()));
	        student.setStatusCode(1);
	        Student savedStudent = studentRepository.save(student);
	        
	        StudentDTO result = createStudentDTO(savedStudent);
	        return result;
	    } else {
	    	//This throws an exception
	        throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid student/email address.  ");
	    }
	}
	
	//This code take s an object and it will use PUt to update the hold of the student
	@PutMapping("/hold/release/{student_id}")
	public StudentDTO releaseHold(@RequestBody StudentDTO studentDTO, @PathVariable("student_id") int student_id) {
	    Student student = studentRepository.findById(student_id).orElse(null);
	    
	    if (student != null) {
	        student.setStatus(studentDTO.getStatus());  
	        //student.setStatusCode(studentDTO.getStatus());
	        //student.setStatusCode(Integer.parseInt(studentDTO.getStatusCode()));
	        student.setStatusCode(0);
	        Student savedStudent = studentRepository.save(student);
	        
	        StudentDTO result = createStudentDTO(savedStudent);
	        return result;
	    } else {
	    	//This throws an exception
	        throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid student/email address.  ");
	    }
	}

	//This code creates the DTO of the student
	private StudentDTO createStudentDTO(Student student) {
	    StudentDTO studentDTO = new StudentDTO();
	    studentDTO.setId(student.getStudent_id());
	    studentDTO.setStudentName(student.getName());
	    studentDTO.setStudentEmail(student.getEmail());
	    studentDTO.setStatus(student.getStatus());
	    //studentDTO.setStatusCode(student.getStatus());
	    studentDTO.setStatus(Integer.toString(student.getStatusCode()));
	    return studentDTO;
	}
	
}