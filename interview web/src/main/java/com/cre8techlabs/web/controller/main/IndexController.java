package com.cre8techlabs.web.controller.main;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cre8techlabs.entity.user.AdminUser;
import com.cre8techlabs.repository.user.UserRepository;

@Controller
public class IndexController {
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/index.html")
	public String index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		createUsers();
			
		return "index";
	}

	private void createUsers() {
		String[] users = {
				"interview", "homer", "marge", "bart", "lisa", "maggie"
		};
		for (String user : users) {
			createUser(user);
		}
		
	}

	private void createUser(String u) {
		AdminUser user = (AdminUser) userRepository.findUserByUsername(u);
		if (user == null) {
			user = new AdminUser();
			user.setUsername(u);
			user.setPassword(u);
			user.setEmail(u + "@lenderprice.com");
			
			userRepository.save(user);
		}
	}

	@RequestMapping("/")
	public String emptyPath(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}
	
	
}
