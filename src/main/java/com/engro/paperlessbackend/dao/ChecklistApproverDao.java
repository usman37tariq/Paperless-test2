package com.engro.paperlessbackend.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistApproverGroup;
import com.engro.paperlessbackend.entities.ChecklistApproverUser;
import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.entities.WorkflowLevel;
import com.engro.paperlessbackend.repositories.ChecklistApproverGroupRepository;
import com.engro.paperlessbackend.repositories.ChecklistApproverUserRepository;

@Component
public class ChecklistApproverDao {

	private static Logger logger = LoggerFactory.getLogger(ChecklistApproverDao.class);

	@Autowired
	ChecklistApproverUserRepository checklistApproverUserRepository;

	@Autowired
	ChecklistApproverGroupRepository checklistApproverGroupRepository;

	@Transactional
	public List<ChecklistApproverUser> addChecklistApproverUsers(List<ChecklistApproverUser> checklistApproverUsers) {
		List<ChecklistApproverUser> approvers = null;
		try {
			approvers = checklistApproverUserRepository.saveAll(checklistApproverUsers);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return approvers;
	}

	@Transactional
	public List<ChecklistApproverGroup> addChecklistApproverGroups(List<ChecklistApproverGroup> checklistApproverGroups) {
		List<ChecklistApproverGroup> approvers = null;
		try {
			approvers = checklistApproverGroupRepository.saveAll(checklistApproverGroups);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return approvers;
	}

	public List<ChecklistApproverUser> getChecklistApproverUsersByChecklistId(int checklistId) {
		List<ChecklistApproverUser> approvers = new ArrayList<ChecklistApproverUser>();
		try {
			Checklist checklist = new Checklist();
			checklist.setChecklistId(checklistId);
			approvers = checklistApproverUserRepository.findByChecklist(checklist);
			logger.info("[Successful]");
		} catch (Exception ex) {

			logger.error("[Exception] - [{}]", ex);
			return null;

		}
		return approvers;
	}

	public List<ChecklistApproverGroup> getChecklistApproverGroupsByChecklistId(int checklistId) {
		List<ChecklistApproverGroup> approvers = new ArrayList<ChecklistApproverGroup>();
		try {
			Checklist checklist = new Checklist();
			checklist.setChecklistId(checklistId);
			approvers = checklistApproverGroupRepository.findByChecklist(checklist);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return null;

		}
		return approvers;
	}

	@Transactional
	public boolean deleteFromChecklistApproverGroupByChecklistId(int checklistId) {
		Checklist checklist = new Checklist();
		try {
			checklist.setChecklistId(checklistId);
			checklistApproverGroupRepository.deleteByChecklist(checklist);
			logger.info("[ChecklistApproverDao] - [deleteFromChecklistApproverGroupByChecklistId] - [Successful]");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	@Transactional
	public boolean deleteFromChecklistApproverUserByChecklistId(int checklistId) {
		Checklist checklist = new Checklist();
		try {
			checklist.setChecklistId(checklistId);
			checklistApproverUserRepository.deleteByChecklist(checklist);
			logger.info("[ChecklistApproverDao] - [deleteFromChecklistApproverUserByChecklistId] - [Successful]");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	public List<ChecklistApproverUser> getChecklistApproverUsersByUserId(String userId) {
		List<ChecklistApproverUser> approverUsers = new ArrayList<ChecklistApproverUser>();
		try {
			Users user = new Users();
			user.setId(userId);
			approverUsers = checklistApproverUserRepository.findByUser(user);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return approverUsers;
	}

	@Transactional
	public boolean deleteChecklistApproverGroupsByGroupId(int groupId) {
		Group group = new Group();
		try {
			group.setId(groupId);
			checklistApproverGroupRepository.deleteByGroup(group);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public List<ChecklistApproverGroup> getChecklistApproverGroupsByGroupId(int groupId) {
		List<ChecklistApproverGroup> approverGroups = null;
		try {
			Group group = new Group();
			group.setId(groupId);
			approverGroups = checklistApproverGroupRepository.findByGroup(group);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return approverGroups;
	}

	public List<ChecklistApproverUser> getChecklistApproverUsersByUserIdAndByWorkFlowLevelId(Users user,
			WorkflowLevel workFlowLevel) {
		List<ChecklistApproverUser> approverUsers = new ArrayList<ChecklistApproverUser>();
		try {
			approverUsers = checklistApproverUserRepository.findByUserAndWorkflowLevel(user, workFlowLevel);
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return approverUsers;
	}

	public List<ChecklistApproverGroup> getChecklistApproverGroupsByGroupIdAndWorkFlowLevelId(Group group,
			WorkflowLevel workFlowLevel) {
		List<ChecklistApproverGroup> approverGroups = null;
		try {
			approverGroups = checklistApproverGroupRepository.findByGroupAndWorkflowLevel(group, workFlowLevel);
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return approverGroups;
	}

	public List<ChecklistApproverUser> getChecklistApproverUsersByUserIdAndByWorkFlowLevelIdGreaterThan(Users user,
			WorkflowLevel workFlowLevel) {
		List<ChecklistApproverUser> approverUsers = new ArrayList<ChecklistApproverUser>();
		try {
			approverUsers = checklistApproverUserRepository.findByUserAndWorkflowLevelGreaterThan(user, workFlowLevel);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return approverUsers;
	}

	public List<ChecklistApproverGroup> getChecklistApproverGroupsByGroupIdAndWorkFlowLevelIdGreaterThan(Group group,
			WorkflowLevel workFlowLevel) {
		List<ChecklistApproverGroup> approverGroups = new ArrayList<ChecklistApproverGroup>();
		try {
			approverGroups = checklistApproverGroupRepository.findByGroupAndWorkflowLevelGreaterThan(group,
					workFlowLevel);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return approverGroups;
	}

}
