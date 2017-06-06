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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import com.cre8techlabs.entity.company.Company;
import com.cre8techlabs.entity.user.AbstractUser;
import com.cre8techlabs.entity.user.AdminUser;
import com.cre8techlabs.entity.user.CompanyUser;
import com.github.mateuszwenus.spring_security_controller_auth.HandlerInvocation;
import com.github.mateuszwenus.spring_security_controller_auth.NullFilterChain;

public class LPVoterAdapter implements AccessDecisionVoter<HandlerInvocation> {

	public static class CheckAccessVoter implements AccessDecisionVoter<FilterInvocation> {

		@Override
        public boolean supports(ConfigAttribute attribute) {
	        return attribute instanceof LPMethodConfigAttribute;
        }

		@Override
        public boolean supports(Class<?> clazz) {
			return HandlerInvocation.class.isAssignableFrom(clazz);
        }

		@Override
        public int vote(Authentication authentication, FilterInvocation fi,
    			Collection<ConfigAttribute> attributes) {
			List<LPMethodConfigAttribute> configs = find(attributes);
			
			if (configs.isEmpty())
				return AccessDecisionVoter.ACCESS_ABSTAIN;
			
			for (LPMethodConfigAttribute config : configs) {
	            if (AccessDecisionVoter.ACCESS_DENIED == checkConfig(authentication, fi, config)) {
	            	
	            	return AccessDecisionVoter.ACCESS_DENIED;
	            }
            }
			return AccessDecisionVoter.ACCESS_GRANTED;
        }


		List<LPMethodConfigAttribute> find(Collection<ConfigAttribute> attributes) {
			return attributes.stream().filter(a -> a instanceof LPMethodConfigAttribute).map(new Function<ConfigAttribute, LPMethodConfigAttribute>() {
				@Override
                public LPMethodConfigAttribute apply(ConfigAttribute t) {
	                return (LPMethodConfigAttribute) t;
                }
			}).collect(Collectors.toList());
		}
		
		private int checkConfig(Authentication authentication, FilterInvocation fi, LPMethodConfigAttribute config) {
			
			AbstractUser user = (AbstractUser) authentication.getPrincipal();
			if (user instanceof AdminUser) {
				return AccessDecisionVoter.ACCESS_GRANTED;
			}
			if (user instanceof CompanyUser) {
				CompanyUser usr = (CompanyUser) user;
				
				CheckType type = config.getType();
				
				if (type == CheckType.UserCanOperateCompany) {
					Map<String, Object> objects = config.getObjectMap();
					

					int finalVote = AccessDecisionVoter.ACCESS_ABSTAIN;
					for (Entry<String, Object> entry : objects.entrySet()) {
						int vote = tryToVote(usr, entry);
						if (vote == AccessDecisionVoter.ACCESS_DENIED) {
							return AccessDecisionVoter.ACCESS_DENIED;
						} else {
							finalVote = vote;
							
						}
                    }
					return finalVote;
					
				} else if (type == CheckType.UserCanOperateRate) {
					
				}
				
			}
			return AccessDecisionVoter.ACCESS_DENIED;
        }

		private int tryToVote(CompanyUser usr, Entry<String, Object> entry) {
	        if (entry.getValue() instanceof Company) {
	        	Company company = (Company) entry.getValue();
	        	return usr.getCompany().getId().equals(company.getId())? AccessDecisionVoter.ACCESS_GRANTED: AccessDecisionVoter.ACCESS_DENIED;
	        } else if (entry.getValue() instanceof String) {
	        	return usr.getCompany().getId().toString().equals(entry.getValue())? AccessDecisionVoter.ACCESS_GRANTED: AccessDecisionVoter.ACCESS_DENIED;
	        }
	        throw new IllegalArgumentException("cannut use Object in parameter to determine security");
        }
	}
	
	
	private WebExpressionVoter webExpressionVoter;
	private CheckAccessVoter checkAccessVoter = new CheckAccessVoter();
	
	
	public LPVoterAdapter() {
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return webExpressionVoter.supports(attribute)
		        || (attribute instanceof LPMethodConfigAttribute);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return HandlerInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public int vote(Authentication authentication, HandlerInvocation object,
	        Collection<ConfigAttribute> attributes) {
		FilterInvocation fi = new FilterInvocation(object.getRequest(),
		        object.getResponse(), new NullFilterChain());
		int vote = webExpressionVoter.vote(authentication, fi, attributes);
		if (AccessDecisionVoter.ACCESS_GRANTED == vote
		        || AccessDecisionVoter.ACCESS_ABSTAIN == vote) {
			int secondVote = checkAccessVoter.vote(authentication, fi, attributes);
			if (AccessDecisionVoter.ACCESS_DENIED == secondVote)
				return AccessDecisionVoter.ACCESS_DENIED;
			return vote;
		} else {
			return vote;
		}
		// return WebExpressionVoter.ACCESS_GRANTED;
	}

	public WebExpressionVoter getWebExpressionVoter() {
		return webExpressionVoter;
	}

	public void setWebExpressionVoter(WebExpressionVoter webExpressionVoter) {
		this.webExpressionVoter = webExpressionVoter;
	}
}
