package vn.toancauxanh.service;

import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.zkoss.util.resource.Labels;

public class SendEmail extends BasicService<Object> {

	public static void sendEmailGmail(String mailTo, String subject, String content) {
		final String username = Labels.getLabel("conf.mail.email");
		final String password = Labels.getLabel("conf.mail.password");
		Properties props = new Properties();
		props.put("mail.smtp.auth", Labels.getLabel("conf.mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", Labels.getLabel("conf.mail.smtp.starttls.enable"));
		props.put("mail.smtp.host", Labels.getLabel("conf.mail.smtp.host"));
		props.put("mail.smtp.port", Labels.getLabel("conf.mail.smtp.port"));

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
			message.setSubject(subject);
			// message.setText("Dear Mail Crawler," + "\n\n No spam to my email,
			// please!");
			message.setContent(MessageFormat.format(content, "TT DỊCH VỤ CÔNG"), "text/html; charset=utf-8");
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
