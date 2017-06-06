package com.cre8techlabs.entity.user;

import com.cre8techlabs.utils.collection.TreeSetStringEnum;

public class UserRoleTreeSet extends TreeSetStringEnum<UserRole>{
	public UserRoleTreeSet() {
		super(UserRole.class);
	}
}
