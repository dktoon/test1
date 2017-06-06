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

import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.ProxyOAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

public class MyProxyOAuth20ServiceImpl extends OAuth20ServiceImpl {
    
    protected final MyGoogleApi20 api;
    protected final OAuthConfig config;
    protected final int connectTimeout;
    protected final int readTimeout;
    protected final String proxyHost;
    protected final int proxyPort;
    protected final boolean getParameter;
    protected final boolean addGrantType;
    
    public MyProxyOAuth20ServiceImpl(final MyGoogleApi20 api, final OAuthConfig config, final int connectTimeout,
                                   final int readTimeout, final String proxyHost, final int proxyPort) {
        this(api, config, connectTimeout, readTimeout, proxyHost, proxyPort, true, false);
    }
    
    public MyProxyOAuth20ServiceImpl(final MyGoogleApi20 api, final OAuthConfig config, final int connectTimeout,
                                   final int readTimeout, final String proxyHost, final int proxyPort,
                                   final boolean getParameter, final boolean addGrantType) {
        super(api, config);
        this.api = api;
        this.config = config;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.getParameter = getParameter;
        this.addGrantType = addGrantType;
    }
    
    @Override
    public Token getAccessToken(final Token requestToken, final Verifier verifier) {
        final OAuthRequest request = new ProxyOAuthRequest(this.api.getAccessTokenVerb(),
                                                           this.api.getAccessTokenEndpoint(), this.connectTimeout,
                                                           this.readTimeout, this.proxyHost, this.proxyPort);
        if (this.getParameter) {
            request.addQuerystringParameter(OAuthConstants.CLIENT_ID, this.config.getApiKey());
            request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, this.config.getApiSecret());
            request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
            request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, this.config.getCallback());
            if (this.config.hasScope()) {
                request.addQuerystringParameter(OAuthConstants.SCOPE, this.config.getScope());
            }
            if (this.addGrantType) {
                request.addQuerystringParameter("grant_type", "authorization_code");
            }
        } else {
            request.addBodyParameter(OAuthConstants.CLIENT_ID, this.config.getApiKey());
            request.addBodyParameter(OAuthConstants.CLIENT_SECRET, this.config.getApiSecret());
            request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
            request.addBodyParameter(OAuthConstants.REDIRECT_URI, this.config.getCallback());
            if (this.config.hasScope()) {
                request.addBodyParameter(OAuthConstants.SCOPE, this.config.getScope());
            }
            if (this.addGrantType) {
                request.addBodyParameter("grant_type", "authorization_code");
            }
        }
        final Response response = request.send();
        return this.api.getAccessTokenExtractor().extract(response.getBody());
    }

	public String getAuthorizationUrl(String jsonState) {
		return api.getAuthorizationUrl(config, jsonState);
	}
	

}