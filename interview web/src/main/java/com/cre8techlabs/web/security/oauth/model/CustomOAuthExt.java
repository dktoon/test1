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
package com.cre8techlabs.web.security.oauth.model;

import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.client.RedirectAction;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.RequiresHttpAction;


public interface CustomOAuthExt {
    String STATE_PARAMETER2 = "#oauth20StateParameter";
    
	public RedirectAction getRedirectAction(final WebContext context, final boolean requiresAuthentication, final boolean ajaxRequest, String state) throws RequiresHttpAction;
	public String buildState(String state);
	public String getState(HttpServletRequest request) throws Exception;
	boolean requiresStateParameter();
}
