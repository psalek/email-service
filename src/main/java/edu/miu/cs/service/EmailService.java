package edu.miu.cs.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	
	void sendEmail(SimpleMailMessage email);
	
	void sendEmailWithAttachment(SimpleMailMessage email, String attachmentPath);

}
