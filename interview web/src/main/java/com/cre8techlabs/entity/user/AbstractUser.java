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
package com.cre8techlabs.entity.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;

import com.cre8techlabs.entity.EntityMod;
import com.cre8techlabs.entity.component.ContactPerson;
import com.cre8techlabs.entity.user.auth.OAuth;
import com.cre8techlabs.entity.user.auth.OpenAuth;
import com.cre8techlabs.entity.user.group.AbstractUserGroup;
import com.cre8techlabs.tools.CollectionTargetType;
import com.cre8techlabs.tools.MapTargetType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Document(collection="User")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class AbstractUser extends EntityMod implements UserDetails {

	
	public AbstractUser(){
		
	}
	
	@Indexed(unique=true)
	protected String username;
	
	@Indexed(unique=true)
	protected String email;
	
	protected String salt;
	
	protected boolean changePasswordNextLogon;
	
	private PasswordResetToken passwordResetToken;
	

	
	

	protected String password;

	protected boolean enabled = true;
	
	protected ContactPerson person = new ContactPerson();

	
	protected Date reviewBannedEndDate;
	
	protected List<Date> reviewBannedHistoric = new ArrayList<Date>();
	
	protected Date lastLoginDate;
	
	protected boolean firstLogin;
	
	
	
	@DBRef
	@CollectionTargetType(type=AbstractUserGroup.class)
	protected List<AbstractUserGroup> groups = new ArrayList<AbstractUserGroup>();
	
	@MapTargetType(keyType=String.class, valueType=OpenAuth.class)
	protected Map<OAuth, OpenAuth> openAuths = new HashMap<OAuth, OpenAuth>();
	
	
	public String getUsername() {
		return username == null? null: username.toLowerCase();
	}

	public void setUsername(String username) {
		this.username = username == null? null: username.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	@CollectionTargetType(type=DefaultGrantedAuthority.class)
	private DefaultGrantedAuthority authorities;
	
	@JsonIgnore
	@Override
	public Collection<? extends DefaultGrantedAuthority> getAuthorities() {
		List<DefaultGrantedAuthority> result = new ArrayList<DefaultGrantedAuthority>();
		
		for (UserRole userRole : getRoles()) {
			result.add(new DefaultGrantedAuthority(userRole.name()));
		}
		return result;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	private boolean accountNonLocked = true;
	
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public abstract Set<UserRole> getRoles();

	public void setRoles(Set<UserRole> roles) {
		// intentionally left blank 
	}

	public String getEmail() {
		return email == null? null: email.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email == null? null: email.toLowerCase();
	}
	@JsonProperty
	public void setAuthorities(DefaultGrantedAuthority authorities) {
		this.authorities = authorities;
	}

	public Map<OAuth, OpenAuth> getOpenAuths() {
		return openAuths;
	}

	public void setOpenAuths(
	        Map<OAuth, OpenAuth> openAuths) {
		this.openAuths = openAuths;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

//	@JsonIgnore
	public List<AbstractUserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<AbstractUserGroup> groups) {
		groups.remove(null);
		this.groups = groups;
	}

	public ContactPerson getPerson() {
		return person;
	}

	public void setPerson(ContactPerson person) {
		this.person = person;
	}



	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}


	public boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public boolean hasRole(UserRole role) {
		return getGroups().stream().anyMatch(g -> g.getRoles().stream().anyMatch(r -> r.equals(role.toString())));
    }

	public Date getReviewBannedEndDate() {
		return reviewBannedEndDate;
	}

	public void setReviewBannedEndDate(Date reviewBannedEndDate) {
		this.reviewBannedEndDate = reviewBannedEndDate;
	}

	public List<Date> getReviewBannedHistoric() {
		return reviewBannedHistoric;
	}

	public void setReviewBannedHistoric(List<Date> reviewBannedHistoric) {
		this.reviewBannedHistoric = reviewBannedHistoric;
	}

	public boolean isChangePasswordNextLogon() {
		return changePasswordNextLogon;
	}

	public void setChangePasswordNextLogon(boolean changePasswordNextLogon) {
		this.changePasswordNextLogon = changePasswordNextLogon;
	}
	public PasswordResetToken getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}



}
