package com.engro.paperlessbackend.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.repositories.GroupRepository;

@Component
public class GroupDao {

	private static Logger logger = LoggerFactory.getLogger(GroupDao.class);
	
	@Autowired
	GroupRepository groupRepository;
	
	public Group addNewGroup(Group group) {
		Group newGroup = null;
		try {
			newGroup = groupRepository.save(group);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return newGroup;
	}

	public Group updateGroup(Group group) {
		Group savedGroup = null;
		try {
			Optional<Group> currGroup = groupRepository.findById(group.getId());
			if(currGroup.isPresent()) {
				Group gr = currGroup.get();
				gr.setName(group.getName());
				gr.setDescription(group.getDescription());
				savedGroup = groupRepository.save(gr);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedGroup;
	}

	public List<Group> getAllGroups() {
		List<Group> groups = null;
		try {
			groups = groupRepository.findAllByOrderByNameAsc();
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return groups;
	}

	public boolean deleteGroupByGroupId(Integer id) {
		try {
			groupRepository.deleteById(id);
			logger.info("[Successful]");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	public Group getGroupByGroupId(int groupId) {
		try {
			Optional<Group> group = groupRepository.findById(groupId);
			logger.info("[Successful]");
			if(group.isPresent()) {
				return group.get();
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return null;
	}

	public boolean isGroupNameAleardyExists(Group group) {
		try {
			List<Group> groups = groupRepository.isGroupNameAleardyExists(group.getId(), group.getName());
			if(!groups.isEmpty()) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	public List<Group> getGroupsByUserId(String userId) {
		List<Group> groups = null;
		try {
			groups = groupRepository.getGroupsByUserId(userId);
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return groups;
	}
}
