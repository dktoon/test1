package com.cre8techlabs.entity.user;

import com.cre8techlabs.utils.collection.HashSetStringEnum;

public class UserRoleHashSet extends HashSetStringEnum<UserRole>{
	public UserRoleHashSet() {
		super(UserRole.class);
	}
}
