package com.cst438;



import org.openqa.selenium.By;
import com.cst438.domain.Student;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.EnrollmentRepository;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;


//The following code completes "(registration service) as an Admin I can add a new student to the registration service"

@SpringBootTest
public class EndToEndAddStudentTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver.exe";

	public static final String URL = "http://localhost:3000/addStudent";
	
	public static final String TEST_STUDENT_NAME = "Test2";

	public static final String TEST_STUDENT_EMAIL = "test2@csumb.edu";

	public static final int TEST_STATUS_CODE = 0;

	public static final int SLEEP_DURATION = 1000; // 1 second.

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Test
	public void addStudentTest() throws Exception {
		
		//The following statement checks if test(#) student exists
		Student s = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
		if (s != null)
		{
			//Then it will remove enrollments for student and test student from the repository
			enrollmentRepository.deleteAllByStudentId(s.getStudent_id());
			studentRepository.delete(s);
		}

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		
		// It will wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			//The following code defines the test attributes: student name, email and status code
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_STUDENT_NAME);
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_STUDENT_EMAIL);		
			driver.findElement(By.xpath("//input[@name='status_code']")).sendKeys("value", Integer.toString(TEST_STATUS_CODE));
			//waits
			Thread.sleep(SLEEP_DURATION);
			//then it looks for  to add 
			driver.findElement(By.xpath("//button")).click();
			Thread.sleep(SLEEP_DURATION);
			Thread.sleep(SLEEP_DURATION);

			//this checks if student was added to database
			Student student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			assertNotNull(student, "Added student not found in database");

		} catch (Exception ex) {
			throw ex;
		} 
		//The following code will remove and clean database after tests
		finally {

			Student student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (student != null)
				studentRepository.delete(student);

			driver.quit();
		}

	}
}