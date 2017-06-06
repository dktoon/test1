package com.cre8techlabs.web.security;

import org.springframework.stereotype.Component;

@Component
public class RouteMapLogin extends AbstractRouteMap<Route> {

	public Route login = new Route("login", "login", null, "ang-app/view/login/index.html", all_screen_format);
	public Route login_login = new Route(login, "log", "log", "controllers.login.LoginController", "ang-app/view/login/login.html", all_screen_format);
	public Route login_forgot_password = new Route(login, "forgot", "forgot", "controllers.login.ForgotPasswordController", "ang-app/view/login/forgot_password.html", all_screen_format);
	
	public Route login_change_password_required = new Route(login, "changePasswordRequired", "changePasswordRequired/{userId}/{tokenId}", "controllers.login.ChangePasswordRequiredController", 
			"ang-app/view/login/changePasswordRequired.html", all_screen_format);
	
	public Route login_contact = new Route(login, "contact", "contact", "controllers.login.ContactController", "ang-app/view/login/contact.html", all_screen_format);
	public Route login_verifyEmail = new Route(login, "verifyEmail", "verifyEmail/{userId}/{tokenId}", "controllers.login.VerifyEmailController", "ang-app/view/login/verifyEmail.html", all_screen_format);
	

	/**
	 * Signup for Invite
	 */
	public Route login_signup = new Route(login, "signup", "signup/{inviteId}", "controllers.login.SignupController", "ang-app/view/login/signup.html", all_screen_format);
	public Route login_signup_info = new Route(login_signup, "info", "info", null, "ang-app/view/login/signup_info.html", all_screen_format);
//	public Route login_signup_captcha = new Route(login_signup, "captcha", "captcha", null, "ang-app/view/login/signup_captcha.html", all_screen_format);
	public Route login_signup_confirm = new Route(login_signup, "confirm", "confirm", null, "ang-app/view/login/signup_confirm.html", all_screen_format);
	public Route login_signup_confirm_company = new Route(login_signup, "confirmcompany", "confirmcompany", null, "ang-app/view/login/signup_confirmCompany.html", all_screen_format);
	public Route login_signup_captchaCompany = new Route(login_signup, "captchacompany", "captchacompany", null, "ang-app/view/login/signup_captchaCompany.html", all_screen_format);
	
	public Route login_signup_done = new Route(login_signup, "done", "done", null, "ang-app/view/login/signup_done.html", all_screen_format);
	
}
