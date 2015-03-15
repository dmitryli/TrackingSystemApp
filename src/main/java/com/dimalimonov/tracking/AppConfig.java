package com.dimalimonov.tracking;

import java.net.UnknownHostException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableScheduling
public class AppConfig {

	@Value("${mail.host}")
	private String mailHost = null;

	@Value("${mail.port}")
	private String mailPort = null;

	@Value("${mail.username}")
	private String mailUsername = null;

	@Value("${mail.password}")
	private String mailPassword = null;

	@Value("${mail.transport.protocol}")
	private String mailProtocol = null;

	@Value("${mail.smtp.auth}")
	private String mailAuthorization = null;

	@Value("${mail.smtp.starttls.enable}")
	private String mailStarttls = null;

	@Value("${mail.debug}")
	private String mailDebug = null;

	@Value("${mongo.databaseName}")
	private String mongoDatabase = null;

	@Bean(name = "mailSender")
	public JavaMailSender getEmailSender() {
		JavaMailSenderImpl jm = new JavaMailSenderImpl();
		jm.setHost(mailHost);
		jm.setPort(Integer.valueOf(mailPort));
		jm.setUsername(mailUsername);
		jm.setPassword(mailPassword);

		Properties javaMailProperties = new Properties();
		javaMailProperties.setProperty("mail.transport.protocol", mailProtocol);
		javaMailProperties.setProperty("mail.smtp.auth", mailAuthorization);

		javaMailProperties.setProperty("mail.smtp.starttls.enable", mailStarttls);
		javaMailProperties.setProperty("mail.debug", mailDebug);

		jm.setJavaMailProperties(javaMailProperties);

		return jm;
	}

	@Bean(name = "mongoTemplate")
	public MongoTemplate getMongoTemplate() {
		MongoTemplate template = null;
		try {
			Mongo mongo = new MongoClient();
			template = new MongoTemplate(mongo, mongoDatabase);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return template;
	}
}
