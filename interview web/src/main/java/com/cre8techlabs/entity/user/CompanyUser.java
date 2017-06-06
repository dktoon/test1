package com.cre8techlabs.entity.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.cre8techlabs.entity.company.Company;
import com.cre8techlabs.entity.user.group.AbstractUserGroup;
import com.cre8techlabs.entity.user.group.CompanyUserGroup;

public class CompanyUser extends AbstractUser {
	private EmailVerifyToken emailVerifyToken;
	public EmailVerifyToken getEmailVerifyToken() {
		return emailVerifyToken;
	}
	public void setEmailVerifyToken(EmailVerifyToken emailVerifyToken) {
		this.emailVerifyToken = emailVerifyToken;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	Company company;
	@Override
	public Set<UserRole> getRoles() {
		Set<UserRole> roles = new HashSet<UserRole>();
		for (AbstractUserGroup group : groups) {
	        if (group instanceof CompanyUserGroup) {
	        	CompanyUserGroup grp = (CompanyUserGroup) group;
	        	if (grp.getCompanyId().equals(getCompany().getId())) {
	        		for (String userRole : grp.getRoles()) {
	        			roles.add(UserRole.valueOf(userRole));
                    }
	        	}
	        }
        }
		
		return Collections.unmodifiableSet(roles);
	}

}
