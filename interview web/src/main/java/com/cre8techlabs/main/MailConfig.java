/*************************************************************************
 * 
 * Cre8Tech Labs CONFIDENTIAL
 * __________________
 * 
 *  [2015] - [2015] Cre8Tech Labs 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Cre8Tech Labs and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Cre8Tech Labs
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Cre8Tech Labs.
 */
package com.cre8techlabs.main;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource(value = "classpath:mail.properties")
public class MailConfig {

	@Value("${email.from}")
	private String emailFrom;
	
	@Value("${email.salesTeam}")
	private String salesTeamEmail;
	
	@Value("${mail.smtp.user}")
	private String username;
	
	@Value("${mail.smtp.password}")
	private String password;
	
	@Value("${mail.transport.protocol}")
	private String protocol;
	
	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.port}")
	private String port;
	
	@Value("${mail.smtp.auth}")
	private String auth;
	
	@Value("${mail.smtp.starttls.enable}")
	private String ttlsEnable;
	
	@Value("${mail.smtp.starttls.required}")
	private String ttlsRequired;
	
	@Value("${mail.smtp.socketFactory.port}")
	private String socketFactoryPort;
	
	@Value("${mail.smtp.socketFactory.class}")
	private String socketFactoryClass;
	
	@Value("${mail.smtp.socketFactory.fallback}")
	private String fallback;
	
	@Value("${mail.debug}")
	private String debug;
	
	@Value("${mailEnv}")
	private String mailEnv;
	
	@Bean(name="sales_team_email")
	public String salesTeamEmail() {
		return emailFrom;
	}
	
	@Bean(name="support_email_from")
	public String supporEmailFrom() {
		return emailFrom;
	}
	
	@Bean(name="mailEnv")
	public String mailEnv() {
		return mailEnv;
	}
	
	@Bean
	public MailSender javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		
		javaMailSender.setHost(host);
		javaMailSender.setPort(Integer.parseInt(port));
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);
		Properties prop = new Properties();
		
		
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", port);
		prop.put("mail.transport.protocol", protocol);
		prop.put("mail.smtp.starttls.enable", ttlsEnable);
		prop.put("mail.smtp.starttls.required", ttlsRequired);
		prop.put("mail.smtp.auth", auth);
		prop.put("mail.debug", debug);
		
		javaMailSender.setJavaMailProperties(prop);
		
		return javaMailSender;
	}
}
