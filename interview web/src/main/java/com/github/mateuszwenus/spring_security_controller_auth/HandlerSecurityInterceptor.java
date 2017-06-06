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
package com.github.mateuszwenus.spring_security_controller_auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HandlerSecurityInterceptor extends AbstractSecurityInterceptor
        implements HandlerInterceptor {

	private ThreadLocal<InterceptorStatusToken> tokenTL = new ThreadLocal<InterceptorStatusToken>();

	private HandlerInvocationSecurityMetadataSource securityMetadataSource;

	public void setSecurityMetadataSource(
	        HandlerInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
	        Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerInvocation hi = new HandlerInvocation(req, resp,
			        (HandlerMethod) handler);
			InterceptorStatusToken token = super.beforeInvocation(hi);
			tokenTL.set(token);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp,
	        Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest req,
	        HttpServletResponse resp, Object handler, Exception exc)
	        throws Exception {
		InterceptorStatusToken token = tokenTL.get();
		if (token != null) {
			tokenTL.set(null);
			super.finallyInvocation(token);
			super.afterInvocation(token, null);
		}
	}

	@Override
	public Class<?> getSecureObjectClass() {
		return HandlerInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return securityMetadataSource;
	}

	

}
