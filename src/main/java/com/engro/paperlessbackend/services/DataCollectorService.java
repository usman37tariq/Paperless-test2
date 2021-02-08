package com.engro.paperlessbackend.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.LockModeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dao.ChecklistApproverDao;
import com.engro.paperlessbackend.dao.ChecklistDao;
import com.engro.paperlessbackend.dao.ChecklistPartiallySavedDataDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleClaimedDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleSkippedDao;
import com.engro.paperlessbackend.dao.ChecklistStateDao;
import com.engro.paperlessbackend.dao.ChecklistStateFieldDao;
import com.engro.paperlessbackend.dao.ChecklistStatusHistoryDao;
import com.engro.paperlessbackend.dao.GroupDao;
import com.engro.paperlessbackend.dao.NotificationDao;
import com.engro.paperlessbackend.dao.TagDao;
import com.engro.paperlessbackend.dao.TagDetailDao;
import com.engro.paperlessbackend.dao.TemplateStructureDao;
import com.engro.paperlessbackend.dao.UsersDao;
import com.engro.paperlessbackend.dao.WorkflowLevelDao;
import com.engro.paperlessbackend.dto.ChecklistApproveDto;
import com.engro.paperlessbackend.dto.ChecklistDataEntryDto;
import com.engro.paperlessbackend.dto.ChecklistFieldValueDto;
import com.engro.paperlessbackend.dto.ChecklistScheduleStatusDto;
import com.engro.paperlessbackend.dto.ChecklistStructureDto;
import com.engro.paperlessbackend.dto.ChecklistStructureGroupingDto;
import com.engro.paperlessbackend.dto.GetChecklistScheduleDto;
import com.engro.paperlessbackend.dto.GetChecklistStructureDto;
import com.engro.paperlessbackend.dto.NotificationCountDto;
import com.engro.paperlessbackend.dto.TagDataDto;
import com.engro.paperlessbackend.dto.UserChecklistApproveDto;
import com.engro.paperlessbackend.dto.UserChecklistDataDto;
import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistApproverGroup;
import com.engro.paperlessbackend.entities.ChecklistApproverUser;
import com.engro.paperlessbackend.entities.ChecklistPartiallySavedData;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistScheduleClaimed;
import com.engro.paperlessbackend.entities.ChecklistScheduleSkipped;
import com.engro.paperlessbackend.entities.ChecklistState;
import com.engro.paperlessbackend.entities.ChecklistStateField;
import com.engro.paperlessbackend.entities.ChecklistStatusHistory;
import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.Tag;
import com.engro.paperlessbackend.entities.TagDetail;
import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.entities.WorkflowLevel;
import com.engro.paperlessbackend.utils.ChecklistStateFieldUtil;
import com.engro.paperlessbackend.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DataCollectorService {

	private static Logger logger = LoggerFactory.getLogger(DataCollectorService.class);

	@Autowired
	ChecklistApproverDao checklistApproverDao;

	@Autowired
	GroupDao groupDao;

	@Autowired
	ChecklistScheduleDao checklistScheduleDao;

	@Autowired
	ChecklistScheduleClaimedDao checklistScheduleClaimedDao;

	@Autowired
	UsersDao usersDao;

	@Autowired
	ChecklistStatusHistoryDao checklistStatusHistoryDao;

	@Autowired
	WorkflowLevelDao workflowLevelDao;

	@Autowired
	ChecklistScheduleSkippedDao checklistScheduleSkippedDao;

	@Autowired
	TagDao tagDao;

	@Autowired
	ChecklistService checklistService;

	@Autowired
	ChecklistDao checklistDao;

	@Autowired
	TemplateStructureDao templateStructureDao;

	@Autowired
	NotificationDao notificationDao;

	@Autowired
	ChecklistPartiallySavedDataDao checklistPartiallySavedDataDao;

	@Autowired
	ChecklistStateDao checklistStateDao;

	@Autowired
	ChecklistStateFieldDao checklistStateFieldDao;

	@Autowired
	ChecklistStateFieldUtil checklistStateFieldUtil;

	@Autowired
	TagDetailDao tagDetailDao;

	public NotificationCountDto getAllNotificationCounts(String userId) {

		NotificationCountDto notificationCounts = new NotificationCountDto();

		notificationCounts.setClaimedCount(notificationDao.getMyChecklistsCount(userId));
		notificationCounts.setUnClaimedCount(notificationDao.getAssignedChecklistsCount(userId));
		notificationCounts.setApproveCount(notificationDao.getReviewChecklistsCount(userId));
		notificationCounts.setDueCount(notificationDao.getDueChecklistsCount(userId));
		notificationCounts.setOverDueCount(notificationDao.getOverdueChecklistsCount(userId));

		logger.info("[SUCCESSFUL]");
		return notificationCounts;
	}

	public UserChecklistDataDto getAllChecklistsAssignedToUser(String userId) {
		UserChecklistDataDto userChecklistDataDto = new UserChecklistDataDto();
		Users user = usersDao.getUserByUserId(userId);

		WorkflowLevel workFlowLevel = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ONE_ID);

		List<ChecklistApproverUser> userApproverChecklists = checklistApproverDao
				.getChecklistApproverUsersByUserIdAndByWorkFlowLevelId(user, workFlowLevel);
		List<Group> userGroups = groupDao.getGroupsByUserId(userId);

		List<ChecklistApproverGroup> groupApproverChecklists = new ArrayList<ChecklistApproverGroup>();

		for (Group group : userGroups) {
			List<ChecklistApproverGroup> approverGroup = new ArrayList<ChecklistApproverGroup>();
			approverGroup = checklistApproverDao.getChecklistApproverGroupsByGroupIdAndWorkFlowLevelId(group,
					workFlowLevel);
			for (ChecklistApproverGroup AG : approverGroup) {
				groupApproverChecklists.add(AG);
			}
		}

		/**
		 * 
		 * claimed, unclaimed
		 * 
		 */

		List<Integer> scheduleIds = new ArrayList<Integer>();
		List<ChecklistSchedule> claimedChecklists = new ArrayList<ChecklistSchedule>();

		List<ChecklistScheduleStatusDto> unClaimedChecklists = new ArrayList<ChecklistScheduleStatusDto>();

		for (ChecklistApproverUser AU : userApproverChecklists) {

			if (AU.getChecklist().getIsDeleted() == Constants.CHECKLIST_IS_NOT_DELETED) {

				List<ChecklistSchedule> CS = checklistScheduleDao.getChecklistSchedulesByChecklist(AU.getChecklist());

				for (ChecklistSchedule sch : CS) {
					scheduleIds.add(sch.getChecklistScheduleId());

					ChecklistStatusHistory statusHistory = checklistStatusHistoryDao
							.getLatestChecklistStatusHistoryByScheduleId(sch);

					if (AU.getChecklist().isManual() && sch.getEndTimestamp() == null && statusHistory == null) {

						if (AU.getChecklist().getStartTime() > System.currentTimeMillis()
								|| AU.getChecklist().getEndTime() <= System.currentTimeMillis()) {
							continue;
						}
						if (AU.getChecklist().getActivationStatus()
								.equalsIgnoreCase(Constants.CHECKLIST_STATUS_PENDING)) {
							continue;
						}
					}

					if (statusHistory != null) {

						if (statusHistory.getWorkflowLevel().getWorkflowLevelId() == Constants.WORKFLOW_LEVEL_ONE_ID) {

							List<ChecklistScheduleClaimed> checklistScheduleClaimed = checklistScheduleClaimedDao
									.getClaimedChecklistDataByChecklistScheduleAndUser(sch, user);

							if (!checklistScheduleClaimed.isEmpty()) {
								claimedChecklists.add(sch);
							}
						}
					} else {

						List<ChecklistScheduleClaimed> checklistScheduleClaimed = checklistScheduleClaimedDao
								.getClaimedChecklistDataByChecklistScheduleAndUser(sch, user);

						if (!checklistScheduleClaimed.isEmpty()) {
							claimedChecklists.add(sch);
						} else {
							List<ChecklistScheduleClaimed> claimedSchedules = checklistScheduleClaimedDao
									.getChecklistSchedulesBySchedule(sch);
							if (claimedSchedules == null || claimedSchedules.isEmpty()) {
								ChecklistScheduleStatusDto unclaimedChk = new ChecklistScheduleStatusDto();
								unclaimedChk.setChecklistSchedule(sch);
								unclaimedChk.setAssignedTo(user.getUserName());
								unclaimedChk.setDueTime(sch.getEndTimestamp());
								unClaimedChecklists.add(unclaimedChk);
							}
						}
					}
				}
			}
		}

		for (ChecklistApproverGroup AG : groupApproverChecklists) {

			if (AG.getChecklist().getIsDeleted() == Constants.CHECKLIST_IS_NOT_DELETED) {

				List<ChecklistSchedule> CS = checklistScheduleDao.getChecklistSchedulesByChecklist(AG.getChecklist());

				for (ChecklistSchedule sch : CS) {
					if (!scheduleIds.contains(sch.getChecklistScheduleId())) {

						ChecklistStatusHistory statusHistory = checklistStatusHistoryDao
								.getLatestChecklistStatusHistoryByScheduleId(sch);

						if (AG.getChecklist().isManual() && sch.getEndTimestamp() == null && statusHistory == null) {

							if (AG.getChecklist().getStartTime() > System.currentTimeMillis()
									|| AG.getChecklist().getEndTime() <= System.currentTimeMillis()) {
								continue;
							}
							if (AG.getChecklist().getActivationStatus()
									.equalsIgnoreCase(Constants.CHECKLIST_STATUS_PENDING)) {
								continue;
							}
						}

						if (statusHistory != null) {

							if (statusHistory.getWorkflowLevel()
									.getWorkflowLevelId() == Constants.WORKFLOW_LEVEL_ONE_ID) {

								List<ChecklistScheduleClaimed> checklistScheduleClaimed = checklistScheduleClaimedDao
										.getClaimedChecklistDataByChecklistScheduleAndUser(sch, user);

								if (!checklistScheduleClaimed.isEmpty()) {
									claimedChecklists.add(sch);
								}
							}
						} else {

							List<ChecklistScheduleClaimed> checklistScheduleClaimed = checklistScheduleClaimedDao
									.getClaimedChecklistDataByChecklistScheduleAndUser(sch, user);

							if (!checklistScheduleClaimed.isEmpty()) {
								claimedChecklists.add(sch);
							} else {

								List<ChecklistScheduleClaimed> claimedSchedules = checklistScheduleClaimedDao
										.getChecklistSchedulesBySchedule(sch);
								if (claimedSchedules == null || claimedSchedules.isEmpty()) {

									ChecklistScheduleStatusDto unclaimedChk = new ChecklistScheduleStatusDto();
									unclaimedChk.setChecklistSchedule(sch);
									unclaimedChk.setAssignedTo(AG.getGroup().getName());
									unclaimedChk.setDueTime(sch.getEndTimestamp());
									unClaimedChecklists.add(unclaimedChk);
								}
							}
						}
					}
				}
			}
		}

		/**
		 * 
		 * sorting of unclaimed checklists DESC
		 * 
		 */
		List<ChecklistScheduleStatusDto> sortedUnclaimedChecklists = unClaimedChecklists.stream()
				.sorted(Comparator.comparing(ChecklistScheduleStatusDto::getDueTime).reversed())
				.collect(Collectors.toList());

		/**
		 * 
		 * due, over-due
		 * 
		 */

		int dueCount = 0, overDueCount = 0;

		List<ChecklistScheduleStatusDto> statusListDto = new ArrayList<ChecklistScheduleStatusDto>();

		for (ChecklistSchedule ch : claimedChecklists) {
			ChecklistScheduleStatusDto st = new ChecklistScheduleStatusDto();
			ChecklistStatusHistory latestStatusHistory = checklistStatusHistoryDao
					.getLatestChecklistStatusHistoryByScheduleId(ch);
			if (latestStatusHistory != null
					&& latestStatusHistory.getWorkflowLevel().getWorkflowLevelId() == Constants.WORKFLOW_LEVEL_ONE_ID) {
				st.setIsRejected(Constants.CHECKLIST_IS_REJECTED);
			}
			st.setChecklistSchedule(ch);

			if (ch.getEndTimestamp() == null) {
				st.setIsManual(Constants.CHECKLIST_IS_MANUAL);
			} else {
				if (ch.getEndTimestamp() <= System.currentTimeMillis()) {
					st.setStatus(Constants.CLAIMED_STATUS_OVER_DUE);
					overDueCount++;
				} else {
					st.setStatus(Constants.CLAIMED_STATUS_DUE);
					dueCount++;
				}
				st.setDueTime(ch.getEndTimestamp());
			}
			statusListDto.add(st);
		}

		/**
		 * 
		 * sorting of claimed checklists ASC
		 * 
		 */
		List<ChecklistScheduleStatusDto> sortedClaimedChecklists = statusListDto.stream()
				.sorted(Comparator.comparing(ChecklistScheduleStatusDto::getDueTime)).collect(Collectors.toList());

		userChecklistDataDto.setClaimedChecklists(sortedClaimedChecklists);
		userChecklistDataDto.setUnClaimedChecklists(sortedUnclaimedChecklists);
		userChecklistDataDto.setClaimedCount(claimedChecklists.size());
		userChecklistDataDto.setUnClaimedCount(unClaimedChecklists.size());
		userChecklistDataDto.setDueCount(dueCount);
		userChecklistDataDto.setOverDueCount(overDueCount);

		return userChecklistDataDto;
	}

	public Object claimChecklistByChecklistScheduleId(int checklistScheduleId, String userId) {
		ChecklistSchedule schedule = checklistScheduleDao.getChecklistScheduleByScheduleId(checklistScheduleId);
		Users user = usersDao.getUserByUserId(userId);

		List<ChecklistScheduleClaimed> checklistSchedulesClaimed = checklistScheduleClaimedDao
				.getChecklistSchedulesBySchedule(schedule);

		if (!checklistSchedulesClaimed.isEmpty()) {
			logger.error("[Checklist is already claimed!]");
			return ResponseEntity.badRequest().body("Checklist is already claimed!");
		}

		ChecklistScheduleClaimed claimChecklist = new ChecklistScheduleClaimed();
		claimChecklist.setChecklistSchedule(schedule);
		claimChecklist.setTimestamp(System.currentTimeMillis());
		claimChecklist.setUser(user);

		ChecklistScheduleClaimed savedClaimedChecklistSchedule = checklistScheduleClaimedDao
				.addClaimedChecklistSchedule(claimChecklist);

		return savedClaimedChecklistSchedule;
	}

	@Transactional
	@Lock(LockModeType.PESSIMISTIC_READ)
	public Object skipChecklist(ChecklistStatusHistory checklistStatus) {

		try {

			ChecklistSchedule checklistSchedule = checklistScheduleDao
					.getChecklistScheduleByScheduleId(checklistStatus.getChecklistSchedule().getChecklistScheduleId());
			if (checklistSchedule == null || checklistSchedule.getChecklist() == null) {
				logger.info("Checklist schedule doesnot exist anymore, either the checklist is deleted or deactivated");
				return ResponseEntity.badRequest().body("Cannot skip, checklist is deleted/de-activated");
			}

			long timeStampInMillis = System.currentTimeMillis();
			checklistStatus.setStatusTimestamp(timeStampInMillis);

			String remarks = checklistStatus.getRemarks();

			ChecklistStatusHistory lastChecklistStatusHistory = new ChecklistStatusHistory();
			lastChecklistStatusHistory = checklistStatusHistoryDao
					.getLatestChecklistStatusHistoryByScheduleId(checklistStatus.getChecklistSchedule());

			if (lastChecklistStatusHistory != null) {
				if (lastChecklistStatusHistory.getWorkflowLevel()
						.getWorkflowLevelId() != Constants.WORKFLOW_LEVEL_ONE_ID) {
					return ResponseEntity.badRequest()
							.body("Already submitted by " + lastChecklistStatusHistory.getUser().getUserName());
				}
			}

			Checklist checklist = checklistDao
					.getChecklistByChecklistId(checklistSchedule.getChecklist().getChecklistId());

			if (checklist == null) {
				logger.error("Checklist retrieval failed");
				return ResponseEntity.badRequest().body("Checklist submission failed!");
			}

			/**
			 * 
			 * Null Data insertion in tag table in case of skip
			 * 
			 * 
			 */

			Users user = usersDao.getUserByUserId(checklistStatus.getUser().getId());

			String ORGANIZATION_ID = user.getOrganization().getOrganizationId();

			Timestamp dataTimestamp = new Timestamp(timeStampInMillis);
			Timestamp currTimestamp = new Timestamp(System.currentTimeMillis());

			ChecklistState checklistState = checklistStateDao
					.getChecklistStateById(checklistSchedule.getChecklistState().getChecklistStateId());

			List<ChecklistStateField> checklistStateFields = checklistStateFieldDao
					.getChecklistStateFieldsByChecklistState(checklistState);

			List<ChecklistStructureDto> checklistStructure = checklistStateFieldUtil
					.getChecklistStructureFromChecklistStateFields(checklistStateFields);

			for (ChecklistStructureDto str : checklistStructure) {

				String value = null;
				int templateId = str.getTemplateId();
				int fieldId = str.getTemplateFieldId();
				String fieldRemarks = null;

				String tableName = ORGANIZATION_ID + "_" + templateId + "_" + fieldId;

				TagDataDto tagData = new TagDataDto();
				tagData.setTimeStamp(currTimestamp);
				tagData.setDataTimestamp(dataTimestamp);
				tagData.setQuality(Constants.TAG_QUALITY_GOOD);
				tagData.setValue(value);
				tagData.setUserId(user.getId());
				tagData.setRemarks(fieldRemarks);
				tagData.setChecklistId(checklistSchedule.getChecklist().getChecklistId());
				tagData.setChecklistScheduleId(checklistSchedule.getChecklistScheduleId());

				if (lastChecklistStatusHistory == null) {
					if (!tagDao.insertValueIntoTagTable(tableName, tagData, str.getFieldType())) {
						logger.error("Error: checklist submission failed:");
						return ResponseEntity.badRequest().body("Error: checklist submission failed");
					}
				} else {
					if (!tagDao.updateValueInTagTable(tableName, tagData, str.getFieldType())) {

						logger.error("Error: checklist submission failed:");
						return ResponseEntity.badRequest().body("Error: checklist submission failed");
					}
				}
			}

			checklistStatus.setChecklistSchedule(checklistSchedule);

			WorkflowLevel currWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ONE_ID);
			checklistStatus.setPreviousWorkflowLevel(currWorkflowLevel);

			WorkflowLevel lvl = new WorkflowLevel();
			if (checkIfChecklistHasApprovers(checklistSchedule.getChecklist().getChecklistId())) {
				lvl = workflowLevelDao.getWorkFlowLevelById((Constants.WORKFLOW_LEVEL_TWO_ID));
			} else {
				lvl = workflowLevelDao.getWorkFlowLevelById((Constants.WORKFLOW_LEVEL_ZERO_ID));
			}

			checklistStatus.setWorkflowLevel(lvl);
			checklistStatus.setUser(user);
			checklistStatus.setRemarks(null);

			checklistStatusHistoryDao.saveChecklistStatusHistory(checklistStatus);

			ChecklistScheduleSkipped checklistScheduleSkipped = new ChecklistScheduleSkipped();

			checklistScheduleSkipped.setChecklistSchedule(checklistSchedule);
			checklistScheduleSkipped.setRemarks(checklistStatus.getRemarks());
			checklistScheduleSkipped.setSkippedTimestamp(System.currentTimeMillis());
			checklistScheduleSkipped.setRemarks(remarks);
			checklistScheduleSkipped.setUser(user);

			checklistScheduleSkippedDao.saveChecklistScheduleSkipped(checklistScheduleSkipped);

			/**
			 * 
			 * if checklist is manual then add entries in schedule and claimed table
			 * 
			 */
			if (checklist.isManual() && checklistSchedule.getEndTimestamp() == null
					&& lastChecklistStatusHistory == null && checklist.getEndTime() > System.currentTimeMillis()) {
				if (!addNewEnteriesForManualChecklist(checklist, user, timeStampInMillis)) {
					logger.error("Error occured while adding new records in scheduled and claimed tables");
					return ResponseEntity.badRequest().body("Error: checklist submission failed");
				}
			}

			if (!checklistPartiallySavedDataDao.deleteSavedChecklistDataByChecklistSchedule(checklistSchedule)) {
				logger.error("Couldn't delete partially saved data of this checklist schedule [{}]",
						checklistSchedule.getChecklistScheduleId());
				return ResponseEntity.badRequest().body("Error: checklist submission failed");
			}

		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
			return ResponseEntity.badRequest().body("Checklist submission failed!");
		}
		logger.info("Checklist Skipped Successfully");
		return ResponseEntity.ok().body("Checklist skipped!");
	}

	@Transactional
	@Lock(LockModeType.PESSIMISTIC_READ)
	public Object submitChecklistData(ChecklistDataEntryDto checklistData) {

		try {

			boolean isValuePresent = false;
			for (ChecklistFieldValueDto entry : checklistData.getChecklistFields()) {
				if (!(entry.getValue() == null) && !entry.getValue().trim().isEmpty()) {
					isValuePresent = true;
					break;
				}
			}
			if (!isValuePresent) {
				logger.error("No values are added in checklist");
				return ResponseEntity.badRequest().body("Cannot submit a blank checklist");
			}

			ChecklistSchedule checklistSch = checklistScheduleDao
					.getChecklistScheduleByScheduleId(checklistData.getChecklistSchedule().getChecklistScheduleId());
			if (checklistSch == null || checklistSch.getChecklist() == null) {
				logger.info("Checklist schedule doesnot exist anymore, either the checklist is deleted or deactivated");
				return ResponseEntity.badRequest().body("Cannot submit, checklist is deleted/de-activated");
			}

			List<ChecklistScheduleClaimed> userClaimedChecklists = checklistScheduleClaimedDao
					.getClaimedChecklistDataByChecklistScheduleAndUser(checklistData.getChecklistSchedule(),
							checklistData.getUser());
			if (userClaimedChecklists == null || userClaimedChecklists.isEmpty()) {
				logger.error("Checklist schedule [{}] received from UI is not assigned to user ID [{}]",
						checklistData.getChecklistSchedule().getChecklistScheduleId(), checklistData.getUser().getId());
				return ResponseEntity.badRequest().body("Invalid checklist, cannot submit");
			}

			ChecklistState checklistState = checklistStateDao
					.getChecklistStateById(checklistSch.getChecklistState().getChecklistStateId());

			List<ChecklistStateField> checklistStateFields = checklistStateFieldDao
					.getChecklistStateFieldsByChecklistState(checklistState);

			List<String> tagFieldIDsFromUI = new ArrayList<String>();
			for (ChecklistFieldValueDto field : checklistData.getChecklistFields()) {
				tagFieldIDsFromUI.add(Integer.toString(field.getTemplateStructure().getFieldId()));
			}

			if (checklistData.getChecklistFields().size() != checklistStateFields.size()) {
				String tags = String.join(",", tagFieldIDsFromUI);
				logger.error(
						"[Error] - [Tags received from UI [{}] are different from tags saved in DB against checklist schedule : {}]",
						tags, checklistSch.getChecklistScheduleId());
				return ResponseEntity.badRequest().body("Invalid Request, Invalid Tags");
			}

			for (int i = 0; i < checklistData.getChecklistFields().size(); i++) {
				if (checklistData.getChecklistFields().get(i).getTemplateStructure()
						.getFieldId() != checklistStateFields.get(i).getTemplateStructure().getFieldId()) {
					String tags = String.join(",", tagFieldIDsFromUI);
					logger.error(
							"[Error] - [Tags received from UI [{}] are different from tags saved in DB against checklist schedule : {}]",
							tags, checklistSch.getChecklistScheduleId());
					return ResponseEntity.badRequest().body("Invalid Request, Invalid Tags");
				}
			}

			ChecklistStatusHistory latestStatus = checklistStatusHistoryDao
					.getLatestChecklistStatusHistoryByScheduleId(checklistData.getChecklistSchedule());

			if (latestStatus != null
					&& latestStatus.getWorkflowLevel().getWorkflowLevelId() != Constants.WORKFLOW_LEVEL_ONE_ID)
				return ResponseEntity.badRequest().body("Checklist is already submitted");

			long dataTimestampInMillis = checklistData.getTimeStamp();

			Checklist checklist = checklistDao.getChecklistByChecklistId(checklistData.getChecklist().getChecklistId());
			if (checklist == null) {
				logger.error("Checklist retrieval failed");
				return ResponseEntity.badRequest().body("Checklist submission failed!");
			}

			Users user = usersDao.getUserByUserId(checklistData.getUser().getId());
			String ORGANIZATION_ID = user.getOrganization().getOrganizationId();
			List<ChecklistFieldValueDto> checklistFieldsData = checklistData.getChecklistFields();

			Timestamp dataTimestamp = new Timestamp(dataTimestampInMillis);
			Timestamp currTimestamp = new Timestamp(System.currentTimeMillis());

			for (ChecklistFieldValueDto dataEntry : checklistFieldsData) {

				TemplateStructure structure = templateStructureDao
						.getTemplateStructureByFieldId(dataEntry.getTemplateStructure().getFieldId());
				String value = dataEntry.getValue();
				int templateId = structure.getTemplateId();
				int fieldId = structure.getFieldId();
				String fieldRemarks = dataEntry.getFieldRemarks();

				String tableName = ORGANIZATION_ID + "_" + templateId + "_" + fieldId;

				TagDataDto tagData = new TagDataDto();
				tagData.setTimeStamp(currTimestamp);
				tagData.setDataTimestamp(dataTimestamp);
				tagData.setQuality(Constants.TAG_QUALITY_GOOD);
				tagData.setValue(value);
				tagData.setUserId(user.getId());
				tagData.setRemarks(fieldRemarks);
				tagData.setChecklistId(checklist.getChecklistId());
				tagData.setChecklistScheduleId(checklistData.getChecklistSchedule().getChecklistScheduleId());

				if (latestStatus == null) {
//					if (tagDao.checkIfTagDataExistByChecklistIdAndDataTimestamp(tableName, checklist.getChecklistId(),
//							dataTimestamp)) {
//						logger.error("Checklist data already exist against given timestamp");
//						return ResponseEntity.badRequest().body(
//								"Checklist data already exist against given timestamp. Please choose a different timestamp");
//					}

					if (!tagDao.insertValueIntoTagTable(tableName, tagData, structure.getFieldType())) {

						logger.error("Error: checklist submission failed:");
						return ResponseEntity.badRequest()
								.body("Error: checklist submission failed, try with a different timestamp");
					}
				} else {
//					if (tagDao.checkIfTagDataExistByScheduleIdAndDataTimeStamp(tableName,
//							checklistData.getChecklistSchedule().getChecklistScheduleId(), dataTimestamp)) {
//						logger.error("Checklist data already exist against given timestamp");
//						return ResponseEntity.badRequest().body(
//								"Checklist data already exist against given timestamp. Please choose a different timestamp");
//					}

					if (!tagDao.updateValueInTagTable(tableName, tagData, structure.getFieldType())) {

						logger.error("Error: checklist submission failed:");
						return ResponseEntity.badRequest().body("Error: checklist submission failed, try with a different timestamp");
					}
				}
			}

			WorkflowLevel dataEntryWorkflowLevel = new WorkflowLevel();
			dataEntryWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ONE_ID);

			WorkflowLevel nextWorkflowLevel = new WorkflowLevel();
			if (checkIfChecklistHasApprovers(checklistData.getChecklist().getChecklistId())) {
				nextWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_TWO_ID);
			} else {
				nextWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ZERO_ID);
			}
			ChecklistStatusHistory checklistStatusHistory = new ChecklistStatusHistory();
			ChecklistSchedule checklistSchedule = checklistScheduleDao
					.getChecklistScheduleByScheduleId(checklistData.getChecklistSchedule().getChecklistScheduleId());

			checklistStatusHistory.setUser(user);
			checklistStatusHistory.setWorkflowLevel(nextWorkflowLevel);
			checklistStatusHistory.setStatusTimestamp(System.currentTimeMillis());
			checklistStatusHistory.setChecklistSchedule(checklistSchedule);
			checklistStatusHistory.setPreviousWorkflowLevel(dataEntryWorkflowLevel);

			checklistStatusHistoryDao.saveChecklistStatusHistory(checklistStatusHistory);

			/**
			 * 
			 * if checklist is manual then add entries in schedule and claimed table
			 * 
			 */
			if (checklist.isManual() && checklistSchedule.getEndTimestamp() == null && latestStatus == null
					&& checklist.getEndTime() > System.currentTimeMillis()) {
				if (!addNewEnteriesForManualChecklist(checklist, user, checklistData.getTimeStamp())) {
					logger.error("Error occured while adding new records in scheduled and claimed tables");
					return ResponseEntity.badRequest().body("Error: checklist submission failed");
				}
			}

			if (!checklistPartiallySavedDataDao.deleteSavedChecklistDataByChecklistSchedule(checklistSchedule)) {
				logger.error("Couldn't delete partially saved data of this checklist schedule [{}]",
						checklistSchedule.getChecklistScheduleId());
				return ResponseEntity.badRequest().body("Error: checklist submission failed");
			}

			logger.info(
					"[ChecklistService] - [insertChecklistDataIntoTagTable] - [Data insertion into tag tables successful]");
		} catch (Exception ex) {
			logger.error("Exception:", ex);
			return ResponseEntity.badRequest().body("Error: checklist submission failed");
		}
		return ResponseEntity.ok().body("Checklist submitted successfully");
	}

	public boolean addNewEnteriesForManualChecklist(Checklist checklist, Users user, long timestamp) {

		try {
			ChecklistState checklistState = checklistStateDao.getLatestChecklistStateByChecklist(checklist);

			ChecklistSchedule checklistSchedule = new ChecklistSchedule();
			checklistSchedule.setChecklist(checklist);
			checklistSchedule.setStartTimestamp(timestamp);
			checklistSchedule.setEndTimestamp(Constants.MANUAL_CHECKLIST_SCHEDULE_END_DATE_NULL);
			checklistSchedule.setFrequency(checklist.getFrequency());
			checklistSchedule.setUnit(checklist.getUnit());
			checklistSchedule.setChecklistState(checklistState);

			ChecklistSchedule savedChecklistSchedule = checklistScheduleDao.addChecklistSchedule(checklistSchedule);
			if (savedChecklistSchedule == null) {
				logger.error("couldn't add new schedule");
				return false;
			}

			ChecklistScheduleClaimed checklistScheduleClaimed = new ChecklistScheduleClaimed();
			checklistScheduleClaimed.setChecklistSchedule(savedChecklistSchedule);
			checklistScheduleClaimed.setTimestamp(timestamp);
			checklistScheduleClaimed.setUser(user);

			ChecklistScheduleClaimed savedChecklistScheduleClaimed = checklistScheduleClaimedDao
					.addClaimedChecklistSchedule(checklistScheduleClaimed);
			if (savedChecklistScheduleClaimed == null) {
				logger.error("couldn't add new record to claimed checklist table");
				return false;
			}
		} catch (Exception e) {
			logger.error("Error occured while adding new records in schedule and claimed tables");
			return false;
		}
		return true;
	}

	private boolean checkIfChecklistHasApprovers(int checklistId) {
		List<ChecklistApproverGroup> approverGroups = checklistApproverDao
				.getChecklistApproverGroupsByChecklistId(checklistId);
		for (ChecklistApproverGroup AG : approverGroups) {
			if (AG.getWorkflowLevel().getWorkflowLevelId() > Constants.WORKFLOW_LEVEL_ONE_ID) {
				return true;
			}
		}

		List<ChecklistApproverUser> approverUsers = checklistApproverDao
				.getChecklistApproverUsersByChecklistId(checklistId);
		for (ChecklistApproverUser AU : approverUsers) {
			if (AU.getWorkflowLevel().getWorkflowLevelId() > Constants.WORKFLOW_LEVEL_ONE_ID) {
				return true;
			}
		}
		return false;
	}

	public List<UserChecklistApproveDto> getAllChecklistsAssignedToApproverUser(String userId) {

		List<UserChecklistApproveDto> checklistsToBeApproved = new ArrayList<UserChecklistApproveDto>();

		Users user = new Users();
		user.setId(userId);

		WorkflowLevel workFlowLevel = new WorkflowLevel();
		workFlowLevel.setWorkflowLevelId(Constants.WORKFLOW_LEVEL_ONE_ID);

		List<ChecklistApproverUser> userApproverChecklists = checklistApproverDao
				.getChecklistApproverUsersByUserIdAndByWorkFlowLevelIdGreaterThan(user, workFlowLevel);

		List<Group> userGroups = groupDao.getGroupsByUserId(userId);
		List<ChecklistApproverGroup> groupApproverChecklists = new ArrayList<ChecklistApproverGroup>();

		for (Group group : userGroups) {
			List<ChecklistApproverGroup> approverGroup = new ArrayList<ChecklistApproverGroup>();
			approverGroup = checklistApproverDao.getChecklistApproverGroupsByGroupIdAndWorkFlowLevelIdGreaterThan(group,
					workFlowLevel);

			for (ChecklistApproverGroup AG : approverGroup) {
				groupApproverChecklists.add(AG);
			}
		}

		List<Integer> scheduleIds = new ArrayList<Integer>();

		for (ChecklistApproverUser AU : userApproverChecklists) {

			if (AU.getChecklist().getIsDeleted() == Constants.CHECKLIST_IS_NOT_DELETED) {

				List<ChecklistSchedule> CS = checklistScheduleDao.getChecklistSchedulesByChecklist(AU.getChecklist());

				for (ChecklistSchedule sch : CS) {

					ChecklistStatusHistory statusHistory = checklistStatusHistoryDao
							.getLatestChecklistStatusHistoryByScheduleId(sch);

					if (statusHistory != null) {

						if (statusHistory.getWorkflowLevel().getWorkflowLevelId() == AU.getWorkflowLevel()
								.getWorkflowLevelId()) {

							UserChecklistApproveDto userChecklistApprove = new UserChecklistApproveDto();

							ChecklistScheduleSkipped latestSkip = checklistScheduleSkippedDao
									.getLatestChecklistScheduleSkipped(sch);

							if (latestSkip != null) {
								userChecklistApprove.setIsSkipped(Constants.CHECKLIST_IS_SKIPPED);
							}

							userChecklistApprove.setChecklistSchedule(sch);
							userChecklistApprove.setSubmittedBy(statusHistory.getUser());
							userChecklistApprove.setSubmittedOn(statusHistory.getStatusTimestamp());
							if (statusHistory.getWorkflowLevel().getWorkflowLevelId() < statusHistory
									.getPreviousWorkflowLevel().getWorkflowLevelId()) {
								userChecklistApprove.setIsRejected(Constants.CHECKLIST_IS_REJECTED);
							}
							if (userChecklistApprove.getChecklistSchedule().isManual()
									&& sch.getEndTimestamp() == null) {
								userChecklistApprove.setIsManual(Constants.CHECKLIST_IS_MANUAL);
							}
							scheduleIds.add(sch.getChecklistScheduleId());
							checklistsToBeApproved.add(userChecklistApprove);
						}
					}
				}
			}
		}

		for (ChecklistApproverGroup AG : groupApproverChecklists) {

			if (AG.getChecklist().getIsDeleted() == Constants.CHECKLIST_IS_NOT_DELETED) {

				List<ChecklistSchedule> CS = checklistScheduleDao.getChecklistSchedulesByChecklist(AG.getChecklist());

				for (ChecklistSchedule sch : CS) {
					if (!scheduleIds.contains(sch.getChecklistScheduleId())) {

						ChecklistStatusHistory statusHistory = checklistStatusHistoryDao
								.getLatestChecklistStatusHistoryByScheduleId(sch);
						if (statusHistory != null) {

							if (statusHistory.getWorkflowLevel().getWorkflowLevelId() == AG.getWorkflowLevel()
									.getWorkflowLevelId()) {

								UserChecklistApproveDto userChecklistApprove = new UserChecklistApproveDto();

								ChecklistScheduleSkipped latestSkip = checklistScheduleSkippedDao
										.getLatestChecklistScheduleSkipped(sch);

								if (latestSkip != null) {
									userChecklistApprove.setIsSkipped(Constants.CHECKLIST_IS_SKIPPED);
								}

								userChecklistApprove.setChecklistSchedule(sch);
								userChecklistApprove.setSubmittedBy(statusHistory.getUser());
								userChecklistApprove.setSubmittedOn(statusHistory.getStatusTimestamp());
								if (statusHistory.getWorkflowLevel().getWorkflowLevelId() < statusHistory
										.getPreviousWorkflowLevel().getWorkflowLevelId()) {
									userChecklistApprove.setIsRejected(Constants.CHECKLIST_IS_REJECTED);
								}
								if (userChecklistApprove.getChecklistSchedule().isManual()
										&& sch.getEndTimestamp() == null) {
									userChecklistApprove.setIsManual(Constants.CHECKLIST_IS_MANUAL);
								}
								checklistsToBeApproved.add(userChecklistApprove);
							}
						}
					}
				}
			}
		}
		return checklistsToBeApproved;
	}

	public Object approveChecklist(ChecklistApproveDto checklistApprove) {

		long currMillis = System.currentTimeMillis();

		ChecklistSchedule checklistSchedule = checklistApprove.getChecklistApprove().getChecklistSchedule();
		WorkflowLevel userWorkflowLevel = checklistApprove.getChecklistApprove().getWorkflowLevel();

		ChecklistStatusHistory statusHistory = checklistStatusHistoryDao
				.getLatestChecklistStatusHistoryByScheduleId(checklistSchedule);
		WorkflowLevel currWorkflowLevel = statusHistory.getWorkflowLevel();

		if (currWorkflowLevel.getWorkflowLevelId() == Constants.WORKFLOW_LEVEL_ZERO_ID) {
			logger.info("Already approved by " + statusHistory.getUser().getUserName());
			return ResponseEntity.badRequest().body("Already approved by " + statusHistory.getUser().getUserName());
		}

		ChecklistStatusHistory currStatusHistory = checklistApprove.getChecklistApprove();
		currStatusHistory.setStatusTimestamp(currMillis);
		WorkflowLevel nextWorkflowLevel = new WorkflowLevel();

		if (currWorkflowLevel.getWorkflowLevelId() == userWorkflowLevel.getWorkflowLevelId()) {

			if (currWorkflowLevel.getWorkflowLevelId() >= Constants.WORKFLOW_LEVEL_TWO_ID) {

				if (checklistApprove.getIsApproved() == 1) {

					if (checkIfChecklistHasSecondApprover(checklistApprove, currWorkflowLevel.getWorkflowLevelId())) {
						nextWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(currWorkflowLevel.getWorkflowLevelId()+1);
					} else {
						nextWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ZERO_ID);
					}
				} else {
					nextWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(currWorkflowLevel.getWorkflowLevelId()-1);
				}
			}
			/*
			if (currWorkflowLevel.getWorkflowLevelId() == Constants.WORKFLOW_LEVEL_THREE_ID) {

				if (checklistApprove.getIsApproved() == 1) {
					nextWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ZERO_ID);
				} else {
					nextWorkflowLevel = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_TWO_ID);
				}
			}*/

			currStatusHistory.setWorkflowLevel(nextWorkflowLevel);
			currStatusHistory.setPreviousWorkflowLevel(currWorkflowLevel);
			checklistStatusHistoryDao.saveChecklistStatusHistory(currStatusHistory);
		} else {
			return ResponseEntity.badRequest()
					.body(statusHistory.getUser().getUserName() + " has already reviewed the checklist!");
		}
		logger.info("Review Submitted Successfully!");
		return ResponseEntity.ok().body("Review Submitted Successfully!");
	}

	private boolean checkIfChecklistHasSecondApprover(ChecklistApproveDto checklistApprove, int currWorkFlowLevel) {
		List<ChecklistApproverUser> approverUsers = new ArrayList<ChecklistApproverUser>();
		List<ChecklistApproverGroup> approverGroups = new ArrayList<ChecklistApproverGroup>();

		approverUsers = checklistApproverDao.getChecklistApproverUsersByChecklistId(
				checklistApprove.getChecklistApprove().getChecklistSchedule().getChecklist().getChecklistId());
		approverGroups = checklistApproverDao.getChecklistApproverGroupsByChecklistId(
				checklistApprove.getChecklistApprove().getChecklistSchedule().getChecklist().getChecklistId());

		if (approverUsers != null) {

			for (ChecklistApproverUser appUser : approverUsers) {

				if (appUser.getWorkflowLevel().getWorkflowLevelId() > currWorkFlowLevel) {
					return true;
				}
			}
		}
		if (approverGroups != null) {

			for (ChecklistApproverGroup appGroup : approverGroups) {

				if (appGroup.getWorkflowLevel().getWorkflowLevelId() > currWorkFlowLevel) {
					return true;
				}
			}
		}
		return false;
	}

	@Transactional
	public ResponseEntity<String> saveChecklistData(ChecklistDataEntryDto data) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			String dataToSave = mapper.writeValueAsString(data);
			ChecklistDataEntryDto checklistData = data;

