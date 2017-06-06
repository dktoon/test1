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

import javax.inject.Named;
import javax.servlet.Filter;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.IncludeRelativePath;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import com.cre8techlabs.entity.CascadingMongoEventListener;
import com.cre8techlabs.entity.eventListener.EntityModEventListener;
import com.cre8techlabs.entity.eventListener.UserPasswordListener;
import com.cre8techlabs.main.security.SecurityConfig;
import com.cre8techlabs.web.filter.NoCacheFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetj.servlet.filter.compression.CompressingFilter;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "com.cre8techlabs" })
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public EntityModEventListener entityModEventListener() {
		return new EntityModEventListener();
	}
	
	@Bean
	public CascadingMongoEventListener cascadingMongoEventListener() {
		return new CascadingMongoEventListener();
	}
	
	@Bean
	public UserPasswordListener userPasswordEventListener() {
		return new UserPasswordListener();
	}
	
	@Autowired
	ObjectMapper mapper;
	
	@Bean
	public VelocityConfigurer velocityConfigurer() {
		VelocityConfigurer res = new VelocityConfigurer();
		res.setResourceLoaderPath("/WEB-INF/view/");
		return res;
	}
	
	@Bean
	public ViewResolver viewResolver() {
	    VelocityViewResolver resolver = new VelocityViewResolver();
	    resolver.setViewClass(VelocityToolboxView.class);
	    resolver.setSuffix(".vm");
	    return resolver;
	}
	
	@Bean
	public VelocityEngine velocityEngine() {
		VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
		Properties velocityProperties = new Properties();
		velocityProperties.setProperty(RuntimeConstants.EVENTHANDLER_INCLUDE, IncludeRelativePath.class.getName());
		velocityProperties.put("resource.loader", "class");
		velocityProperties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		
		velocityEngineFactoryBean.setVelocityProperties(velocityProperties);
		
		VelocityEngine ve = new VelocityEngine(velocityProperties);
		
		return ve;
	}



	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations(
			        "classpath:/META-INF/resources/webjars/");
		}
		if (!registry.hasMappingForPattern("/**")) {
			registry.addResourceHandler("/**").addResourceLocations("/",
			        "/css/**", "/js/**", "/ang-app/**");
		}
	}

	@Autowired
	@Named("nocache")
	public String nocache;
	
	@Bean
	public Filter noCacheFilter() {
		return new NoCacheFilter(nocache.equals("true"));
	}
	
	@Bean
	public CorsFilter corsFilter() {
		CorsFilter filter = new CorsFilter();
		return filter;
	}
	 @Bean
	 public Filter compressingFilter() {
		 
	     CompressingFilter compressingFilter = new CompressingFilter();
	     return compressingFilter;
	 }
	@Autowired
	private SecurityConfig securityConfig;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(securityConfig.handlerSecurityInterceptor());
	}
	
	@Bean(name="jspUrlBasedViewResolver")
	public UrlBasedViewResolver urlBasedViewResolver() {
		UrlBasedViewResolver view = new UrlBasedViewResolver();
		
		view.setViewClass(JstlView.class);
		view.setPrefix("/WEB-INF/jsp/");
		view.setSuffix(".jsp");
		
		return view;
	}
	

}
