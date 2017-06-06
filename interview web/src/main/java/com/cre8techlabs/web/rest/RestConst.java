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
package com.cre8techlabs.web.rest;

import org.springframework.http.HttpStatus;

public interface RestConst {
	String RANGE_PREFIX = "items=";
	String CONTENT_RANGE_HEADER = "Content-Range";
	String ACCEPT_JSON = "Accept=application/json";
	String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	
	String ACCEPT_TEXT_XML = "Accept=text/xml";
	String ACCEPT_TEXT = "Accept=text/plain";
	String ACCEPT_ALL = "Accept=*/*";
	
	public class BasicError{
		private String message;
		private int code;
		
		public BasicError(){
			code = HttpStatus.OK.value();
			message = "";
		}
		
		public BasicError(int code, String message){
			this.code = code;
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		
	}
}
