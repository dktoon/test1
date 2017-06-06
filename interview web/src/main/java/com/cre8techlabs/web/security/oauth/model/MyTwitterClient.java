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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.pac4j.core.client.RedirectAction;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.client.TwitterClient;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;

import com.cre8techlabs.entity.user.auth.OAuth;


public class MyTwitterClient extends TwitterClient implements CustomOAuthExt {

    public MyTwitterClient(String fbKey, String fbSecret) {
	    super(fbKey, fbSecret);
	    setName(OAuth.Twitter.toString());
    }
	@Override
	public String getState(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Token requestToken = (Token) session.getAttribute(getRequestTokenSessionAttributeName());
		Object[] tokenAndState = (Object[]) session.getAttribute(getRequestTokenSessionAttributeName() + "#state");
		if (requestToken.equals(tokenAndState[0])) {
			String pstate = (String) tokenAndState[1];
			return pstate;
			
		}
		throw new IllegalStateException("Token is not defined");
	}
    @Override
    protected void internalInit() {
        super.internalInit();
        DefaultApi10a api;
        if (this.isAlwaysConfirmAuthorization() == false) {
            api = new MyTwitterApi.Authenticate();
        } else {
            api = new MyTwitterApi();
        }
        this.service = new MyProxyOAuth10aServiceImpl((MyTwitterApi) api, new OAuthConfig(this.key, this.secret, this.callbackUrl,
                                                                         SignatureType.Header, null, null),
                                                    this.connectTimeout, this.readTimeout, this.proxyHost,
                                                    this.proxyPort);
    }

	@Override
	public String buildState(String state) {
//		return "{\"type\":\"" + state + "\"}";
		return state;
	}
	@Override
	public boolean requiresStateParameter() {
		return false;
	}
    public String retrieveAuthorizationUrl(final WebContext context, String state) {
        final Token requestToken = ((MyProxyOAuth10aServiceImpl) this.service).getRequestToken(state);
        logger.debug("requestToken : {}", requestToken);
        // save requestToken in user session
        context.setSessionAttribute(getRequestTokenSessionAttributeName(), requestToken);
        context.setSessionAttribute(getRequestTokenSessionAttributeName() + "#state", new Object[] {requestToken, state});
        final String authorizationUrl = ((MyProxyOAuth10aServiceImpl) this.service).getAuthorizationUrl(requestToken);
        logger.debug("authorizationUrl : {}", authorizationUrl);
        return authorizationUrl;
    }
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
            String intermediateUrl = CommonHelper.addParameter(getContextualCallbackUrl(context), NEEDS_CLIENT_REDIRECTION_PARAMETER, "true");
            String randomState = getStateParameter(context);
//            String jsonState = "{\"chunck\": \"" +randomState + "\", \"state\": " + state + "}";
            intermediateUrl = CommonHelper.addParameter(intermediateUrl, "state", state);
            return RedirectAction.redirect(intermediateUrl);
        }
    }
    @Override
    protected String getStateParameter(WebContext webContext) {
        return RandomStringUtils.randomAlphanumeric(10);
    }
    public RedirectAction retrieveRedirectAction(final WebContext context, String state) {
        try {
            return RedirectAction.redirect(retrieveAuthorizationUrl(context, state));
        } catch (final OAuthException e) {
            throw new TechnicalException(e);
        }
    }
}