//			boolean isValuePresent = false;
//
//			for (ChecklistFieldValueDto entry : checklistData.getChecklistFields()) {
//				if ((!(entry.getValue() == null) && !entry.getValue().trim().isEmpty())
//						|| (!(entry.getFieldRemarks() == null) && !entry.getFieldRemarks().trim().isEmpty())) {
//					isValuePresent = true;
//					break;
//				}
//			}
//			if (!isValuePresent) {
//				logger.error("No values are added in checklist");
//				return ResponseEntity.badRequest().body("Cannot save a blank checklist");
//			}

			ChecklistSchedule checklistSchedule = checklistScheduleDao
					.getChecklistScheduleByScheduleId(checklistData.getChecklistSchedule().getChecklistScheduleId());
			Users user = usersDao.getUserByUserId(checklistData.getUser().getId());

			ChecklistPartiallySavedData checklistPartiallySavedData = checklistPartiallySavedDataDao
					.getSavedChecklistDataByChecklistScheduleIdAndUserId(checklistSchedule, user);

			if (checklistPartiallySavedData == null) {
				checklistPartiallySavedData = new ChecklistPartiallySavedData();
				checklistPartiallySavedData.setChecklistSchedule(checklistSchedule);
				checklistPartiallySavedData.setUser(user);
			}
			checklistPartiallySavedData.setData(dataToSave);
			checklistPartiallySavedDataDao.saveChecklistData(checklistPartiallySavedData);

			logger.info("Checklist Saved Successfully");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
			return ResponseEntity.badRequest().body("Error occured while saving checklist");
		}
		return ResponseEntity.ok().body("Checklist Saved Successfully");
	}

	public GetChecklistScheduleDto getChecklistScheduleDataByChecklistScheduleId(Integer checklistScheduleId) {

		ChecklistSchedule checklistSchedule = checklistScheduleDao
				.getChecklistScheduleByScheduleId(checklistScheduleId);

		GetChecklistStructureDto getChecklistStructureDto = (GetChecklistStructureDto) checklistService
				.getChecklistDetailsByChecklistId(checklistSchedule.getChecklist().getChecklistId());

		List<ChecklistStatusHistory> checklistScheduleStatusHistory = checklistStatusHistoryDao
				.getChecklistScheduleStatusHistory(checklistSchedule.getChecklistScheduleId());

		GetChecklistScheduleDto scheduleDto = new GetChecklistScheduleDto();

		ChecklistState checklistState = checklistStateDao
				.getChecklistStateById(checklistSchedule.getChecklistState().getChecklistStateId());

		if (checklistState == null) {

		}
		List<ChecklistStructureGroupingDto> checklistStructureFromChecklistState = new ArrayList<>();
		List<ChecklistStateField> checklistStateFields = checklistStateFieldDao
				.getChecklistStateFieldsByChecklistState(checklistState);

		List<ChecklistStructureDto> checklistStructure = checklistStateFieldUtil
				.getChecklistStructureFromChecklistStateFields(checklistStateFields);
		checklistStructureFromChecklistState = checklistService.groupingChecklistStructureByAsset(checklistStructure);

		scheduleDto.setChecklist(getChecklistStructureDto.getChecklist());
		scheduleDto.setChecklistSchedule(checklistSchedule);
		scheduleDto.setChecklistStructure(checklistStructureFromChecklistState);

		ChecklistPartiallySavedData checklistPartiallySavedData = checklistPartiallySavedDataDao
				.getSavedChecklistDataByChecklistSchedule(checklistSchedule);

		if (checklistPartiallySavedData != null) {
			ObjectMapper mapper = new ObjectMapper();
			ChecklistDataEntryDto checklistData;

			JsonNode dataJSON;
			try {
				dataJSON = mapper.readTree(checklistPartiallySavedData.getData());
				checklistData = mapper.treeToValue(dataJSON, ChecklistDataEntryDto.class);

				for (ChecklistStructureGroupingDto checklistStruct : scheduleDto.getChecklistStructure()) {
					for (ChecklistStructureDto checklistStructureDto : checklistStruct.getStructure()) {
						ChecklistFieldValueDto fieldValueDto = getValuesFromDataEntryDTOlist(
								checklistData.getChecklistFields(), checklistStructureDto.getTemplateFieldId());
						if (fieldValueDto.getValue() == null || !Constants.isNumeric(fieldValueDto.getValue())) {
							checklistStructureDto.setValue(fieldValueDto.getValue());
						} else {
							checklistStructureDto.setValue(Double.parseDouble(fieldValueDto.getValue()));
						}
						checklistStructureDto.setRemarks(fieldValueDto.getFieldRemarks());
					}
				}

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			if (checklistScheduleStatusHistory != null && !checklistScheduleStatusHistory.isEmpty()) {
				scheduleDto = getChecklistScheduleDataFromTagTables(scheduleDto, checklistSchedule);
			}
		}

		scheduleDto.setChecklistScheduleStatusHistory(checklistScheduleStatusHistory);

		if (!checklistScheduleStatusHistory.isEmpty()) {
			scheduleDto.setWorkflowLevel(
					checklistScheduleStatusHistory.get(checklistScheduleStatusHistory.size() - 1).getWorkflowLevel());
			scheduleDto.setRemarks(
					checklistScheduleStatusHistory.get(checklistScheduleStatusHistory.size() - 1).getRemarks());

		}

		return scheduleDto;
	}

	private GetChecklistScheduleDto getChecklistScheduleDataFromTagTables(GetChecklistScheduleDto scheduleDto,
			ChecklistSchedule checklistSchedule) {

		long data_timestamp = 0L;

		for (ChecklistStructureGroupingDto checklistStructure : scheduleDto.getChecklistStructure()) {
			for (ChecklistStructureDto checklistStructureDto : checklistStructure.getStructure()) {
				List<TagDetail> tagDetail = tagDetailDao
						.getTagDetailByField(checklistStructureDto.getTemplateFieldId());
				if (tagDetail != null && !tagDetail.isEmpty()) {
					List<Tag> dataRows = tagDao.getTagDataAllFields(tagDetail.get(0).getTagId(),
							checklistSchedule.getChecklistScheduleId());

					if (dataRows.isEmpty()) {
						checklistStructureDto.setValue(null);
						checklistStructureDto.setRemarks(null);
					} else {
						checklistStructureDto.setValue(dataRows.get(0).getValue());
						checklistStructureDto.setRemarks(dataRows.get(0).getRemarks());
						if (data_timestamp == 0) {
							data_timestamp = dataRows.get(0).getDataEntryTimestamp().getTime();
						}
					}
				}
			}
		}
		scheduleDto.setDataEntryTimestamp(data_timestamp);

		return scheduleDto;
	}

	private ChecklistFieldValueDto getValuesFromDataEntryDTOlist(List<ChecklistFieldValueDto> dtoList, int fieldId) {
		for (ChecklistFieldValueDto dto : dtoList) {
			if (dto.getTemplateStructure().getFieldId() == fieldId)
				return dto;
		}
		return new ChecklistFieldValueDto();
	}
}
