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
package com.cre8techlabs.web.rest.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cre8techlabs.entity.user.AbstractUser;
import com.cre8techlabs.entity.user.CompanyUser;
import com.cre8techlabs.entity.user.UserRole;
import com.cre8techlabs.main.security.CompanyTransfer;
import com.cre8techlabs.main.security.DefaultPages;
import com.cre8techlabs.main.security.TokenTransfer;
import com.cre8techlabs.main.security.TokenUtils;
import com.cre8techlabs.main.security.UserTransfer;
import com.cre8techlabs.web.rest.RestConst;
import com.cre8techlabs.web.security.AuthenResult;
import com.cre8techlabs.web.security.Route;
import com.cre8techlabs.web.security.RouteMap;
import com.cre8techlabs.web.security.filter.JsonUsernamePasswordAuthenticationFilter.SimpleGeoContainer;

@RestController
@RequestMapping("/security")
public class SecurityController implements RestConst {

	
	@Autowired
	DefaultPages defaultPages;
	
	@Autowired
	RouteMap routeMap;
	
	
	@RequestMapping(value="/setGeoData", method = RequestMethod.POST, headers = ACCEPT_JSON)
	public void setGeoData(HttpServletRequest request, @RequestBody SimpleGeoContainer geoContainer) {
		request.getSession().setAttribute("GeoData", geoContainer);
	}
	
	@RequestMapping("/routeMap")
	public Set<Route> routeMap() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return Collections.EMPTY_SET;
		}
		AbstractUser user = (AbstractUser) auth.getPrincipal();
		Set<Route> routes = new HashSet<Route>();
		
		for (UserRole role : user.getRoles()) {
	        List<Route> rts = routeMap.routeMapByRole().get(role);
	        routes.addAll(rts);
        }
		
		return routes;
	}

	@RequestMapping("/routes")
	public Collection<Route> routes() {
		return routeMap.routes();
	}
	

	@RequestMapping("/loggedUser")
	public AuthenResult getLoggedUser(HttpServletRequest request, HttpServletResponse response, Device device) throws IOException {

		boolean isMobile = device.isMobile();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			HttpSession session = request.getSession();
			if (session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
				SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
				auth = securityContext.getAuthentication();
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				response.sendError(
						HttpServletResponse.SC_UNAUTHORIZED,
						"Unauthorized: Authentication token was either missing or invalid.");
				return null;
				
			}
		}
		AbstractUser principal = (AbstractUser) auth.getPrincipal();
		
		TokenTransfer token = new TokenTransfer(TokenUtils.createToken((UserDetails) auth.getPrincipal()));
		
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
		
		AuthenResult authenResult = new AuthenResult(token, user, defaultPages.getDefaultRoute(principal), defaultPages.getDefaultRouteUrl(principal, isMobile));
		return authenResult;
	}
}
