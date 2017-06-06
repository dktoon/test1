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
import java.util.Map;

import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.ProxyOAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth10aServiceImpl;
import org.scribe.utils.MapUtils;

/**
 * This class represents a specific OAuth service with proxy capabilities for OAuth 1.0 protocol. It could be part of the Scribe library.
 * 
 * @author Jerome Leleu
 * @since 1.2.0
 */
public class MyProxyOAuth10aServiceImpl extends OAuth10aServiceImpl {
    
    protected final MyTwitterApi api;
    protected final OAuthConfig config;
    protected final int connectTimeout;
    protected final int readTimeout;
    protected final String proxyHost;
    protected final int proxyPort;
    
    public MyProxyOAuth10aServiceImpl(final MyTwitterApi api, final OAuthConfig config, final int connectTimeout,
                                    final int readTimeout, final String proxyHost, final int proxyPort) {
        super(api, config);
        this.api = api;
        this.config = config;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }
    
    @Override
    public Token getRequestToken() {
        this.config.log("obtaining request token from " + this.api.getRequestTokenEndpoint());
        final OAuthRequest request = new ProxyOAuthRequest(this.api.getRequestTokenVerb(),
                                                           this.api.getRequestTokenEndpoint(), this.connectTimeout,
                                                           this.readTimeout, this.proxyHost, this.proxyPort);
        
        this.config.log("setting oauth_callback to " + this.config.getCallback());
        request.addOAuthParameter(OAuthConstants.CALLBACK, this.config.getCallback());
        addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
        appendSignature(request);
        
        this.config.log("sending request...");
        final Response response = request.send();
        final String body = response.getBody();
        
        this.config.log("response status code: " + response.getCode());
        this.config.log("response body: " + body);
        return this.api.getRequestTokenExtractor().extract(body);
    }
    
    public Token getRequestToken(String state) {
        this.config.log("obtaining request token from " + this.api.getRequestTokenEndpoint());
        final OAuthRequest request = new ProxyOAuthRequest(this.api.getRequestTokenVerb(),
                                                           this.api.getRequestTokenEndpoint(), this.connectTimeout,
                                                           this.readTimeout, this.proxyHost, this.proxyPort);
        
        this.config.log("setting oauth_callback to " + this.config.getCallback());
        request.addOAuthParameter(OAuthConstants.CALLBACK, this.config.getCallback());
        
        addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
        appendSignature(request);
        
        request.getOauthParameters().put("state", state);
        
        this.config.log("sending request...");
        final Response response = request.send();
        final String body = response.getBody();
        
        this.config.log("response status code: " + response.getCode());
        this.config.log("response body: " + body);
        Token result = this.api.getRequestTokenExtractor().extract(body);
        
        return result;
    }
    
    private void addOAuthParams(final OAuthRequest request, final Token token) {
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, this.api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter(OAuthConstants.NONCE, this.api.getTimestampService().getNonce());
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, this.config.getApiKey());
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, this.api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
        if (this.config.hasScope())
            request.addOAuthParameter(OAuthConstants.SCOPE, this.config.getScope());
        request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));
        
        this.config.log("appended additional OAuth parameters: " + MapUtils.toString(request.getOauthParameters()));
    }
    
    @Override
    public Token getAccessToken(final Token requestToken, final Verifier verifier) {
        this.config.log("obtaining access token from " + this.api.getAccessTokenEndpoint());
        final ProxyOAuthRequest request = new ProxyOAuthRequest(this.api.getAccessTokenVerb(),
                                                                this.api.getAccessTokenEndpoint(), this.connectTimeout,
                                                                this.readTimeout, this.proxyHost, this.proxyPort);
        request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
        request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());
        
        this.config.log("setting token to: " + requestToken + " and verifier to: " + verifier);
        addOAuthParams(request, requestToken);
        appendSignature(request);
        final Response response = request.send();
        return this.api.getAccessTokenExtractor().extract(response.getBody());
    }
    
    @Override
    public void signRequest(final Token token, final OAuthRequest request) {
        this.config.log("signing request: " + request.getCompleteUrl());
        
        // Do not append the token if empty. This is for two legged OAuth calls.
        if (!token.isEmpty()) {
            request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
        }
        this.config.log("setting token to: " + token);
        addOAuthParams(request, token);
        appendSignature(request);
    }
    
    private String getSignature(final OAuthRequest request, final Token token) {
        this.config.log("generating signature...");
        final String baseString = this.api.getBaseStringExtractor().extract(request);
        final String signature = this.api.getSignatureService().getSignature(baseString, this.config.getApiSecret(),
                                                                             token.getSecret());
        
        this.config.log("base string is: " + baseString);
        this.config.log("signature is: " + signature);
        return signature;
    }
    
    private void appendSignature(final OAuthRequest request) {
        switch (this.config.getSignatureType()) {
            case Header:
                this.config.log("using Http Header signature");
                
                final String oauthHeader = this.api.getHeaderExtractor().extract(request);
                request.addHeader(OAuthConstants.HEADER, oauthHeader);
                break;
            case QueryString:
                this.config.log("using Querystring signature");
                
                for (final Map.Entry<String, String> entry : request.getOauthParameters().entrySet()) {
                    request.addQuerystringParameter(entry.getKey(), entry.getValue());
                }
                break;
        }
    }

	public String getAuthorizationUrl(Token requestToken, String state) {
	    return api.getAuthorizationUrl(requestToken, state);
	}


}
