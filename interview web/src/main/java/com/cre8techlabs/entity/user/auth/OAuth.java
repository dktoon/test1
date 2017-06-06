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
package com.cre8techlabs.entity.user.auth;
import java.util.Map;

public enum OAuth {
	Google(new Extract() {

		@Override
		public String extractReadableFullname(Map<String, Object> attributes) {
			return attributes.get("name").toString();
		}

		@Override
		public String extractEmail(Map<String, Object> attributes) {
			return attributes.get("email").toString();
		}

		@Override
		public String extractProfileLink(Map<String, Object> attributes) {
			return attributes.get("link") == null? null: attributes.get("link").toString();
		}}), 
	
	Facebook(new Extract() {

		@Override
		public String extractReadableFullname(Map<String, Object> attributes) {
			return attributes.get("first_name") + " " + attributes.get("last_name");
		}

		@Override
		public String extractEmail(Map<String, Object> attributes) {
			return attributes.get("email").toString();
		}

		@Override
		public String extractProfileLink(Map<String, Object> attributes) {
			return attributes.get("link").toString();
		}}), 
	
	LinkedIn(new Extract() {

		@Override
		public String extractReadableFullname(Map<String, Object> attributes) {
			return attributes.get("formatted-name").toString();
		}

		@Override
		public String extractEmail(Map<String, Object> attributes) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String extractProfileLink(Map<String, Object> attributes) {
			return attributes.get("public-profile-url").toString();
		}}), 
	
	Twitter(new Extract() {

		@Override
		public String extractReadableFullname(Map<String, Object> attributes) {
			return attributes.get("name").toString();
		}

		@Override
		public String extractEmail(Map<String, Object> attributes) {
			return null;
		}

		@Override
		public String extractProfileLink(Map<String, Object> attributes) {
			return "https://twitter.com/" + attributes.get("screen_name");
		}});
	
	private OAuth(Extract extract) {
		this.extract = extract;
	}
	
	private static interface Extract {
		String extractReadableFullname(Map<String, Object> attributes);
		String extractEmail(Map<String, Object> attributes);
		String extractProfileLink(Map<String, Object> attributes);
	}
	Extract extract;
	public String extractReadableFullname(Map<String, Object> attributes) {
		return extract.extractReadableFullname(attributes);
	}
	public String extractEmail(Map<String, Object> attributes) {
		return extract.extractEmail(attributes);
	}
	public String extractProfileLink(Map<String, Object> attributes) {
		return extract.extractProfileLink(attributes);
	}
}
