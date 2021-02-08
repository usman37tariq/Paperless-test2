package com.engro.paperlessbackend.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dao.ChecklistApproverDao;
import com.engro.paperlessbackend.dao.ChecklistPartiallySavedDataDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleClaimedDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleDao;
import com.engro.paperlessbackend.dao.GroupDao;
import com.engro.paperlessbackend.dao.ResourceDao;
import com.engro.paperlessbackend.dao.UserGroupDao;
import com.engro.paperlessbackend.dao.UsersDao;
import com.engro.paperlessbackend.dao.WorkflowLevelDao;
import com.engro.paperlessbackend.dto.GroupDto;
import com.engro.paperlessbackend.entities.ChecklistApproverGroup;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistScheduleClaimed;
import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.UserGroup;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.entities.WorkflowLevel;
import com.engro.paperlessbackend.utils.Constants;
import com.engro.paperlessbackend.utils.GroupUtil;

@Component
public class GroupService {

	private static Logger logger = LoggerFactory.getLogger(GroupService.class);

	@Autowired
	GroupDao groupDao;

	@Autowired
	UsersDao usersDao;

	@Autowired
	ResourceDao resourceDao;

	@Autowired
	UserGroupDao userGroupDao;

	@Autowired
	ChecklistApproverDao checklistApproverDao;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	WorkflowLevelDao workFlowLevelDao;

	@Autowired
	DataCollectorService dataCollectorService;

	@Autowired
	GroupUtil groupUtil;

	@Autowired
	ChecklistScheduleDao checklistScheduleDao;

	@Autowired
	ChecklistScheduleClaimedDao checklistScheduleClaimedDao;

	@Autowired
	ChecklistPartiallySavedDataDao checklistPartiallySavedDataDao;

	@Transactional
	public Object addNewGroup(GroupDto groupDto) {
		Group newGroup = new Group();
		GroupDto newGroupDto = new GroupDto();
		try {
			Group group = groupDto.getGroup();
			if (groupDao.isGroupNameAleardyExists(group)) {
				return ResponseEntity.badRequest().body("Group with given name already exists");
			}

			List<Users> users = groupDto.getUsers();
			newGroup = groupDao.addNewGroup(group);

			List<UserGroup> groupUsers = new ArrayList<UserGroup>();

			if (users != null) {
				for (int i = 0; i < users.size(); i++) {
					UserGroup userGroup = new UserGroup();
					userGroup.setGroup(newGroup);
					userGroup.setUser(users.get(i));
					groupUsers.add(userGroup);
				}
			}
			userGroupDao.addUserGroups(groupUsers);

			newGroupDto = getGroupDetailsByGroupId(newGroup.getId());
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occred while adding new group");
		}
		return newGroupDto;
	}

	@Transactional
	public Object updateGroup(Group group) {
		Group savedGroup = new Group();
		try {
			if (groupDao.isGroupNameAleardyExists(group)) {
				return ResponseEntity.badRequest().body("Group with given name already exists");
			}
			savedGroup = groupDao.updateGroup(group);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while updating group");
		}
		return savedGroup;
	}

