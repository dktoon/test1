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
package com.cre8techlabs.main.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.cre8techlabs.entity.user.AbstractUser;
import com.cre8techlabs.entity.user.CompanyUser;
import com.cre8techlabs.entity.user.PasswordResetToken;
import com.cre8techlabs.entity.user.UserRole;
import com.cre8techlabs.repository.user.UserRepository;
import com.cre8techlabs.web.rest.RestConst;
import com.cre8techlabs.web.security.AuthenResult;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonAuthenticationSuccessHandler extends
        SimpleUrlAuthenticationSuccessHandler implements RestConst {
	
	private DefaultPages defaultPages;

	public JsonAuthenticationSuccessHandler(ObjectMapper mapper,
			UserRepository userRepository,
			DefaultPages defaultPages
			) {
		this.defaultPages = defaultPages;
		this.mapper = mapper;
		this.userRepository = userRepository;
	}
	
	private ObjectMapper mapper;
	
	UserRepository userRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
	        HttpServletResponse response, Authentication auth)
	        throws IOException, ServletException {

		if (request.getHeader("Content-Type").contains(CONTENT_TYPE_APPLICATION_JSON)) {

			AbstractUser principal = (AbstractUser) auth.getPrincipal();
			if (principal.isChangePasswordNextLogon()) {
				Map<String, String> res = new HashMap<String, String>();
				PasswordResetToken prt = new PasswordResetToken();
				principal.setPasswordResetToken(prt);
				userRepository.save(principal);
				res.put("redirect", "/login/changePasswordRequired/" + principal.getId() + "/" + principal.getPasswordResetToken().getId());
				response.getWriter().print(mapper.writeValueAsString(res));
				response.getWriter().flush();
				return;
			}
			TokenTransfer token = new TokenTransfer(TokenUtils.createToken((UserDetails) auth.getPrincipal()));
			
			
			principal.setLastLoginDate(new Date());
			userRepository.save(principal);
			
			CompanyTransfer company = null;
			if (principal instanceof CompanyUser) {
				CompanyUser cu = (CompanyUser) principal;
				if (cu.getCompany() != null)
					company = new CompanyTransfer(cu.getCompany());
			}
			
			UserTransfer user = new UserTransfer(principal.getId().toString(),
					principal.getUsername(),
					principal.getPerson().getFirstname(),
					principal.getPerson().getLastname(),
					principal.getEmail(),							
								company, principal.getRoles().stream().map(UserRole::toString).collect(Collectors.toList()));
			

			Device device = new LiteDeviceResolver().resolveDevice(request);
			
			AuthenResult authenResult = new AuthenResult(token, user, defaultPages.getDefaultRoute(principal), defaultPages.getDefaultRouteUrl(principal, device.isMobile()));
			
			response.getWriter().print(mapper.writeValueAsString(authenResult));
			response.getWriter().flush();
		} else {
			super.onAuthenticationSuccess(request, response, auth);
		}
	}
}
