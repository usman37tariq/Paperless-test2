package com.engro.paperlessbackend.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.engro.paperlessbackend.dto.EmailDto;
import com.engro.paperlessbackend.utils.Constants;

@Service
public class EmailService {

	private static Logger logger = LoggerFactory.getLogger(EmailService.class);

	public JavaMailSender javaMailSender;

	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public boolean sendMail(EmailDto emailDto) {
		try {
			MimeMessage mailMessage = javaMailSender.createMimeMessage();
			mailMessage.setSubject(emailDto.getEmailSubject());

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true);
			mimeMessageHelper.setFrom(Constants.SENDER_EMAIL_ADDRESS);
			mimeMessageHelper.setTo(new String[] { emailDto.getEmailRecipient() });
			mimeMessageHelper.setText(emailDto.getEmailBody(), true);
			javaMailSender.send(mailMessage);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
			return false;
		}
		return true;
	}
}
