package com.engro.paperlessbackend.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.dto.UpdatePasswordDto;
import com.engro.paperlessbackend.dto.UserDto;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.services.UsersService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class UsersController {

	@Autowired
	UsersService usersService;
	
	@ApiOperation(value = "Add New User", tags = "Users")
	@RequestMapping(method = RequestMethod.POST, value = "users")
	public Object addNewUser(@RequestBody UserDto user) {
		return usersService.addNewUser(user);
	}
	
	@ApiOperation(value = "Update User", tags = "Users")
	@RequestMapping(method = RequestMethod.PUT, value = "users")
	public Object updateUser(@RequestBody UserDto user) {
		return usersService.updateUser(user);
	}
	
	@ApiOperation(value = "Get All User (Active)", tags = "Users")
	@RequestMapping(method = RequestMethod.GET, value = "users")
	public Object getAllUsers() {
		return usersService.getAllUsers();
	}
	
	@ApiOperation(value = "Delete User by User ID", tags = "Users")
	@RequestMapping(method = RequestMethod.DELETE, value = "users/{userId}")
	public Object deleteUserByUserId(@PathVariable String userId) {
		return usersService.deleteUserByUserId(userId);
	}
	
	@ApiOperation(value = "Get User by User ID", tags = "Users")
	@RequestMapping(method = RequestMethod.GET, value = "users/{userId}")
	public Object getUserByUserId(@PathVariable String userId) {
		return usersService.getUserByUserId(userId);
	}
	
	@ApiOperation(value = "User Login", tags = "Users")
	@RequestMapping(method = RequestMethod.POST, value = "login")
	public Object authenticateUser(HttpServletRequest request, @RequestBody Users user) {
		String addr = request.getRemoteAddr().toString();
		
		return usersService.authenticateUser(user);
	}
	
	@ApiOperation(value = "Update Password", tags = "Users")
	@RequestMapping(method = RequestMethod.POST, value = "users/updatePassword")
	public Object updatePassword(@RequestBody UpdatePasswordDto updatePassword) {
		return usersService.updatePassword(updatePassword);
	}
	
	
}
