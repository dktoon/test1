package com.cre8techlabs.entity.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.cre8techlabs.entity.user.group.AbstractUserGroup;
import com.cre8techlabs.entity.user.group.AdminUserGroup;

public class AdminUser extends AbstractUser {

	@Override
	public Set<UserRole> getRoles() {
		Set<UserRole> roles = new HashSet<UserRole>();
		for (AbstractUserGroup group : groups) {
	        if (group instanceof AdminUserGroup) {
	        	AdminUserGroup grp = (AdminUserGroup) group;
        		for (String userRole : grp.getRoles()) {
        			roles.add(UserRole.valueOf(userRole));
                }
	        }
        }
		if (roles.isEmpty())
			roles.add(UserRole.Admin);
		return Collections.unmodifiableSet(roles);

	}

}
