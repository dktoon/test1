package com.cre8techlabs.entity.user;

import com.cre8techlabs.entity.EntityMod;

public class EmailVerifyToken extends EntityMod {
	private String temporaryPassword;

	public String getTemporaryPassword() {
		return temporaryPassword;
	}

	public void setTemporaryPassword(String temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}
}