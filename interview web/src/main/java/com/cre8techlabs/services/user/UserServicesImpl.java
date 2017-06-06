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
package com.cre8techlabs.services.user;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cre8techlabs.entity.user.AbstractUser;
import com.cre8techlabs.entity.user.PasswordResetToken;
import com.cre8techlabs.repository.user.UserRepository;


@Service
public class UserServicesImpl implements UserServices {
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDetails userDetail = userRepository.findUserByUsername(username);	
		if (userDetail == null) return null;
		Date lastLogin = ((AbstractUser) userDetail).getLastLoginDate();
		Date now = new Date();
		if (lastLogin== null || now.getTime() - lastLogin.getTime()  < 5000) {
			((AbstractUser) userDetail).setFirstLogin(true);
//			
		} else {
			((AbstractUser) userDetail).setFirstLogin(false);
//			
		}

		return userDetail;
	}

	@Override
	public AbstractUser createUser(AbstractUser user) throws IOException {
		
		AbstractUser newUser = userRepository.save(user);
		
		return newUser;
	}

	@Override
	public AbstractUser resetPassword(ObjectId userId, boolean sendNotification)
			throws IOException {
		AbstractUser user = userRepository.findOne(userId.toString());
		if (!user.isEnabled())
			throw new IllegalStateException("User is disabled and hence we cannot reset his password.");
		user.setChangePasswordNextLogon(true);
		String temporaryPassword =  (String) (UUID.randomUUID().toString()).subSequence(0, 10);
		user.setPasswordResetToken(new PasswordResetToken());
		user.getPasswordResetToken().setTemporaryPassword(temporaryPassword);
		user.setPassword(temporaryPassword);
		user.setSalt("");
		
		userRepository.save(user);
		
		return user;
	}


}
