package com.cre8techlabs.entity.user;

import com.cre8techlabs.entity.EntityMod;

public class PasswordResetToken extends EntityMod {
	private String temporaryPassword;

	public String getTemporaryPassword() {
		return temporaryPassword;
	}

	public void setTemporaryPassword(String temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}
}