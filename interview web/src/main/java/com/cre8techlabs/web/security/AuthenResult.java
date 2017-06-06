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
package com.cre8techlabs.web.security;

import com.cre8techlabs.main.security.TokenTransfer;
import com.cre8techlabs.main.security.UserTransfer;

public class AuthenResult {

	private TokenTransfer token;
	private UserTransfer user;
	private String state;
	private String url;
	private String urlWithParams;

	public AuthenResult(TokenTransfer token, UserTransfer user,
			AbstractRoute routeByState, String urlWithParams) {
		this.user = user;
		this.token = token;
		this.state = routeByState.getStateWithParent();
		this.url = routeByState.getUrlWithParent();
		this.urlWithParams = urlWithParams;
    }

	public TokenTransfer getToken() {
		return token;
	}

	public void setToken(TokenTransfer token) {
		this.token = token;
	}

	public UserTransfer getUser() {
		return user;
	}

	public void setUser(UserTransfer user) {
		this.user = user;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlWithParams() {
		return urlWithParams;
	}

	public void setUrlWithParams(String urlWithParams) {
		this.urlWithParams = urlWithParams;
	}



}
