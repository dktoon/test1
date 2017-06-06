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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cre8techlabs.entity.user.UserRole;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestSecurity {
	public static final String LIST = "listJson";
	public static final String GET = "getJson";
	public static final String POST = "update";
	public static final String PUT = "create";
	public static final String DELETE = "deleteJson";
	
	String listMethod() default LIST;
	String getMethod() default GET;
	String postMethod() default POST;
	String putMethod() default PUT;
	String deleteMethod() default DELETE;

	String otherMethod() default "";
	
	/***
	 * 
	 * @return the authorized roles
	 */
	UserRole[] list() default {};
	UserRole[] get() default {};
	UserRole[] post() default {};
	UserRole[] put() default {};
	UserRole[] delete() default {};
	
	UserRole[] other() default {};
	
	String listVoter() default "";
	String getVoter() default "";
	String postVoter() default "";
	String putVoter() default "";
	String deleteVoter() default "";
}
