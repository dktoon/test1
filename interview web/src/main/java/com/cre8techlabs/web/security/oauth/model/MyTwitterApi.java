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

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;

public class MyTwitterApi extends DefaultApi10a {
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize?oauth_token=%s";
	private static final String AUTHORIZE_URL_STATE = "https://api.twitter.com/oauth/authorize?oauth_token=%s&state=%s";
	private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
	private static final String ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";

	@Override
	public String getAccessTokenEndpoint() {
		return "https://" + ACCESS_TOKEN_RESOURCE;
	}

	@Override
	public String getRequestTokenEndpoint() {
		return "https://" + REQUEST_TOKEN_RESOURCE;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}
	
	public String getAuthorizationUrl(Token requestToken, String state) {
		return String.format(AUTHORIZE_URL_STATE, requestToken.getToken(), OAuthEncoder.encode(state));
	}

	/**
	 * Twitter 'friendlier' authorization endpoint for OAuth.
	 */
	public static class Authenticate extends MyTwitterApi {
		private static final String AUTHENTICATE_URL = "https://api.twitter.com/oauth/authenticate?oauth_token=%s";

		@Override
		public String getAuthorizationUrl(Token requestToken) {
			return String.format(AUTHENTICATE_URL, requestToken.getToken());
		}
		
	}
}
