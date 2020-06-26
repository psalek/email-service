package edu.miu.cs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mail.SimpleMailMessage;

import edu.miu.cs.service.EmailService;

@SpringBootApplication
public class Application {
	
	private static final String LIST_PATH = "./src/main/resources/seasons/seasons.csv";
	
	private static final String ATTACHMENTS_PATH = "./src/main/resources/seasons/Seasons";
	
	private static EmailService emailService;
	
	public static void main(String[] args) throws FileNotFoundException {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		emailService = context.getBean(EmailService.class);
		
		sendEmails();
		
		context.close();
	}
	
	private static void sendEmails() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(LIST_PATH));
		
		while(scanner.hasNext()) {
			try {
				SimpleMailMessage emailMessage = new SimpleMailMessage();
				 String attachmentPath = buildEmail(scanner.nextLine(), emailMessage);
				emailService.sendEmailWithAttachment(emailMessage, attachmentPath);
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	private static String buildEmail(String line, SimpleMailMessage email) {
		String[] tokens = line.split(",");
		
		String id = tokens[0];
		String name = tokens[1];
		String emailAddress = tokens[2];
		
		String[] to = new String[1];
		to[0] = emailAddress;
				
		email.setTo(to);
		email.setCc(new String[0]); // empty list
		email.setBcc(new String[0]); // empty list
		
		email.setSubject("Email Message Subject Line"); // to be replaced by the real subject line
		
		StringBuilder sb = new StringBuilder();
		sb.append("Dear ");
		sb.append(name);
		sb.append(",\r\n\r\n");
		sb.append("You are receiving this email because ...\r\n\r\n");
		sb.append("Attached to this email, is the digital photo we...\r\n\r\n");
		sb.append("Thanks\r\nSeasons Committee.\r\n\r\n");
		
		email.setText(sb.toString());
		
		return ATTACHMENTS_PATH + id + ".jpg";
	}

}
