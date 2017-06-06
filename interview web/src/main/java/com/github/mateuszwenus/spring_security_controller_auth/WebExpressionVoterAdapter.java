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
package com.github.mateuszwenus.spring_security_controller_auth;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;

public class WebExpressionVoterAdapter implements AccessDecisionVoter<HandlerInvocation> {

  private final WebExpressionVoter webExpressionVoter;

  public WebExpressionVoterAdapter(WebExpressionVoter webExpressionVoter) {
    this.webExpressionVoter = webExpressionVoter;
  }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return webExpressionVoter.supports(attribute);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return HandlerInvocation.class.isAssignableFrom(clazz);
  }

  @Override
  public int vote(Authentication authentication, HandlerInvocation object, Collection<ConfigAttribute> attributes) {
    FilterInvocation fi = new FilterInvocation(object.getRequest(), object.getResponse(), new NullFilterChain());
    return webExpressionVoter.vote(authentication, fi, attributes);
//    return WebExpressionVoter.ACCESS_GRANTED;
  }
}
