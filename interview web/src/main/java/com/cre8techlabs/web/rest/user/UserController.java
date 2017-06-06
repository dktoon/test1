package com.cre8techlabs.web.rest.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cre8techlabs.entity.user.AbstractUser;
import com.cre8techlabs.entity.user.AdminUser;
import com.cre8techlabs.repository.user.UserRepository;
import com.cre8techlabs.web.rest.AbstractRestController;

/*
 * Add the necessary Annotation, method in order to use this rest controller.
 * If you don't want to do too much of the work, you better read the code first
 */
@RestController
@RequestMapping(value="/users")
public class UserController extends AbstractRestController<AbstractUser> {

	@Autowired
	UserRepository userRepository;
	
	
	
	@Override
	protected MongoRepository<AbstractUser, String> getCrudRepository() {
		return userRepository;
	}

	@Override
	protected AbstractUser createNew(HttpServletRequest request, String id) throws IOException {
		
		return new AdminUser();
	}
	
	@RequestMapping(value="/getAllAdmin")
	public List<AbstractUser> getAllAdmin(){
				return userRepository.findAll();
	}
	
	@RequestMapping(value="/deleteAdmin")
	public void deleteAdmin(AbstractUser user){
		userRepository.delete(user);
	}
	

}
