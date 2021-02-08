package com.engro.paperlessbackend.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dao.ChecklistApproverDao;
import com.engro.paperlessbackend.dao.GroupDao;
import com.engro.paperlessbackend.dao.RoleDao;
import com.engro.paperlessbackend.dao.RoleResourceDao;
import com.engro.paperlessbackend.dao.UserGroupDao;
import com.engro.paperlessbackend.dao.UsersDao;
import com.engro.paperlessbackend.dto.AccessDto;
import com.engro.paperlessbackend.dto.EmailDto;
import com.engro.paperlessbackend.dto.UpdatePasswordDto;
import com.engro.paperlessbackend.dto.UserDto;
import com.engro.paperlessbackend.dto.UserLoginDto;
import com.engro.paperlessbackend.entities.ChecklistApproverGroup;
import com.engro.paperlessbackend.entities.ChecklistApproverUser;
import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.RoleResource;
import com.engro.paperlessbackend.entities.UserGroup;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.utils.Constants;
import com.engro.paperlessbackend.utils.WebUtil;

@Component
public class UsersService {

	private static Logger logger = LoggerFactory.getLogger(UsersService.class);

	@Autowired
	UsersDao usersDao;

	@Autowired
	GroupDao groupDao;

	@Autowired
	UserGroupDao userGroupDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	RoleResourceDao roleResourceDao;

	@Autowired
	ChecklistApproverDao checklistApproverDao;

	@Autowired
	EmailService emailService;

