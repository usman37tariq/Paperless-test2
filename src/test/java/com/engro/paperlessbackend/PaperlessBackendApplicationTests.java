package com.engro.paperlessbackend;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.engro.paperlessbackend.dao.TagDao;
import com.engro.paperlessbackend.dto.UserLoginDto;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.services.UsersService;
@SpringBootTest
class PaperlessBackendApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(PaperlessBackendApplicationTests.class);
	@Autowired
	UsersService usersService;
	@Autowired TagDao tagDao;
	@Test
	@DisplayName("Test Spring @Autowired Integration")
	void contextLoads() {
		logger.info("running junit test");
	}
	@Test
	@DisplayName("Test Spring Invalid Email Login")
	void testEmailValidationForLogin() {
		logger.info("running Login Email Validation Unit test");
		//UserLoginDto resp= new UserLoginDto();
		Users user = new Users();
		user.setEmail("emailemail.com");
		user.setPassword("password");
		ResponseEntity response =  (ResponseEntity) usersService.authenticateUser(user);
		assertEquals(ResponseEntity.badRequest().body("Invalid Email").getBody(), response.getBody());
		
	}
	@Test
	@DisplayName("Test Spring Invalid Creds Login")
	void testInvalidLoginResponse() {
		logger.info("running inValid Login Unit test");
		Users user_invalid = new Users();
		user_invalid.setEmail("email@email.com");
		user_invalid.setPassword("password");
		ResponseEntity response= (ResponseEntity) usersService.authenticateUser(user_invalid);
		assertEquals(ResponseEntity.badRequest().body("Authentication Failed!").getBody(), response.getBody());
	}
	@Test
	@DisplayName("Test Spring Valid Creds Login")
	void testValidLoginResponse() {
		logger.info("running  Valid Login Unit test");
		UserLoginDto resp = new UserLoginDto();
		Users user_valid = new Users();
		user_valid.setEmail("email@email.com");
		user_valid.setPassword("admin");
		UserLoginDto response= (UserLoginDto) usersService.authenticateUser(user_valid);
		assertEquals(response.getUser().getEmail(), user_valid.getEmail());
	}
	

}
