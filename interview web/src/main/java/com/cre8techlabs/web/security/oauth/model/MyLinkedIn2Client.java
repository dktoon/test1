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
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.client.LinkedIn2Client;
import org.scribe.exceptions.OAuthException;
import org.scribe.oauth.StateOAuth20Service;

import com.cre8techlabs.entity.user.auth.OAuth;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyLinkedIn2Client extends LinkedIn2Client implements CustomOAuthExt {


	
    public MyLinkedIn2Client(ObjectMapper mapper, String fbKey, String fbSecret) {
	    super(fbKey, fbSecret);
	    setName(OAuth.LinkedIn.toString());
	    this.mapper = mapper;
    }
	@Override
	public String getState(HttpServletRequest request) throws Exception {
		String pstate = request.getParameter("state");
		StateParam state = mapper.readValue(pstate, StateParam.class);
		return state.getState().getType();
	}
    private ObjectMapper mapper;
	public RedirectAction getRedirectAction(final WebContext context, final boolean requiresAuthentication,
            final boolean ajaxRequest, String state) throws RequiresHttpAction {
        init();
        // it's an AJAX request -> unauthorized (instead of a redirection)
        if (ajaxRequest) {
            throw RequiresHttpAction.unauthorized("AJAX request -> 401", context, null);
        }
        // authentication has already been tried
        final String attemptedAuth = (String) context.getSessionAttribute(getName() + ATTEMPTED_AUTHENTICATION_SUFFIX);
        if (CommonHelper.isNotBlank(attemptedAuth)) {
            context.setSessionAttribute(getName() + ATTEMPTED_AUTHENTICATION_SUFFIX, null);
            // protected target -> forbidden
            if (requiresAuthentication) {
                logger.error("authentication already tried and protected target -> forbidden");
                throw RequiresHttpAction.forbidden("authentication already tried -> forbidden", context);
            }
        }
        // it's a direct redirection or force the redirection -> return the real redirection
        if (isDirectRedirection() || requiresAuthentication) {
            return retrieveRedirectAction(context, state);
        } else {
            // return an intermediate url which is the callback url with a specific parameter requiring redirection
            final String intermediateUrl = CommonHelper.addParameter(getContextualCallbackUrl(context),
                    NEEDS_CLIENT_REDIRECTION_PARAMETER, "true");
            return RedirectAction.redirect(intermediateUrl);
        }
    }
    public RedirectAction retrieveRedirectAction(final WebContext context, String state) {
        try {
            return RedirectAction.redirect(retrieveAuthorizationUrl(context, state));
        } catch (final OAuthException e) {
            throw new TechnicalException(e);
        }
    }


    public String retrieveAuthorizationUrl(final WebContext context, String state) {
        // no request token for OAuth 2.0 -> no need to save it in the context
        final String authorizationUrl;
        // if a state parameter is required
        if (requiresStateParameter()) {
            String randomState = getStateParameter(context);
            logger.debug("Random state parameter: {}", randomState);
            String jsonState = "{\"chunck\": \"" +randomState + "\", \"state\": " + state + "}";
            context.setSessionAttribute(getName() + STATE_PARAMETER2, jsonState);
            authorizationUrl = ((StateOAuth20Service) this.service).getAuthorizationUrl(jsonState);
        } else {
            authorizationUrl = this.service.getAuthorizationUrl(null);
        }
        logger.debug("authorizationUrl : {}", authorizationUrl);
        return authorizationUrl;
    }

	@Override
	public String buildState(String state) {
		return "{\"type\":\"" + state + "\"}";
	}
	@Override
	public boolean requiresStateParameter() {
		// TODO Auto-generated method stub
		return super.requiresStateParameter();
	}
}
