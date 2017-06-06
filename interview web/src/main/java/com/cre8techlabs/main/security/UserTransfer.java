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
package com.cre8techlabs.main.security;


import java.util.List;

public class UserTransfer {
	private final String id;
	private final String username;
	private final String firstname;
	private final String lastname;
	private final String email;
	private CompanyTransfer company;
	private final List<String> roles;

	public UserTransfer(String id, String userName,  String firstname, String lastname, String email, CompanyTransfer company,List<String> roles) {
		this.id = id;
		this.username = userName;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.roles = roles;
		this.company = company;
	}

	public String getUsername() {
		return this.username;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

	public List<String> getRoles() {
		return this.roles;
	}

	public String getId() {
		return id;
	}

	public CompanyTransfer getCompany() {
		return company;
	}

	public void setCompany(CompanyTransfer company) {
		this.company = company;
	}

}