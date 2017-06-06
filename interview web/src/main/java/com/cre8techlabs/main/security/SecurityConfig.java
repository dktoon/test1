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
package com.cre8techlabs.main.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.cre8techlabs.repository.user.UserRepository;
import com.cre8techlabs.services.user.UserServices;
import com.cre8techlabs.web.security.filter.AuthenticationTokenProcessingFilter;
import com.cre8techlabs.web.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mateuszwenus.spring_security_controller_auth.HandlerSecurityInterceptor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
@PropertySource(value = "classpath:oauth.properties")
@DependsOn(value="OAuthConfiguration")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DefaultPages defaultPages;
	
	@Autowired
	protected UserServices userServices;
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	LPDaoAuthenticationProvider lpDaoAuthenticationProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	        throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	@Bean
	public AuthenticationManager authenticationManager() {
//		return new ProviderManager(Arrays.asList(authenticationProvider(), clientAuthenticationProvider));
		return new ProviderManager(Arrays.asList(authenticationProvider()));
	}
	
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		return lpDaoAuthenticationProvider;
    }
	@Override
	public void configure(WebSecurity web) throws Exception {
		
		// @formatter:off
		String[] anonymousPaths = {
								"/ang-app/**", 
								"/css/**",
								"/utils/buildVersion",
								"/utils/info",
								"/public/**/**",
								"/**/*.html",
								"/**/*.jpg",
								"/**/*.png",
								"/**/*.js*",
								"/**/*.css*",
								"/webjars/**/*",
								"/images/**/*",
								"/security/**",
								"/new/**",
								"/constant/**",
								"/services/signup/**",
								"/callback*",
								"/oauth/**",
								"/rest/share/**/*",
								"/public/**",
								"/rest/company/download/logo/*",
								"/rest/company/download/rateCss/*",
								"/rest/user/download/file/**",
								"/rest/marketing/**/**",
								"/rest/shareRatePref/download/**",
								"/rest/forgotPassword/**",
								"/ImportClassesJsAsyncBulk",
								"/generateImportCssAsyncBulk",
								"/rest/e1003/application/**",
								"/**/*.pdf",
		        				
		        				
		};
		
		web.ignoring().antMatchers(anonymousPaths);

		// @formatter:on
	}

	@Autowired
	ObjectMapper mapper;
	
//	@Autowired
//	ClientAuthenticationProvider clientAuthenticationProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
        http.csrf().disable()
        .authorizeRequests()
            .anyRequest().authenticated().and()
        .formLogin().failureUrl("/index.html#/login/log")
        .loginPage("/index.html#/login/log").and()
        .exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
			
			@Override
			public void handle(HttpServletRequest request,
			        HttpServletResponse response,
			        AccessDeniedException accessDeniedException) throws IOException,
			        ServletException {
				response.setStatus(405);
				throw accessDeniedException;
			}
			
		})
        .and()
        .httpBasic();
        
        http.headers().frameOptions().sameOrigin();
        
        JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new JsonAuthenticationSuccessHandler(mapper, userRepository, defaultPages));
        filter.setAuthenticationManager(authenticationManager());
        
        
        http.addFilter(filter);
        
        
	}

	
	@Bean
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.<AccessDecisionVoter<? extends Object>> asList(webExpressionVoter());
		AccessDecisionManager accessDecisionManager = new AffirmativeBased(decisionVoters);
		
		return accessDecisionManager;
	}
	       
	

	@Bean
	public WebExpressionVoter webExpressionVoter() {
		WebExpressionVoter voter = new WebExpressionVoter();
		voter.setExpressionHandler(webSecurityExpressionHandler());
		return voter;
	}
	@Bean
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		return new DefaultWebSecurityExpressionHandler();
	}
	
	@Bean
	public HandlerSecurityInterceptor handlerSecurityInterceptor() {

		HandlerSecurityInterceptor interceptor = new HandlerSecurityInterceptor();
		WebExpressionVoter webExpressionVoter = webExpressionVoter();
		webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
		LPVoterAdapter voter = new LPVoterAdapter();
		voter.setWebExpressionVoter(webExpressionVoter);
		List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.<AccessDecisionVoter<? extends Object>> asList(voter);
		AccessDecisionManager accessDecisionManager = new AffirmativeBased(decisionVoters);
		interceptor.setAccessDecisionManager(accessDecisionManager);

		interceptor.setSecurityMetadataSource(new LPHandlerInvocationSecurityMetadataSource());

		interceptor.setAuthenticationManager(authenticationManager());
		return interceptor;
	}
	public Filter authenticationTokenProcessingFilter() {
		return new AuthenticationTokenProcessingFilter(userServices);
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		Filter securityFilter = authenticationTokenProcessingFilter();
		registrationBean.setFilter(securityFilter);
		registrationBean.setOrder(0);
		return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean logoutFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		Filter logoutFilter = logoutFilter();
		registrationBean.setFilter(logoutFilter);
		registrationBean.setOrder(0);
		registrationBean.setUrlPatterns(Arrays.asList("/logout"));
		return registrationBean;
	}
	
	public LogoutFilter logoutFilter() {
	    SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
	    securityContextLogoutHandler.setInvalidateHttpSession(true);
	    

	    LogoutFilter logoutFilter = new LogoutFilter("/", securityContextLogoutHandler) {
	    	@Override
	    	public void doFilter(ServletRequest req, ServletResponse res,
	    	        FilterChain chain) throws IOException, ServletException {
	    		if (req instanceof HttpServletRequest) {
	    			HttpServletRequest request = (HttpServletRequest) req;
	    			if (request.getCookies() != null) {
		    			for (javax.servlet.http.Cookie cookie : request.getCookies()) {
		    	            if (cookie.equals("authToken")) {
		    	                cookie.setMaxAge(0);
		    	                ((HttpServletResponse)res).addCookie(cookie);
		    	            }
		    	        }
	    				
	    			}
	    		}
	    	    super.doFilter(req, res, chain);
	    	}
	    };
	    logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	    return logoutFilter;
	}
	
	/*****************/
	
	public MethodSecurityInterceptor methodSecurityInterceptor() {
		MethodSecurityInterceptor interceptor = new MethodSecurityInterceptor();

		WebExpressionVoter webExpressionVoter = webExpressionVoter();
		webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
		LPVoterAdapter voter = new LPVoterAdapter();
		voter.setWebExpressionVoter(webExpressionVoter);
		List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.<AccessDecisionVoter<? extends Object>> asList(voter);
		AccessDecisionManager accessDecisionManager = new AffirmativeBased(decisionVoters);
		interceptor.setAccessDecisionManager(accessDecisionManager);

		interceptor.setSecurityMetadataSource(new LPHandlerInvocationSecurityMetadataSource());

		interceptor.setAuthenticationManager(authenticationManager());
	
		
		return interceptor;
	}
	

}