	@Transactional
	public Object addNewUser(UserDto userDto) {
		Users user = new Users();
		Users newUser = new Users();
		UserDto newUserDto = new UserDto();
		List<Group> groups = new ArrayList<Group>();

		try {
			String phoneNumberLength = userDto.getUser().getPhoneNumber().toString();
			if (phoneNumberLength.length() > 15) {
				logger.info("Phone Number length exceeded limit");
				return ResponseEntity.badRequest().body("Phone Number length exceeded limit");
			}

			user = userDto.getUser();
			String userEmail = user.getEmail().toLowerCase();
			user.setEmail(userEmail);
			/* This code is commented out temporarily due to UI changes
			groups = userDto.getGroups();
			*/
			String userName = user.getUserName().trim();
			user.setUserName(userName);

			List<Users> existingEmailUsers = usersDao.getUserByEmail(user.getEmail());
			if (existingEmailUsers != null && !existingEmailUsers.isEmpty()) {
				logger.error("Email already taken");
				return ResponseEntity.badRequest().body("Email already taken. Please choose a different Email address");
			}

			if (userDto.getUser().getUserRole() != null) {
				if (userDto.getUser().getUserRole().getRoleId() != null) {
					user.setUserRole(roleDao.getRoleByRoleId(userDto.getUser().getUserRole().getRoleId()));
				}
			}

			if (user.getPassword() != null && !user.getPassword().isEmpty()) {
				user.setPasswordUpdatedAt(System.currentTimeMillis());
			}

			newUser = usersDao.addNewUser(user);

			// send email
			if (newUser != null) {

				EmailDto emailDto = new EmailDto();

				emailDto.setEmailRecipient(newUser.getEmail());
//				emailDto.setEmailRecipient(Constants.DUMMY_RECEIVER_EMAIL_ADDRESS);

				emailDto.setEmailSubject(Constants.EMAIL_SUBJECT_NEW_USER);
				if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
					emailDto.setEmailBody(String.format(Constants.EMAIL_HTML_BODY_WITH_PASSWORD, newUser.getUserName(),
							Constants.BASE_URL_PROD, newUser.getEmail(), newUser.getPassword()));
				} else {
//					emailDto.setEmailBody(
//							String.format(Constants.EMAIL_HTML_BODY_WITHOUT_PASSWORD, newUser.getUserName(),
//									String.format(Constants.SET_NEW_PASSWORD_URL_LOCAL,
//											Base64.getEncoder().encodeToString(newUser.getId().getBytes())),
//									newUser.getEmail()));

					emailDto.setEmailBody(
							String.format(Constants.EMAIL_HTML_BODY_WITHOUT_PASSWORD, newUser.getUserName(),
									String.format(Constants.SET_NEW_PASSWORD_URL_PROD, newUser.getId()),
									newUser.getEmail()));

				}
				emailService.sendMail(emailDto);
			}
			/* This code is commented out temporarily due to UI changes
			List<UserGroup> userGroups = new ArrayList<UserGroup>();
			for (int i = 0; i < groups.size(); i++) {
				UserGroup userGroup = new UserGroup();
				Group group = groupDao.getGroupByGroupId(groups.get(i).getId());
				userGroup.setGroup(group);
				userGroup.setUser(newUser);
				userGroups.add(userGroup);
			}
			userGroupDao.addUserGroups(userGroups);
			List<Group> addedGroups = groupDao.getGroupsByUserId(newUser.getId());
			newUserDto.setUser(newUser);
			newUserDto.setGroups(addedGroups);
			*/
			newUserDto.setUser(newUser);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while adding new user");
		}
		return newUserDto;
	}

	public Object updateUser(UserDto userDto) {
		Users user = userDto.getUser();
		UserDto savedUser = new UserDto();
		List<Group> groups = userDto.getGroups();

		try {
			String userName = user.getUserName().trim();
			user.setUserName(userName);

			String userEmail = user.getEmail().toLowerCase();
			user.setEmail(userEmail);
			List<Users> existingEmailUsers = usersDao.getExistingUsersWithProvidedEmail(user);
			if (existingEmailUsers != null && !existingEmailUsers.isEmpty()) {
				logger.error("Email already taken");
				return ResponseEntity.badRequest().body("Email already taken. Please choose a different Email address");
			}

			if (!userGroupDao.deleteUserGroupByUserId(user.getId())) {
				return ResponseEntity.badRequest().body("Error occured while updating User");
			}

			Users existingUser = usersDao.getUserByUserId(user.getId());

			String oldPassword = existingUser.getPassword();
			boolean isPasswordChanged = false;

			if (oldPassword != null) {
				if (!oldPassword.equals(user.getPassword())) {
					user.setPasswordUpdatedAt(System.currentTimeMillis());
					isPasswordChanged = true;
				} else {
					user.setPasswordUpdatedAt(existingUser.getPasswordUpdatedAt());
				}
			} else {
				user.setPasswordUpdatedAt(System.currentTimeMillis());
				isPasswordChanged = true;
			}
			Users updatedUser = usersDao.updateUser(user);

			/// send password changed email
			if (updatedUser != null && isPasswordChanged) {

				Users updatedBy = null;
				if (userDto.getUpdatedBy() != null && !userDto.getUpdatedBy().isEmpty()) {
					updatedBy = usersDao.getUserByUserId(userDto.getUpdatedBy());
				}

				if (updatedBy != null) {

					EmailDto emailDto = new EmailDto();

					emailDto.setEmailRecipient(updatedUser.getEmail());
//					emailDto.setEmailRecipient(Constants.DUMMY_RECEIVER_EMAIL_ADDRESS);

					emailDto.setEmailSubject(Constants.EMAIL_SUBJECT_PASSWORD_CHANGED);
					emailDto.setEmailBody(String.format(Constants.EMAIL_HTML_BODY_PASSWORD_RESET,
							updatedUser.getUserName(), updatedBy.getUserName(), Constants.BASE_URL_PROD,
							updatedUser.getEmail(), updatedUser.getPassword()));

					emailService.sendMail(emailDto);
				}
			}

			List<UserGroup> userGroups = new ArrayList<UserGroup>();
			for (int i = 0; i < groups.size(); i++) {
				UserGroup userGroup = new UserGroup();
				userGroup.setUser(updatedUser);
				userGroup.setGroup(groups.get(i));
				userGroups.add(userGroup);
			}
			userGroupDao.addUserGroups(userGroups);
			List<Group> updatedGroups = groupDao.getGroupsByUserId(updatedUser.getId());
			savedUser.setGroups(updatedGroups);
			savedUser.setUser(updatedUser);
		} catch (Exception ex) {
			logger.error("[UsersService] - [updateUser] - [Error occured while updating user]");
			return ResponseEntity.badRequest().body("Error occured while updating user");
		}
		return savedUser;
	}

	public Object getAllUsers() {
		List<UserDto> usersDto = new ArrayList<UserDto>();
		try {
			List<Users> users = usersDao.getAllUsers();
			for (Users user : users) {
				UserDto userDto = new UserDto();
				List<Group> groups = groupDao.getGroupsByUserId(user.getId());
				userDto.setUser(user);
				userDto.setGroups(groups);
				usersDto.add(userDto);
			}
		} catch (Exception ex) {
			logger.error("Exception:", ex);
			return ResponseEntity.badRequest().body("Error occured while retrieving users");
		}
		return usersDto;
	}

	@Transactional
	public ResponseEntity<String> deleteUserByUserId(String userId) {

		Users user = usersDao.getUserByUserId(userId);
		if (user == null) {
			logger.error("No User found in DB against user ID: [{}]", userId);
			return ResponseEntity.badRequest().body("Error occured while deleting user");
		}

		String errMsg = "Deletion NOT allowed, user is attached with checklist(s): ";
		List<String> checklistsName = new ArrayList<String>();

		List<ChecklistApproverUser> userChecklists = checklistApproverDao.getChecklistApproverUsersByUserId(userId);
		for (ChecklistApproverUser AU : userChecklists) {
			if (!checklistsName.contains(AU.getChecklist().getName())) {
				checklistsName.add(AU.getChecklist().getName());
			}
		}

		List<UserGroup> userGroups = userGroupDao.getUserGroupsByUser(user);
		for (UserGroup userGroup : userGroups) {

			List<ChecklistApproverGroup> userGroupChecklists = checklistApproverDao
					.getChecklistApproverGroupsByGroupId(userGroup.getGroup().getId());

			for (ChecklistApproverGroup AG : userGroupChecklists) {
				if (!checklistsName.contains(AG.getChecklist().getName())) {
					checklistsName.add(AG.getChecklist().getName());
				}
			}
		}

		if (checklistsName != null && !checklistsName.isEmpty()) {

			for (int i = 0; i < checklistsName.size(); i++) {

				errMsg += checklistsName.get(i);
				if (i < checklistsName.size() - 1) {
					errMsg += ",";
				}
			}
			logger.error("[user ID: {}] - [{}]", userId, errMsg);
			return ResponseEntity.badRequest().body(errMsg);
		}

		if (!userGroupDao.deleteUserGroupByUserId(userId)) {
			logger.error("Error occured while deleting from tbl_user_group against User ID: [{}]", userId);
			return ResponseEntity.badRequest().body("Error occured while deleting user");
		}

		return usersDao.deleteUserByUserId(userId);
	}

	public Object getUserByUserId(String userId) {
		UserDto userDto = new UserDto();
		Users user = new Users();
		List<Group> groups = new ArrayList<Group>();

		try {
			user = usersDao.getUserByUserId(userId);
			groups = groupDao.getGroupsByUserId(userId);
			userDto.setGroups(groups);
			userDto.setUser(user);
		} catch (Exception ex) {
			logger.error("[UsersService] - [getUserByUserId] - [Error occured while retrieving user]");
			return ResponseEntity.badRequest().body("Error occured while retrieving user");
		}
		return userDto;
	}

	public Object authenticateUser(Users user) {
		WebUtil utils= new WebUtil();
		utils.getClientIp();
		UserLoginDto userLoginResponse = new UserLoginDto();
		EmailValidator validator = new EmailValidator();
		
		try {
			String userEmail = user.getEmail().toLowerCase();
			boolean valid = validator.isValid(userEmail, null);
			if (!valid) {
				return ResponseEntity.badRequest().body("Invalid Email");
			}
			user.setEmail(userEmail);
			Users authenticatedUser = usersDao.authenticateUser(user);
			if (authenticatedUser == null) {
				return ResponseEntity.badRequest().body("Authentication Failed!");
			}
			if (authenticatedUser.getStatus().equalsIgnoreCase(Constants.USER_STATUS_INACTIVE)) {
				return ResponseEntity.badRequest().body("Authentication Failed!");
			}
			userLoginResponse.setUser(authenticatedUser);

			AccessDto acc = new AccessDto();
			acc.setAdd(0);
			acc.setEdit(0);
			acc.setDelete(0);
			acc.setRead(0);

			userLoginResponse.setHierachyBuilder(acc);
			userLoginResponse.setAssetBuilder(acc);
			userLoginResponse.setChecklistBuilder(acc);
			userLoginResponse.setDataCollector(acc);
			userLoginResponse.setDataVisualization(acc);
			userLoginResponse.setUserManagement(acc);

			List<RoleResource> roleResources = roleResourceDao.getRoleResourcesByRole(authenticatedUser.getUserRole());

			for (RoleResource gr : roleResources) {

				if (gr.getResource().getResourceName().equalsIgnoreCase(Constants.RESOURCE_HIERARCHY_BUILDER)) {

					AccessDto prevRec = userLoginResponse.getHierachyBuilder();
					AccessDto access = new AccessDto();

					access.setAdd((gr.getAdd() == 1) ? 1 : prevRec.getAdd());
					access.setDelete((gr.getDelete() == 1) ? 1 : prevRec.getDelete());
					access.setEdit((gr.getEdit() == 1) ? 1 : prevRec.getEdit());
					access.setRead((gr.getRead() == 1) ? 1 : prevRec.getRead());

					userLoginResponse.setHierachyBuilder(access);
				}
				if (gr.getResource().getResourceName().equalsIgnoreCase(Constants.RESOURCE_ASSET_BUILDER)) {

					AccessDto prevRec = userLoginResponse.getAssetBuilder();
					AccessDto access = new AccessDto();

					access.setAdd((gr.getAdd() == 1) ? 1 : prevRec.getAdd());
					access.setDelete((gr.getDelete() == 1) ? 1 : prevRec.getDelete());
					access.setEdit((gr.getEdit() == 1) ? 1 : prevRec.getEdit());
					access.setRead((gr.getRead() == 1) ? 1 : prevRec.getRead());

					userLoginResponse.setAssetBuilder(access);
				}
				if (gr.getResource().getResourceName().equalsIgnoreCase(Constants.RESOURCE_CHECKLIST_BUILDER)) {

					AccessDto prevRec = userLoginResponse.getChecklistBuilder();
					AccessDto access = new AccessDto();

					access.setAdd((gr.getAdd() == 1) ? 1 : prevRec.getAdd());
					access.setDelete((gr.getDelete() == 1) ? 1 : prevRec.getDelete());
					access.setEdit((gr.getEdit() == 1) ? 1 : prevRec.getEdit());
					access.setRead((gr.getRead() == 1) ? 1 : prevRec.getRead());

					userLoginResponse.setChecklistBuilder(access);
				}
				if (gr.getResource().getResourceName().equalsIgnoreCase(Constants.RESOURCE_DATA_COLLECTOR)) {

					AccessDto prevRec = userLoginResponse.getDataCollector();
					AccessDto access = new AccessDto();

					access.setAdd((gr.getAdd() == 1) ? 1 : prevRec.getAdd());
					access.setDelete((gr.getDelete() == 1) ? 1 : prevRec.getDelete());
					access.setEdit((gr.getEdit() == 1) ? 1 : prevRec.getEdit());
					access.setRead((gr.getRead() == 1) ? 1 : prevRec.getRead());

					userLoginResponse.setDataCollector(access);
				}
				if (gr.getResource().getResourceName().equalsIgnoreCase(Constants.RESOURCE_DATA_VISUALIZATION)) {

					AccessDto prevRec = userLoginResponse.getDataVisualization();
					AccessDto access = new AccessDto();

					access.setAdd((gr.getAdd() == 1) ? 1 : prevRec.getAdd());
					access.setDelete((gr.getDelete() == 1) ? 1 : prevRec.getDelete());
					access.setEdit((gr.getEdit() == 1) ? 1 : prevRec.getEdit());
					access.setRead((gr.getRead() == 1) ? 1 : prevRec.getRead());

					userLoginResponse.setDataVisualization(access);
				}
				if (gr.getResource().getResourceName().equalsIgnoreCase(Constants.RESOURCE_USER_MANAGEMENT)) {

					AccessDto prevRec = userLoginResponse.getUserManagement();
					AccessDto access = new AccessDto();

					access.setAdd((gr.getAdd() == 1) ? 1 : prevRec.getAdd());
					access.setDelete((gr.getDelete() == 1) ? 1 : prevRec.getDelete());
					access.setEdit((gr.getEdit() == 1) ? 1 : prevRec.getEdit());
					access.setRead((gr.getRead() == 1) ? 1 : prevRec.getRead());

					userLoginResponse.setUserManagement(access);
				}
			}
		} catch (Exception ex) {
			logger.error("[UsersService] - [authenticateUser] - [User authentication failed!]");
			return ResponseEntity.badRequest().body("User authentication failed!");
		}
		return userLoginResponse;
	}

	public Object updatePassword(UpdatePasswordDto updatePassword) {

		if (!updatePassword.getPassword().equals(updatePassword.getConfirmPassword())) {
			return ResponseEntity.badRequest().body("Password mismatch!");
		}

		Users updatedUser = usersDao.updatePassword(updatePassword);
		if (updatedUser != null) {
			return ResponseEntity.ok().body("Password updated successfully");
		}
		return ResponseEntity.badRequest().body("Password updation failed");
	}
}
