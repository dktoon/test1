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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.j2e.filter.ClientsConfigFilter;
import org.pac4j.j2e.filter.RequiresAuthenticationFilter;
import org.pac4j.j2e.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallbackFilter extends ClientsConfigFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(CallbackFilter.class);
    
    private String defaultUrl = "/";

	private Clients clients;
    
    public CallbackFilter(Clients clients) {
	    this.clients = clients;
    }

	@Override
    public void init(final FilterConfig filterConfig) throws ServletException {
//        super.init(filterConfig);
//        this.defaultUrl = filterConfig.getInitParameter("defaultUrl");
//        CommonHelper.assertNotBlank("defaultUrl", this.defaultUrl);
    }
    
    @Override
    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    protected void internalFilter(final HttpServletRequest request, final HttpServletResponse response,
                                  final HttpSession session, final FilterChain chain) throws IOException,
        ServletException {
        
        final WebContext context = new J2EContext(request, response);
        final Client client = clients.findClient(context);
        logger.debug("client : {}", client);
        
        final Credentials credentials;
        try {
            credentials = client.getCredentials(context);
        } catch (final RequiresHttpAction e) {
            logger.debug("extra HTTP action required : {}", e.getCode());
            return;
        }
        logger.debug("credentials : {}", credentials);
        
        // get user profile
        final CommonProfile profile = (CommonProfile) client.getUserProfile(credentials, context);
        logger.debug("profile : {}", profile);
        
        if (profile != null) {
            // only save profile when it's not null
            UserUtils.setProfile(session, profile);
        }
        
        final String requestedUrl = (String) session.getAttribute(RequiresAuthenticationFilter.ORIGINAL_REQUESTED_URL);
        logger.debug("requestedUrl : {}", requestedUrl);
        if (CommonHelper.isNotBlank(requestedUrl)) {
            response.sendRedirect(requestedUrl);
        } else {
            response.sendRedirect(this.defaultUrl);
        }
    }
}
