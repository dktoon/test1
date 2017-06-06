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


public class StateParam {
	String chunk;
	ReqType state;
	public String getChunk() {
		return chunk;
	}
	public void setChunk(String chunk) {
		this.chunk = chunk;
	}
	public ReqType getState() {
		return state;
	}
	public void setState(ReqType state) {
		this.state = state;
	}

}