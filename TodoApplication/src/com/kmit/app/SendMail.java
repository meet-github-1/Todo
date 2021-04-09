package com.kmit.app;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	
	public static void send(String email,String msg){
	
		// create an instance of Properties Class
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		
		// Password Authentication
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("man.sdproject@gmail.com", "man0201@fs");
			}
		});
		
		try {
		
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("man.sdproject@gmail.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject("User Credentials: TodoApplication ");
			message.setText(msg);

			//Transport the mail
			Transport.send(message);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}

