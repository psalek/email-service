package edu.miu.cs.service;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${email-service.from-email}")
	private String from;

	@Override
	public void sendEmail(SimpleMailMessage email) {
		email.setFrom(from);
		email.setSentDate(new Date());

		javaMailSender.send(email);
	}

	public void sendEmailWithAttachment(SimpleMailMessage email, String attachmentPath) {

		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(from);
			helper.setTo(email.getTo());
			helper.setSubject(email.getSubject());
			helper.setText(email.getText());

			FileSystemResource file = new FileSystemResource(attachmentPath);
			
			helper.addAttachment(file.getFilename(), file);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		
		javaMailSender.send(message);
	}

}
