package com.engro.paperlessbackend.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dto.UpdatePasswordDto;
import com.engro.paperlessbackend.entities.Role;
import com.engro.paperlessbackend.entities.Section;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.repositories.UsersRepository;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class UsersDao {

	private static Logger logger = LoggerFactory.getLogger(UsersDao.class);

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	RoleDao roleDao;

	@Transactional
	public Users addNewUser(Users user) {
		Users newUser = null;
		try {
			newUser = usersRepository.save(user);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return newUser;
	}

	public List<Users> getUsersByGroupId(int groupId) {
		List<Users> users = null;
		try {
			users = usersRepository.getUsersByGroupId(groupId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return users;
	}

	@Transactional
	public Users updateUser(Users user) {
		Users updatedUser = null;
		try {
			Optional<Users> currUser = usersRepository.findById(user.getId());
			if (currUser.isPresent()) {
				Users usr = currUser.get();
				usr.setUserName(user.getUserName());
				usr.setPassword(user.getPassword());
				usr.setPasswordUpdatedAt(user.getPasswordUpdatedAt());
				usr.setDivision(user.getDivision());
				usr.setDesignation(user.getDesignation());
				usr.setSection(user.getSection());
				usr.setEmail(user.getEmail());
				usr.setPhoneNumber(user.getPhoneNumber());
				usr.setAddress(user.getAddress());
				usr.setLanguage(user.getLanguage());

				if (user.getUserRole() != null) {
					if (user.getUserRole().getRoleId() != null) {
						usr.setUserRole(roleDao.getRoleByRoleId(user.getUserRole().getRoleId()));
					} else {
						usr.setUserRole(null);
					}
				} else {
					usr.setUserRole(null);
				}
				updatedUser = usersRepository.save(usr);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return updatedUser;
	}

	public List<Users> getAllUsers() {
		List<Users> users = null;
		try {
			users = usersRepository.findByStatusOrderByUserNameAsc(Constants.USER_STATUS_ACTIVE);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return users;
	}

	public ResponseEntity<String> deleteUserByUserId(String userId) {
		try {
			Optional<Users> currUser = usersRepository.findById(userId);
			if (currUser.isPresent()) {
				Users usr = currUser.get();
				usr.setStatus(Constants.USER_STATUS_INACTIVE);
				usr.setUserRole(null);
//				usr.setEmail(null);
				usr.setSection(null);
				usr.setDateInactive(new Timestamp(System.currentTimeMillis()));
				usersRepository.save(usr);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while deleting user");
		}
		return ResponseEntity.ok().body("User deleted successfully");
	}

	public Users getUserByUserId(String userId) {
		try {
			Optional<Users> currUser = usersRepository.findById(userId);
			logger.debug("[Successful]");
			if (currUser.isPresent()) {
				return currUser.get();
			}
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return null;
	}

	public List<Users> getUserByEmail(String email) {
		List<Users> users = null;
		try {
			users = usersRepository.findByEmail(email);
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return users;
	}

	public List<Users> getExistingUsersWithProvidedEmail(Users user) {
		List<Users> users = null;
		try {
			users = usersRepository.findByEmailAndIdNot(user.getEmail(), user.getId());
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return users;
	}

	public boolean isUserAlreadyExists(String id) {
		try {
			Optional<Users> user = usersRepository.findById(id);
			if (user.isPresent()) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return false;
	}

//	public Users authenticateUser(Users user) {
//		List<Users> users = null;
//		try {
//			users = usersRepository.findByIdAndPassword(user.getId(), user.getPassword());
//			if (users != null && !users.isEmpty()) {
//				return users.get(0);
//			}
//		} catch (Exception ex) {
//			logger.error("[EXCEPTION] - [{}]", ex);
//		}
//		return null;
//	}

	public Users authenticateUser(Users user) {
		List<Users> users = null;
		try {
			users = usersRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
			if (users != null && !users.isEmpty()) {
				return users.get(0);
			}
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return null;
	}

	@Transactional
	public boolean deleteUsersBySectionId(int sectionId) {
		try {
			usersRepository.deleteUsersBySectionId(new Timestamp(System.currentTimeMillis()), sectionId);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
			return false;
		}
		return true;
	}

	public List<Users> getUsersBySectionId(Section section) {
		List<Users> users = null;
		try {
			users = usersRepository.findBySectionAndStatus(section, Constants.USER_STATUS_ACTIVE);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return users;
	}

	public List<Users> getUsersByRole(Role role) {
		List<Users> users = null;
		try {
			users = usersRepository.findByUserRole(role);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return users;
	}

	@Transactional
	public Users updatePassword(UpdatePasswordDto updatePassword) {
		Users updatedUser = null;
		try {
			Optional<Users> currUser = usersRepository.findById(updatePassword.getId());
			if (currUser.isPresent()) {
				Users usr = currUser.get();
				usr.setPassword(updatePassword.getPassword());
				usr.setPasswordUpdatedAt(System.currentTimeMillis());

				updatedUser = usersRepository.save(usr);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return updatedUser;
	}
}
