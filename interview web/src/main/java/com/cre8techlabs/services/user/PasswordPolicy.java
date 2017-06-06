package com.cre8techlabs.services.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PasswordPolicy {

	public boolean validate(String password) {
		if (password == null || password.trim().length() == 0)
			return false;
		if (password.length() < 8)
			return false;
		boolean hasUpperCase = password.matches(".*[A-Z].*");
		boolean hasLowerCase = password.matches(".*[a-z].*");
		boolean hasNumbers = password.matches(".*\\d.*");
		if (!hasUpperCase || !hasLowerCase || !hasNumbers)
			return false;
		return true;
	}

	public List<String> errors(String password) {
		List<String> errors = new ArrayList<>();
		if (password == null || password.trim().length() == 0) {
			errors.add("Password is empty");
			return errors;
		}
		if (password.length() < 8)
			errors.add("Min. password length is 9");
		boolean hasUpperCase = password.matches(".*[A-Z].*");
		boolean hasLowerCase = password.matches(".*[a-z].*");
		boolean hasNumbers = password.matches(".*\\d.*");
		if (!hasUpperCase) {
			errors.add("At least 1 Upper case character");
		}
		if (!hasLowerCase) {
			errors.add("At least 1 Lower case character");
		}
		if (!hasNumbers) {
			errors.add("Must contains at least 1 Number");
		}
		return errors;
	}

}