	public Object getAllGroups() {
		List<GroupDto> groupsDto = new ArrayList<GroupDto>();
		try {
			List<Group> groups = groupDao.getAllGroups();
			for (Group group : groups) {
				GroupDto groupDto = new GroupDto();

				List<Users> users = usersDao.getUsersByGroupId(group.getId());
				groupDto.setGroup(group);
				groupDto.setUsers(users);

				groupsDto.add(groupDto);
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while retrieving groups");
		}
		return groupsDto;
	}

	@Transactional
	public Object updateGroupDetails(GroupDto groupDto) {

		GroupDto updatedGroup = new GroupDto();

		try {
			Group group = groupDto.getGroup();
			List<Users> users = groupDto.getUsers();

			if (groupDao.isGroupNameAleardyExists(group)) {
				return ResponseEntity.badRequest().body("Group with given name already exists");
			}

			List<UserGroup> existingGroupUsers = userGroupDao.getUserGroupByGroup(group);

			/**
			 * 
			 * TODO:
			 */

			List<String> existingGroupUserIds = new ArrayList<String>();
			List<String> groupUserIdsFromUI = new ArrayList<String>();

			if (existingGroupUsers != null && users != null) {

				boolean isUsersUpdated = false;
				
				for (UserGroup GU : existingGroupUsers) {
					existingGroupUserIds.add(GU.getUser().getId());
				}

				if ((existingGroupUsers.size() == users.size()) && existingGroupUsers.size() > 0 && users.size() > 0) {

					for (Users user : users) {

						if (!existingGroupUserIds.contains(user.getId())) {
							isUsersUpdated = true;
							break;
						}
					}
				} else {
					isUsersUpdated = true;
				}

				if (isUsersUpdated) {

					WorkflowLevel workFlowLevelOne = workFlowLevelDao
							.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ONE_ID);
					List<ChecklistApproverGroup> checklistsAssignedToGroup = checklistApproverDao
							.getChecklistApproverGroupsByGroupIdAndWorkFlowLevelId(group, workFlowLevelOne);

					if (checklistsAssignedToGroup != null && !checklistsAssignedToGroup.isEmpty()) {

						List<String> activeChecklists = new ArrayList<>();

						for (Users user : users) {
							groupUserIdsFromUI.add(user.getId());
						}

						List<String> newAddedGroupMemebersIds = groupUtil
								.getListOfNewMembersAddedInGroup(existingGroupUserIds, groupUserIdsFromUI);
						List<String> removedGroupMemebersIds = groupUtil
								.getListOfMembersRemovedFromGroup(existingGroupUserIds, groupUserIdsFromUI);

						for (ChecklistApproverGroup groupChecklist : checklistsAssignedToGroup) {

							if (groupChecklist.getChecklist().getActivationStatus().equalsIgnoreCase(
									Constants.CHECKLIST_STATUS_ACTIVE) && groupChecklist.getChecklist().isManual()
									&& groupChecklist.getChecklist().getEndTime() > System.currentTimeMillis()) {

								for (String userId : removedGroupMemebersIds) {

									Users user = usersDao.getUserByUserId(userId);

									List<ChecklistSchedule> checklistSchedules = checklistScheduleDao
											.getChecklistSchedulesOfManualChecklist(groupChecklist.getChecklist());

									for (ChecklistSchedule checklistSchedule : checklistSchedules) {

										List<ChecklistScheduleClaimed> checklistSchedulesClaimed = checklistScheduleClaimedDao
												.getClaimedChecklistDataByChecklistScheduleAndUser(checklistSchedule,
														user);

										for (ChecklistScheduleClaimed scheduleClaimed : checklistSchedulesClaimed) {

											if (!checklistScheduleClaimedDao
													.deleteChecklistSchedulesClaimedByChecklistSchedule(
															scheduleClaimed.getChecklistSchedule())) {
												logger.error(
														"couldn't delete checklist schedule claimed ID [{}] of checklist ID [{}]",
														scheduleClaimed.getChecklistSchedule().getChecklistScheduleId(),
														scheduleClaimed.getChecklistSchedule().getChecklist()
																.getChecklistId());

												return ResponseEntity.badRequest()
														.body("Error occured while updating group members");
											}

											if (!checklistPartiallySavedDataDao
													.deleteSavedChecklistDataByChecklistSchedule(
															scheduleClaimed.getChecklistSchedule())) {
												logger.error(
														"couldn't delete partially saved data of checklist schedule ID [{}] of checklist ID [{}]",
														scheduleClaimed.getChecklistSchedule().getChecklistScheduleId(),
														scheduleClaimed.getChecklistSchedule().getChecklist()
																.getChecklistId());

												return ResponseEntity.badRequest()
														.body("Error occured while updating group members");
											}

											if (!checklistScheduleDao.deleteChecklistScheduleByChecklistScheduleId(
													scheduleClaimed.getChecklistSchedule().getChecklistScheduleId())) {
												logger.error(
														"couldn't delete checklist schedule ID [{}] of checklist ID [{}]",
														scheduleClaimed.getChecklistSchedule().getChecklistScheduleId(),
														scheduleClaimed.getChecklistSchedule().getChecklist()
																.getChecklistId());

												return ResponseEntity.badRequest()
														.body("Error occured while updating group members");
											}

										}
									}
								}

								for (String userId : newAddedGroupMemebersIds) {

									Users user = usersDao.getUserByUserId(userId);
									if (!dataCollectorService.addNewEnteriesForManualChecklist(
											groupChecklist.getChecklist(), user, System.currentTimeMillis())) {

										logger.error(
												"couldnot add new enteries for manual checklist with ID [{}] against user ID [{}]",
												groupChecklist.getChecklist().getChecklistId(), userId);

										return ResponseEntity.badRequest()
												.body("Error occured while updating group members");
									}
								}
							}
						}

//						for (int i = 0; i < checklistsAssignedToGroup.size(); i++) {
//							if (checklistsAssignedToGroup.get(i).getChecklist().getActivationStatus()
//									.equalsIgnoreCase(Constants.CHECKLIST_STATUS_ACTIVE) && checklists) {
//								activeChecklists.add(checklistsAssignedToGroup.get(i).getChecklist().getName());
//							}
//						}
//						String errMsg = "cannot update group, it is assigned to checklist(s), please de-activate mentioned checklist(s): ";
//						if (!activeChecklists.isEmpty()) {
//							errMsg += String.join(",", activeChecklists);
//							logger.error("[{}]", errMsg);
//							return ResponseEntity.badRequest().body(errMsg);
//						}

					}
				}
			}

			Group savedGroup = groupDao.updateGroup(group);

			if (!userGroupDao.deleteUserGroupByGroupId(group.getId())) {
				return ResponseEntity.badRequest().body("Error occured while updating group resources & users");
			}

			List<UserGroup> groupUsers = new ArrayList<UserGroup>();
			if (users != null) {
				for (int i = 0; i < users.size(); i++) {
					UserGroup userGroup = new UserGroup();
					userGroup.setGroup(group);
					userGroup.setUser(users.get(i));
					groupUsers.add(userGroup);
				}
			}
			userGroupDao.addUserGroups(groupUsers);

			updatedGroup = (GroupDto) getGroupDetailsByGroupId(group.getId());
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while updating group resources & users");
		}
		return updatedGroup;
	}

