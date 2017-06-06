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

import java.util.Arrays;

import javax.servlet.Filter;

import org.pac4j.core.client.Clients;
import org.pac4j.springframework.security.authentication.ClientAuthenticationProvider;
import org.pac4j.springframework.security.web.ClientAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;

import com.cre8techlabs.web.security.oauth.model.MyFacebookClient;
import com.cre8techlabs.web.security.oauth.model.MyGoogleClient;
import com.cre8techlabs.web.security.oauth.model.MyLinkedIn2Client;
import com.cre8techlabs.web.security.oauth.model.MyTwitterClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration("OAuthConfiguration")
@PropertySource(value = "classpath:oauth.properties")
public class OAuthConfiguration {

	@Value("${fbKey}")
	private String fbKey;
	@Value("${fbSecret}")
	private String fbSecret;

	@Value("${twKey}")
	private String twKey;
	@Value("${twSecret}")
	private String twSecret;

	@Value("${ggKey}")
	private String ggKey;
	@Value("${ggSecret}")
	private String ggSecret;

	@Value("${inKey}")
	private String inKey;
	@Value("${inSecret}")
	private String inSecret;

	@Value("${callbackUrl}")
	private String callbackUrl;

	@Value("${callbackHost}")
	private String callbackHost;

	@Autowired
	ObjectMapper mapper;
	
	@Bean
	public MyFacebookClient facebookClient() {
		return new MyFacebookClient(mapper, fbKey, fbSecret);
	}

	@Bean
	public MyGoogleClient google2Client() {
		return new MyGoogleClient(mapper, ggKey, ggSecret);
	}

	@Bean
	public MyTwitterClient twitterClient() {
		return new MyTwitterClient(twKey, twSecret);
	}

	@Bean
	public MyLinkedIn2Client linkedIn2Client() {
		return new MyLinkedIn2Client(mapper, inKey, inSecret);
	}

	@Bean
	public org.pac4j.core.client.Clients clients() {
		Clients clients = new Clients();

		clients.setCallbackUrl(callbackHost + callbackUrl);
		clients.setClients(facebookClient(), google2Client(), twitterClient(),
		        linkedIn2Client());
			

		return clients;
	}

	@Autowired
	public AuthenticationManager authenticationManager;

	@Bean
	public ClientAuthenticationProvider clientAuthenticationProvider() {
		ClientAuthenticationProvider provider = new ClientAuthenticationProvider();
		provider.setClients(clients());
		return provider;
	}

//	@Bean
	public ClientAuthenticationFilter clientAuthenticationFilter() {
		ClientAuthenticationFilter filter = new ClientAuthenticationFilter(callbackUrl);
		filter.setClients(clients());
		filter.setAuthenticationManager(authenticationManager);
		return filter;

	}

//	@Bean
	public CallbackFilter callbackFilter() {
		return new CallbackFilter(clients());
	}
//	@Bean
	public FilterRegistrationBean callbackFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		Filter filter = callbackFilter();
		registrationBean.setFilter(filter);
		registrationBean.setOrder(0);
		registrationBean.setUrlPatterns(Arrays.asList("/callback*"));
		return registrationBean;
	}
}
