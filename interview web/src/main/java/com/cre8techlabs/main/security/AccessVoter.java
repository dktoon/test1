package com.cre8techlabs.main.security;

import java.util.HashMap;
import java.util.Map;

import com.cre8techlabs.entity.user.AbstractUser;

public interface AccessVoter {
	public static Map<String, AccessVoter> map = new HashMap<String, AccessVoter>();
	public boolean pass(Class<? extends Object> ctrlClass, String method, Object[] args, AbstractUser user);
}
