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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.web.access.expression.PublicWebExpressionConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.web.method.HandlerMethod;

import com.cre8techlabs.entity.user.UserRole;
import com.github.mateuszwenus.spring_security_controller_auth.AuthorizeRequest;
import com.github.mateuszwenus.spring_security_controller_auth.HandlerInvocation;
import com.github.mateuszwenus.spring_security_controller_auth.HandlerInvocationSecurityMetadataSource;

public class LPHandlerInvocationSecurityMetadataSource implements
        HandlerInvocationSecurityMetadataSource,
        FilterInvocationSecurityMetadataSource, MethodSecurityMetadataSource {

	private SecurityExpressionHandler<?> expressionHandler = new AppDefaultWebSecurityExpressionHandler();

	public void setExpressionHandler(
	        SecurityExpressionHandler<?> expressionHandler) {
		this.expressionHandler = expressionHandler;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
	        throws IllegalArgumentException {
		
		List<ConfigAttribute> result = new ArrayList<ConfigAttribute>();
		
		HandlerInvocation invocation = (HandlerInvocation) object;
		HandlerMethod method = invocation.getMethod();
		
		AuthorizeRequest authorizeRequestAnn = method.getMethodAnnotation(AuthorizeRequest.class);
		if (authorizeRequestAnn != null) {
			String annValue = authorizeRequestAnn.value();
			annValue += (StringUtils.isEmpty(annValue)? "": " or ") + buildHasAnyAuthority(authorizeRequestAnn.roles());
			if (!StringUtils.isEmpty(annValue)) {
				Expression expr = expressionHandler.getExpressionParser().parseExpression(annValue);
				ConfigAttribute attr = new PublicWebExpressionConfigAttribute(expr);
				result.add(attr);
				
			}
		}

//		CheckAccess checkAccessAnn = method.getMethodAnnotation(CheckAccess.class);
//		if (checkAccessAnn != null) {
//			LPMethodConfigAttribute attr = new LPMethodConfigAttribute();
//			
//			attr.setType(checkAccessAnn.type());
//			
//			
////			result.add(attr);
//		}
		return result;
	}
	private String buildHasAnyAuthority(UserRole[] roles) {
	    StringBuilder builder = new StringBuilder("hasAnyAuthority(");
	    builder.append("'" + UserRole.Admin + "'");
	    if (roles.length > 0) {
	    	builder.append(",");
	    }
	    for (int i = 0; i < roles.length; i++) {
	        
	    	builder.append("'" + roles[i] + "'");
	    	if (i < roles.length - 1)
	    		builder.append(",");
        }
	    
	    builder.append(")");
	    return builder.toString();
    }

	@Override
    public Collection<ConfigAttribute> getAttributes(Method method,
            Class<?> targetClass) {
	    // TODO Auto-generated method stub
	    return null;
    }
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return HandlerInvocation.class.isAssignableFrom(clazz);
	}
	public static String[] getParams(String annotation) {
	    Pattern p = Pattern.compile("[a-zA-Z]+.*[\\w\\s$]*(\\((.*[\\w\\s,$]*)\\))");
	    Matcher m = p.matcher(annotation);
	    if (m.find()) {
	    	for (int i = 2; i <= m.groupCount(); i++) {
	    		String[] params = m.group(i).split(", ");
	    		return params;
            }
	    }
	    return new String[0];
    }


	
}