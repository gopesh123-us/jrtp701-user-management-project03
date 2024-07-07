package live.learnjava.usermanagement.utils;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import live.learnjava.usermanagement.config.ApplicationConfigProperties;
import live.learnjava.usermanagement.contants.UserManagementConstants;

@Service("appMailService")
public class UserManagementMailService {

	Logger logger = LoggerFactory.getLogger(UserManagementMailService.class);

	@Autowired
	JavaMailSender sender;

	@Autowired
	ApplicationConfigProperties properties;

	@Autowired
	Configuration config;

	public void sendSimpleEmail(String toEmail, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(properties.getMail().get(UserManagementConstants.FROM_EMAIL.toString()));
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		sender.send(message);

		logger.info("Email sent without attachment...");
	}

	public void sendEmail(String toEmail, String subject, Map<String, Object> placeHoldersMap, Template template) throws Exception {
		
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, placeHoldersMap);
		
		MimeMessage mimemsg = sender.createMimeMessage();
		MimeMessageHelper mimeMsgHelper = new MimeMessageHelper(mimemsg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
		mimeMsgHelper.setFrom(properties.getMail().get(UserManagementConstants.FROM_EMAIL.toString()));
		mimeMsgHelper.setTo(toEmail);
		mimeMsgHelper.setText(html, true);
		mimeMsgHelper.setSubject(subject);	
		
		sender.send(mimemsg);

		logger.info("Email sent without attachment...");
	}

	public void sendEmailWithAttachment(String toEmail, String body, String subject, String fileName)
			throws MessagingException {
		MimeMessage mimemsg = sender.createMimeMessage();

		MimeMessageHelper mimeMsgHelper = new MimeMessageHelper(mimemsg, true);
		mimeMsgHelper.setFrom(properties.getMail().get(UserManagementConstants.FROM_EMAIL.toString()));
		mimeMsgHelper.setTo(toEmail);
		mimeMsgHelper.setText(body);
		mimeMsgHelper.setSubject(subject);

		FileSystemResource fileSystemResource = new FileSystemResource(new File(fileName));
		mimeMsgHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
		sender.send(mimemsg);

		logger.info("Email sent with attachment to: " + toEmail);
	}

}
