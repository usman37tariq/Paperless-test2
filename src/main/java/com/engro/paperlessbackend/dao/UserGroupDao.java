package com.engro.paperlessbackend.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.UserGroup;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.repositories.UserGroupRepository;

@Component
public class UserGroupDao {

	private static Logger logger = LoggerFactory.getLogger(UserGroupDao.class);

	@Autowired
	UserGroupRepository userGroupRepository;

	public List<UserGroup> addUserGroups(List<UserGroup> list) {
		List<UserGroup> userGroup = null;
		try {
			userGroup = userGroupRepository.saveAll(list);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return userGroup;
	}

	public boolean deleteUserGroupByGroupId(int groupId) {
		try {
			userGroupRepository.deleteByGroupId(groupId);
			logger.info("[Successful]");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	@Transactional
	public boolean deleteUserGroupByUserId(String userId) {
		try {
			userGroupRepository.deleteByUserId(userId);
			logger.info("[Successful]");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	public List<UserGroup> getUserGroupsByUser(Users user) {
		List<UserGroup> userGroups = null;
		try {
			userGroups = userGroupRepository.findByUser(user);
			logger.info("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return userGroups;
	}

	public List<UserGroup> getUserGroupByGroup(Group group) {
		List<UserGroup> groupUsers = null;
		try {
			groupUsers = userGroupRepository.findByGroup(group);
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
			e.printStackTrace();
		}
		return groupUsers;
	}
}
