package live.learnjava.usermanagement.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import live.learnjava.usermanagement.utils.UserManagementMailService;

public class BootMailTestRunner implements CommandLineRunner {

	@Autowired
	private UserManagementMailService service;

	@Override
	public void run(String... args) throws Exception {
		
		String body = "This is the email sent from Spring Boot Mail service.";
		String subject = "Subject: Check this out...";
		String filePath = "c://install.log";
		service.sendEmailWithAttachment("gopesh.sharma+sbmail@gmail.com", body, subject, filePath);

	}

}