	@Transactional
	public GroupDto getGroupDetailsByGroupId(int groupId) {
		GroupDto groupDetails = new GroupDto();
		Group group = new Group();

		List<Users> users = new ArrayList<Users>();

		try {
			group = groupDao.getGroupByGroupId(groupId);
			users = usersDao.getUsersByGroupId(groupId);
			groupDetails.setGroup(group);
			groupDetails.setUsers(users);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return groupDetails;
	}

	@Transactional
	public ResponseEntity<String> deleteGroupByGroupId(int groupId) {

		List<ChecklistApproverGroup> approverGroup = checklistApproverDao.getChecklistApproverGroupsByGroupId(groupId);

		String errMsg = "Deletion not allowed, this group is being used in checklist(s): ";
		List<String> chkNames = new ArrayList<String>();
		if (approverGroup != null && !approverGroup.isEmpty()) {
			for (int i = 0; i < approverGroup.size(); i++) {
				if (!chkNames.contains(approverGroup.get(i).getChecklist().getName())) {
					chkNames.add(approverGroup.get(i).getChecklist().getName());
					errMsg += approverGroup.get(i).getChecklist().getName();
					if (i < approverGroup.size() - 1) {
						errMsg += ",";
					}
				}
			}
			logger.error("[Group ID: {}] - [{}]", groupId, errMsg);
			return ResponseEntity.badRequest().body(errMsg);
		}

		if (!userGroupDao.deleteUserGroupByGroupId(groupId)) {
			logger.error("error occured while deleting group from tbl_user_group ID:[{}]", groupId);
			return ResponseEntity.badRequest().body("Error occured while deleting group");
		}

		if (!groupDao.deleteGroupByGroupId(groupId)) {
			logger.error("error occured while deleting group from tbl_group ID:[{}]", groupId);
			return ResponseEntity.badRequest().body("Error occured while deleting group");
		}
		logger.info("[Successful]");
		return ResponseEntity.ok().body("Group Deleted Successfully");
	}

	public Object getUsersByGroupId(String id) {
		return usersDao.getUsersByGroupId(Integer.valueOf(id));
	}
}
