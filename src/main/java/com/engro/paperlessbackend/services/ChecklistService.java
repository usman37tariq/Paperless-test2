package com.engro.paperlessbackend.services;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dao.ChecklistApproverDao;
import com.engro.paperlessbackend.dao.ChecklistDao;
import com.engro.paperlessbackend.dao.ChecklistPartiallySavedDataDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleClaimedDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleDao;
import com.engro.paperlessbackend.dao.ChecklistStateDao;
import com.engro.paperlessbackend.dao.ChecklistStateFieldDao;
import com.engro.paperlessbackend.dao.ChecklistStructureDao;
import com.engro.paperlessbackend.dao.GroupDao;
import com.engro.paperlessbackend.dao.TagDao;
import com.engro.paperlessbackend.dao.TagDetailDao;
import com.engro.paperlessbackend.dao.TemplateDao;
import com.engro.paperlessbackend.dao.TemplateStructureDao;
import com.engro.paperlessbackend.dao.UserGroupDao;
import com.engro.paperlessbackend.dao.UsersDao;
import com.engro.paperlessbackend.dao.WorkflowLevelDao;
import com.engro.paperlessbackend.dto.ChecklistDto;
import com.engro.paperlessbackend.dto.ChecklistStructureDto;
import com.engro.paperlessbackend.dto.ChecklistStructureGroupingDto;
import com.engro.paperlessbackend.dto.ChecklistTemplateAdditionDto;
import com.engro.paperlessbackend.dto.GetAllChecklistsDto;
import com.engro.paperlessbackend.dto.GetChecklistStructureDto;
import com.engro.paperlessbackend.dto.TemplateDto;
import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistApproverGroup;
import com.engro.paperlessbackend.entities.ChecklistApproverUser;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistScheduleClaimed;
import com.engro.paperlessbackend.entities.ChecklistState;
import com.engro.paperlessbackend.entities.ChecklistStateField;
import com.engro.paperlessbackend.entities.ChecklistStructure;
import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.Organization;
import com.engro.paperlessbackend.entities.TagDetail;
import com.engro.paperlessbackend.entities.Template;
import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.entities.WorkflowLevel;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class ChecklistService {

	private static Logger logger = LoggerFactory.getLogger(ChecklistService.class);

	@Autowired
	ChecklistDao checklistDao;

	@Autowired
	ChecklistStructureDao checklistStructureDao;

	@Autowired
	TemplateStructureDao templateStructureDao;

	@Autowired
	TemplateDao templateDao;

	@Autowired
	ChecklistApproverDao checklistApproverDao;

	@Autowired
	GroupDao groupDao;

	@Autowired
	UsersDao usersDao;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	TagDao tagDao;

	@Autowired
	TagDetailDao tagDetailDao;

	@Autowired
	WorkflowLevelDao workflowLevelDao;

	@Autowired
	ChecklistScheduleDao checklistScheduleDao;

	@Autowired
	UserGroupDao userGroupDao;

	@Autowired
	ChecklistScheduleClaimedDao checklistScheduleClaimedDao;

	@Autowired
	ChecklistStateDao checklistStateDao;

	@Autowired
	ChecklistStateFieldDao checklistStateFieldDao;

	@Autowired
	ChecklistPartiallySavedDataDao checklistPartiallySavedDataDao;

	@Transactional
	public Object addChecklist(Checklist checklist) {
		if (checklist.getName().isEmpty()) {
			logger.error("checklist name is empty!");
			return ResponseEntity.badRequest().body("Checklist name cannot be empty!");
		}
		checklist.setActivationStatus(Constants.CHECKLIST_STATUS_PENDING);
		checklist.setIsDeleted(Constants.CHECKLIST_IS_NOT_DELETED);

		return checklistDao.addChecklist(checklist);
	}

	@Transactional
	public Object addAssetTemplateToChecklist(ChecklistTemplateAdditionDto checklistTemplate) {
		List<ChecklistStructure> savedchecklistStructure = null;
		try {
			if (checklistTemplate.getNodeId() <= 0) {
				logger.error("Node Id send by UI is either ZERO or NULL");
				return ResponseEntity.badRequest().body("Please select Location/ Asset");
			}
			if (!checklistStructureDao.deleteFromChecklistStructureByNodeIdAndChecklistId(
					checklistTemplate.getChecklistId(), checklistTemplate.getNodeId())) {

				logger.error("Error occured while adding Checklist Structure");
				return ResponseEntity.badRequest().body("Error occured while adding Checklist Structure");
			}

			TemplateDto templateDto = templateStructureDao
					.getAssetTemplateStructureByNodeId(checklistTemplate.getNodeId());

			List<TemplateStructure> templateStructure = templateDto.getTemplateStructures();
			List<ChecklistStructure> checklistStructure = new ArrayList<ChecklistStructure>();
			Checklist checklist = new Checklist();
			checklist.setChecklistId(checklistTemplate.getChecklistId());

			for (int i = 0; i < templateStructure.size(); i++) {
				ChecklistStructure checklistStructureObj = new ChecklistStructure();
				checklistStructureObj.setTemplateStructure(templateStructure.get(i));
				checklistStructureObj.setChecklist(checklist);
				checklistStructureObj.setChecklistFieldOrderId(templateStructure.get(i).getOrderId());

				checklistStructure.add(checklistStructureObj);
			}
			savedchecklistStructure = checklistStructureDao.addAssetTemplateToChecklistStructure(checklistStructure);
		} catch (Exception ex) {
			logger.error("Exception:", ex);
			return ResponseEntity.badRequest().body("Error occured while adding Checklist Structure");
		}

		return savedchecklistStructure;
	}

	public Object getAllChecklists() {
		List<Checklist> checklistsDto = null;
		try {
			List<Checklist> checklists = checklistDao.getAllChecklists();
			if (checklists != null && !checklists.isEmpty()) {
				Type listType = new TypeToken<List<GetAllChecklistsDto>>() {
				}.getType();
				checklistsDto = modelMapper.map(checklists, listType);
			}
		} catch (Exception ex) {
			logger.error("Exception:", ex);
			return ResponseEntity.badRequest().body("Error occured while retrieving checklists");
		}
		return checklistsDto;
	}

	public Object getChecklistDetailsByChecklistId(int checklistId) {
		GetChecklistStructureDto getChecklistStructureDto = new GetChecklistStructureDto();
		List<ChecklistStructureDto> checklistStructures = new ArrayList<ChecklistStructureDto>();

		try {
			Checklist checklist = checklistDao.getChecklistByChecklistId(checklistId);

			List<ChecklistApproverGroup> checklistApproverGroups = checklistApproverDao
					.getChecklistApproverGroupsByChecklistId(checklistId);
			List<ChecklistApproverUser> checklistApproverUsers = checklistApproverDao
					.getChecklistApproverUsersByChecklistId(checklistId);
			//List<WorkflowLevel> workflowLevels= workflowLevelDao.getAllWorkflowLevel();
			int max= 0;
			for (ChecklistApproverUser approver: checklistApproverUsers) {
				if (approver.getWorkflowLevel().getWorkflowLevelId()>max) {
					max= approver.getWorkflowLevel().getWorkflowLevelId();
				}
			}
			for (ChecklistApproverGroup approver: checklistApproverGroups) {
				if (approver.getWorkflowLevel().getWorkflowLevelId()>max) {
					max= approver.getWorkflowLevel().getWorkflowLevelId();
				}
			}
			checklistApproverGroups = prepareChecklistApproverGroups(checklistApproverGroups,
					max);
			checklistApproverUsers = prepareChecklistApproverUsers(checklistApproverUsers, 
					max);

			getChecklistStructureDto.setApproverGroups(checklistApproverGroups);
			getChecklistStructureDto.setApproverUsers(checklistApproverUsers);

			List<ChecklistStructure> checklistDetails = checklistStructureDao
					.getChecklistStructureByChecklist(checklist);

			getChecklistStructureDto.setChecklist(checklist);

			for (int i = 0; i < checklistDetails.size(); i++) {
				ChecklistStructureDto checklistStructureDto = new ChecklistStructureDto();
				checklistStructureDto.setChecklistFieldId(checklistDetails.get(i).getChecklistFieldId());
				checklistStructureDto.setChecklistOrderId(checklistDetails.get(i).getChecklistFieldOrderId());
				checklistStructureDto.setDescription(checklistDetails.get(i).getTemplateStructure().getDescription());
				checklistStructureDto.setFieldType(checklistDetails.get(i).getTemplateStructure().getFieldType());
				checklistStructureDto.setLowerLimit(checklistDetails.get(i).getTemplateStructure().getLowerLimit());
				checklistStructureDto.setUpperLimit(checklistDetails.get(i).getTemplateStructure().getUpperLimit());
				checklistStructureDto
						.setUnitOfMeasure(checklistDetails.get(i).getTemplateStructure().getUnitOfMeasure());
				checklistStructureDto.setTemplateId(checklistDetails.get(i).getTemplateStructure().getTemplateId());
				checklistStructureDto.setTemplateFieldId(checklistDetails.get(i).getTemplateStructure().getFieldId());
				checklistStructures.add(checklistStructureDto);
			}
			getChecklistStructureDto.setChecklistStructure(groupingChecklistStructureByAsset(checklistStructures));
		} catch (Exception ex) {
			logger.error("Exception:", ex);
			return ResponseEntity.badRequest().body("Error occured while retrieving checklist details");
		}
		return getChecklistStructureDto;
	}

	@Transactional
	public Object updateChecklistScheduling(ChecklistDto checklistDto) {
		Checklist updatedChecklist = new Checklist();
		ChecklistDto savedChecklistDto = new ChecklistDto();

		try {
			Checklist checklist = checklistDto.getChecklist();
//			if (checklist.getStartTime() > checklist.getEndTime()) {
//				return ResponseEntity.badRequest().body("Start Date is greater than End Date");
//			}

			if (!checklistApproverDao.deleteFromChecklistApproverGroupByChecklistId(checklist.getChecklistId())) {
				return ResponseEntity.badRequest().body("Error occured while updating checklist");
			}
			if (!checklistApproverDao.deleteFromChecklistApproverUserByChecklistId(checklist.getChecklistId())) {
				return ResponseEntity.badRequest().body("Error occured while updating checklist");
			}

			List<ChecklistApproverGroup> approverGroups = checklistDto.getApproverGroups();
			List<ChecklistApproverUser> approverUsers = checklistDto.getApproverUsers();

			List<ChecklistApproverGroup> AG = new ArrayList<ChecklistApproverGroup>();
			List<ChecklistApproverUser> AU = new ArrayList<ChecklistApproverUser>();
			int max= 0;
			for (int i = 0; i < approverGroups.size(); i++) {
				if (approverGroups.get(i).getGroup().getId() > 0) {
					Group gr = groupDao.getGroupByGroupId(approverGroups.get(i).getGroup().getId());
					WorkflowLevel wf = workflowLevelDao
							.getWorkFlowLevelById(approverGroups.get(i).getWorkflowLevel().getWorkflowLevelId());
					approverGroups.get(i).setGroup(gr);
					approverGroups.get(i).setWorkflowLevel(wf);
					approverGroups.get(i).setChecklist(checklist);
					AG.add(approverGroups.get(i));
					if(approverGroups.get(i).getWorkflowLevel().getWorkflowLevelId()>max) {
						max= approverGroups.get(i).getWorkflowLevel().getWorkflowLevelId();
					}
				}
			}

			for (int i = 0; i < approverUsers.size(); i++) {
				if (approverUsers.get(i).getUser().getId() != null && approverUsers.get(i).getUser().getId() != "") {
					Users user = usersDao.getUserByUserId(approverUsers.get(i).getUser().getId());
					WorkflowLevel wf = workflowLevelDao
							.getWorkFlowLevelById(approverUsers.get(i).getWorkflowLevel().getWorkflowLevelId());
					approverUsers.get(i).setUser(user);
					approverUsers.get(i).setWorkflowLevel(wf);
					approverUsers.get(i).setChecklist(checklist);
					AU.add(approverUsers.get(i));
					if(approverUsers.get(i).getWorkflowLevel().getWorkflowLevelId()>max) {
						max= approverUsers.get(i).getWorkflowLevel().getWorkflowLevelId();
					}
				}
			}

			checklistApproverDao.addChecklistApproverGroups(AG);
			checklistApproverDao.addChecklistApproverUsers(AU);

			updatedChecklist = checklistDao.updateChecklistSchedulingByChecklistId(checklistDto);
			savedChecklistDto.setChecklist(updatedChecklist);

			List<ChecklistApproverGroup> savedChecklistApproverGroups = new ArrayList<ChecklistApproverGroup>();
			List<ChecklistApproverUser> savedChecklistApproverUsers = new ArrayList<ChecklistApproverUser>();

			savedChecklistApproverGroups = checklistApproverDao
					.getChecklistApproverGroupsByChecklistId(updatedChecklist.getChecklistId());
			savedChecklistApproverUsers = checklistApproverDao
					.getChecklistApproverUsersByChecklistId(updatedChecklist.getChecklistId());
			//List<WorkflowLevel> workflowLevels= workflowLevelDao.getAllWorkflowLevel();
			savedChecklistApproverGroups = prepareChecklistApproverGroups(savedChecklistApproverGroups, max);
			savedChecklistApproverUsers = prepareChecklistApproverUsers(savedChecklistApproverUsers, max);

			savedChecklistDto.setApproverGroups(savedChecklistApproverGroups);
			savedChecklistDto.setApproverUsers(savedChecklistApproverUsers);
		} catch (Exception ex) {
			logger.error("Exception:", ex);
			return ResponseEntity.badRequest().body("Error occured while updating checklist scheduling");
		}
		return savedChecklistDto;
	}

	private List<ChecklistApproverGroup> prepareChecklistApproverGroups(
			List<ChecklistApproverGroup> savedChecklistApproverGroups, int workflowLevels) {

		List<ChecklistApproverGroup> groups = new ArrayList<ChecklistApproverGroup>();
		
		for (int i = 0; i < workflowLevels; i++) {
			ChecklistApproverGroup group = new ChecklistApproverGroup();
			boolean flag = false;

			for (ChecklistApproverGroup gr : savedChecklistApproverGroups) {
				if (gr.getWorkflowLevel().getWorkflowLevelId() == (i + 1)) {
					flag = true;
					group.setWorkflowLevel(gr.getWorkflowLevel());
					group.setChecklist(gr.getChecklist());
					group.setGroup(gr.getGroup());
					group.setId(gr.getId());
				}
			}

			if (!flag) {
				WorkflowLevel lvl = new WorkflowLevel();
				lvl.setWorkflowLevelId(i + 1);
				group.setWorkflowLevel(lvl);
				group.setGroup(new Group());
			}
			groups.add(group);
		}
		return groups;
	}

	private List<ChecklistApproverUser> prepareChecklistApproverUsers(
			List<ChecklistApproverUser> savedChecklistApproverUsers, int workflowLevels) {

		List<ChecklistApproverUser> users = new ArrayList<ChecklistApproverUser>();
		
		for (int i = 0; i < workflowLevels; i++) {
			ChecklistApproverUser user = new ChecklistApproverUser();
			boolean flag = false;

			for (ChecklistApproverUser usr : savedChecklistApproverUsers) {
				if (usr.getWorkflowLevel().getWorkflowLevelId() == (i + 1)) {
					flag = true;
					user.setWorkflowLevel(usr.getWorkflowLevel());
					user.setChecklist(usr.getChecklist());
					user.setUser(usr.getUser());
					user.setId(usr.getId());
				}
			}

			if (!flag) {
				WorkflowLevel lvl = new WorkflowLevel();
				lvl.setWorkflowLevelId(i + 1);
				user.setWorkflowLevel(lvl);
				user.setUser(new Users());
			}
			users.add(user);
		}
		return users;
	}

	List<ChecklistSchedule> createChecklistSchedules(Checklist checklist) {
		long start = checklist.getStartTime();
		long end = checklist.getEndTime();
		Calendar c = Calendar.getInstance();

		int frequency = checklist.getFrequency();

		final String MINUTE = "minute";
		final String HOUR = "hour";
		final String DAY = "day";
		final String MONTH = "month";

		Map<String, Integer> timeUnitMap = new HashMap<>();
		timeUnitMap.put(MINUTE, 1);
		timeUnitMap.put(HOUR, 60);
		timeUnitMap.put(DAY, 1440);
		timeUnitMap.put(MONTH, 1440 * 30);

		Map<String, Integer> timeUnitMapCalender = new HashMap<>();
		timeUnitMapCalender.put(MINUTE, Calendar.MINUTE);
		timeUnitMapCalender.put(HOUR, Calendar.HOUR);
		timeUnitMapCalender.put(DAY, Calendar.DATE);
		timeUnitMapCalender.put(MONTH, Calendar.MONTH);

		String timeUnit = checklist.getUnit();

		List<ChecklistSchedule> schedulesList = new ArrayList<>();

		c.setTime(new Date(start));
		c.add(timeUnitMapCalender.get(timeUnit), frequency);

		long startTimeForSchedule = start;
		long endTimeForSchedule = c.getTimeInMillis();

		while (endTimeForSchedule <= end) {

			ChecklistSchedule schedule = new ChecklistSchedule();
			schedule.setChecklist(checklist);
			schedule.setStartTimestamp(startTimeForSchedule);
			schedule.setEndTimestamp(endTimeForSchedule);
			schedule.setFrequency(checklist.getFrequency());
			schedule.setUnit(checklist.getUnit());

			if (endTimeForSchedule > System.currentTimeMillis())
				schedulesList.add(schedule);

			c.setTime(new Date(endTimeForSchedule));
			c.add(timeUnitMapCalender.get(timeUnit), frequency);

			startTimeForSchedule = endTimeForSchedule;
			endTimeForSchedule = c.getTimeInMillis();
		}

		return schedulesList;

	}

	List<ChecklistSchedule> getChecklistSchedulesInTimeRange(Checklist checklist, long rangeStart, long rangeEnd) {
		long start = checklist.getStartTime();
		long end = checklist.getEndTime();
		Calendar c = Calendar.getInstance();

		int frequency = checklist.getFrequency();

		final String MINUTE = "minute";
		final String HOUR = "hour";
		final String DAY = "day";
		final String MONTH = "month";

//		final String[] timeUnitAsStrings = { MINUTE, HOUR, DAY, MONTH };
//		final int[] timeUnitAsMinutes = { 1, 60, 1440, 1440 * 30 };
		Map<String, Integer> timeUnitMap = new HashMap<>();
		timeUnitMap.put(MINUTE, 1);
		timeUnitMap.put(HOUR, 60);
		timeUnitMap.put(DAY, 1440);
		timeUnitMap.put(MONTH, 1440 * 30);

		Map<String, Integer> timeUnitMapCalender = new HashMap<>();
		timeUnitMapCalender.put(MINUTE, Calendar.MINUTE);
		timeUnitMapCalender.put(HOUR, Calendar.HOUR);
		timeUnitMapCalender.put(DAY, Calendar.DATE);
		timeUnitMapCalender.put(MONTH, Calendar.MONTH);

		String timeUnit = checklist.getUnit();

		List<ChecklistSchedule> schedulesList = new ArrayList<>();

		c.setTime(new Date(start));
		c.add(timeUnitMapCalender.get(timeUnit), frequency);

		long startTimeForSchedule = start;
		long endTimeForSchedule = c.getTimeInMillis();

		ChecklistState checklistState = checklistStateDao.getLatestChecklistStateByChecklist(checklist);

		while (endTimeForSchedule <= end) {

			ChecklistSchedule schedule = new ChecklistSchedule();
			schedule.setChecklist(checklist);
			schedule.setStartTimestamp(startTimeForSchedule);
			schedule.setEndTimestamp(endTimeForSchedule);
			schedule.setFrequency(checklist.getFrequency());
			schedule.setUnit(checklist.getUnit());
			schedule.setChecklistState(checklistState);

			if (endTimeForSchedule > System.currentTimeMillis() && (startTimeForSchedule <= rangeEnd))
				schedulesList.add(schedule);

			c.setTime(new Date(endTimeForSchedule));
			c.add(timeUnitMapCalender.get(timeUnit), frequency);

			startTimeForSchedule = endTimeForSchedule;
			endTimeForSchedule = c.getTimeInMillis();
		}

		return schedulesList;

	}

	public Object deleteChecklistStructureFieldByFieldId(int fieldId) {
		if (Boolean.TRUE.equals(checklistStructureDao.deleteChecklistStructureFieldByFieldId(fieldId))) {
			return ResponseEntity.ok().body("Checklist structure field deleted successfully");
		}
		return ResponseEntity.badRequest().body("Error occured while deleting checklist structure field");

	}

	@Transactional
	public ResponseEntity<String> deleteChecklistByChecklistId(int checklistId) {

		if (!checklistApproverDao.deleteFromChecklistApproverGroupByChecklistId(checklistId)) {
			logger.error("error occured while deleting records from tbl_checklist_approver_group for checklist ID:[{}]",
					checklistId);
			return ResponseEntity.badRequest().body("Error occured while deleting checklist");
		}

		if (!checklistApproverDao.deleteFromChecklistApproverUserByChecklistId(checklistId)) {
			logger.error("error occured while deleting records from tbl_checklist_approver_user for checklist ID:[{}]",
					checklistId);
			return ResponseEntity.badRequest().body("Error occured while deleting checklist");
		}

		return checklistDao.deleteChecklistByChecklistId(checklistId);
	}

	public Object updateChecklist(Checklist checklist) {

		if (checklist.getName().isEmpty()) {
			logger.error("checklist name is empty!");
			return ResponseEntity.badRequest().body("Checklist name cannot be empty!");
		}

		return checklistDao.updateChecklist(checklist);
	}

	public List<ChecklistStructureGroupingDto> groupingChecklistStructureByAsset(
			List<ChecklistStructureDto> checklistStructure) {

		List<ChecklistStructureGroupingDto> structure = new ArrayList<>();
		LinkedHashMap<Integer, ArrayList<ChecklistStructureDto>> map = new LinkedHashMap<>();
		for (int i = 0; i < checklistStructure.size(); i++) {
			int id = checklistStructure.get(i).getTemplateId();
			if (!map.containsKey(checklistStructure.get(i).getTemplateId())) {
				map.put(id, new ArrayList<ChecklistStructureDto>());
			}
			ArrayList<ChecklistStructureDto> structureDto = map.get(id);
			structureDto.add(checklistStructure.get(i));
			map.put(id, structureDto);
		}

		for (Map.Entry<Integer, ArrayList<ChecklistStructureDto>> entry : map.entrySet()) {
			ChecklistStructureGroupingDto struc = new ChecklistStructureGroupingDto();
			int key = entry.getKey();
			Template template = templateDao.getTemplateById(key);
			struc.setStructure(entry.getValue());
			struc.setName(template.getName());
			structure.add(struc);
		}

		return structure;
	}

	public ResponseEntity<String> activateChecklistByChecklistId(int checklistId, String userId) {

		Checklist checklist = checklistDao.getChecklistByChecklistId(checklistId);

		if (checklist == null) {
			logger.error("No checklist retrieved from database");
			return ResponseEntity.badRequest().body("Error occured while activating checklist");
		}

		if (checklist.getEndTime() <= System.currentTimeMillis()) {
			logger.error("End Date is less than current system time");
			return ResponseEntity.badRequest().body("End date time cannot be less than current date time");
		}

		GetChecklistStructureDto getChecklistStructureDto = (GetChecklistStructureDto) getChecklistDetailsByChecklistId(
				checklistId);

		if (getChecklistStructureDto.getChecklistStructure() == null
				|| getChecklistStructureDto.getChecklistStructure().isEmpty()) {
			return ResponseEntity.badRequest().body("Add Location or Asset");
		}

		/**
		 * 
		 * save new state of checklist if updated
		 * 
		 */

		ChecklistState existingChecklistState = checklistStateDao.getLatestChecklistStateByChecklist(checklist);
		List<ChecklistStructure> checklistFields = checklistStructureDao.getChecklistStructureByChecklist(checklist);

		if (existingChecklistState == null) {

			Users user = null;
			if (userId != null && !userId.isEmpty()) {
				user = usersDao.getUserByUserId(userId);
			}
			
			ChecklistState checklistState = new ChecklistState();
			checklistState.setChecklist(checklist);
			checklistState.setUser(user);
			ChecklistState savedChecklistState = checklistStateDao.addNewChecklistState(checklistState);

			if (savedChecklistState != null) {
				List<ChecklistStateField> checklistStateFields = new ArrayList<>();

				for (ChecklistStructure checklistField : checklistFields) {
					ChecklistStateField checklistStateField = new ChecklistStateField();
					checklistStateField.setChecklistStateFieldOrderId(checklistField.getChecklistFieldId());
					checklistStateField.setTemplateStructure(checklistField.getTemplateStructure());
					checklistStateField.setChecklistState(savedChecklistState);

					checklistStateFields.add(checklistStateField);
				}
				List<ChecklistStateField> savedChecklistStateFields = checklistStateFieldDao
						.addChecklistStateFields(checklistStateFields);
			}
		} else {
			List<ChecklistStateField> existingChecklistStateFields = checklistStateFieldDao
					.getChecklistStateFieldsByChecklistState(existingChecklistState);
			List<Integer> checklistFieldIds = new ArrayList<Integer>();

			boolean isChanged = false;

			if (existingChecklistStateFields.size() != checklistFields.size()) {
				isChanged = true;
			} else {
				for (ChecklistStateField existingChecklistStateField : existingChecklistStateFields) {
					checklistFieldIds.add(existingChecklistStateField.getChecklistStateFieldOrderId());
				}

				for (ChecklistStructure checklistField : checklistFields) {
					if (!checklistFieldIds.contains(checklistField.getChecklistFieldId())) {
						isChanged = true;
						break;
					}
				}
			}

			if (isChanged) {

				Users user = null;
				if (userId != null && !userId.isEmpty()) {
					user = usersDao.getUserByUserId(userId);
				}

				ChecklistState checklistState = new ChecklistState();
				checklistState.setChecklist(checklist);
				checklistState.setUser(user);
				ChecklistState savedChecklistState = checklistStateDao.addNewChecklistState(checklistState);

				if (savedChecklistState != null) {
					List<ChecklistStateField> checklistStateFields = new ArrayList<>();

					for (ChecklistStructure checklistField : checklistFields) {
						ChecklistStateField checklistStateField = new ChecklistStateField();
						checklistStateField.setChecklistStateFieldOrderId(checklistField.getChecklistFieldId());
						checklistStateField.setTemplateStructure(checklistField.getTemplateStructure());
						checklistStateField.setChecklistState(savedChecklistState);

						checklistStateFields.add(checklistStateField);
					}
					List<ChecklistStateField> savedChecklistStateFields = checklistStateFieldDao
							.addChecklistStateFields(checklistStateFields);
				}
			}
		}

		/**
		 * 
		 * check if checklist is MANUAL or not
		 * 
		 */

		if (checklist.isManual()) {

			if (!activateManualChecklist(checklist)) {
				logger.error("Manual Checklist Activation Failed");
				return ResponseEntity.badRequest().body("Error occured while activating checklist");
			}
		} else {

			/*
			 * NOT Manual checklist schedules of checklist
			 * 
			 */

			LocalDate localDate = LocalDate.now();
			LocalDateTime endOfDay = LocalTime.MAX.atDate(localDate);
			long utcEndOfTheDayInMillis = endOfDay.atOffset(OffsetDateTime.now().getOffset()).toInstant()
					.toEpochMilli();

			List<ChecklistSchedule> checklistSchedules = getChecklistSchedulesInTimeRange(checklist,
					System.currentTimeMillis(), utcEndOfTheDayInMillis);

			checklistScheduleDao.addChecklistSchedulesList(checklistSchedules);

			/****************************/

		}

		return createTagTablesAndActivate(getChecklistStructureDto, checklistId);
	}

	@Transactional
	private ResponseEntity<String> createTagTablesAndActivate(GetChecklistStructureDto getChecklistStructureDto,
			int checklistId) {
		try {
			List<ChecklistStructureGroupingDto> checkStructureDto = getChecklistStructureDto.getChecklistStructure();
			for (int i = 0; i < checkStructureDto.size(); i++) {
				for (ChecklistStructureDto structure : checkStructureDto.get(i).getStructure()) {
					int templateId = structure.getTemplateId();
					int fieldId = structure.getTemplateFieldId();

					String tagId = Constants.ORGANIZATION_ID + "_" + templateId + "_" + fieldId;
					String tagName = templateId + "_" + fieldId;
					String tagType = structure.getFieldType();

					boolean isTableCreated;
					String tagDataType;
					if (Arrays.asList(Constants.NUMBER_TYPES).contains(tagType)) {
						isTableCreated = tagDao.createTagTable(tagId, TagDao.TAG_TYPE_NUMERIC);
						tagDataType = TagDao.TAG_TYPE_NUMERIC;
					} else {
						isTableCreated = tagDao.createTagTable(tagId, TagDao.TAG_TYPE_TEXT);
						tagDataType = TagDao.TAG_TYPE_TEXT;
					}

					if (isTableCreated) {
						// Insert in to tagDetail
					}

					TemplateStructure templateStructure = new TemplateStructure();
					templateStructure.setFieldId(fieldId);

					Organization org = new Organization();
					org.setOrganizationId(Constants.ORG_NAME);
					org.setId(Constants.ORG_ID);

					TagDetail tagDetail = new TagDetail();
					tagDetail.setTagId(tagId);
					tagDetail.setDataSource(Constants.TAG_DATA_SOURCE);
					tagDetail.setDate(LocalDate.now());
					tagDetail.setDescription(structure.getDescription());
					tagDetail.setExecutionInterval(null);
					tagDetail.setLastExecutionDate(null);
					tagDetail.setOrganizationId(org);
					tagDetail.setScheduled(false);
					tagDetail.setTagType(Constants.TAG_DATA_SOURCE);
					tagDetail.setTagName(tagName);
					tagDetail.setTagNameDatasource(tagId);
					tagDetail.setTagOpcName(null);
					tagDetail.setUpdatedBy(Constants.USER_ID);
					tagDetail.setTagDataType(tagDataType);
					tagDetail.setTemplateField(templateStructure);

					tagDetailDao.addNewTagDetail(tagDetail);

				}
			}
			if (!checklistDao.setActivationStatus(checklistId)) {
				return ResponseEntity.badRequest().body("Error occured while activating checklist");
			}
		} catch (Exception ex) {
			logger.error("Exception:", ex);
			return ResponseEntity.badRequest().body("Error occured while activating checklist");
		}
		logger.info("[SUCCESSFULL]");
		return ResponseEntity.ok().body("Checklist activated successfully");
	}

	@Transactional
	private boolean activateManualChecklist(Checklist checklist) {

		List<ChecklistApproverUser> approverUsers = checklistApproverDao
				.getChecklistApproverUsersByChecklistId(checklist.getChecklistId());
		List<ChecklistApproverGroup> approverGroups = checklistApproverDao
				.getChecklistApproverGroupsByChecklistId(checklist.getChecklistId());

		ChecklistState checklistState = checklistStateDao.getLatestChecklistStateByChecklist(checklist);

		for (ChecklistApproverGroup AG : approverGroups) {

			if (AG.getWorkflowLevel().getWorkflowLevelId() == Constants.WORKFLOW_LEVEL_ONE_ID) {

				List<Users> users = usersDao.getUsersByGroupId(AG.getGroup().getId());

				for (Users user : users) {
					long currMillis = System.currentTimeMillis();
					ChecklistSchedule sch = new ChecklistSchedule();
					sch.setChecklist(checklist);
					sch.setStartTimestamp(currMillis);
					sch.setEndTimestamp(Constants.MANUAL_CHECKLIST_SCHEDULE_END_DATE_NULL);
					sch.setFrequency(checklist.getFrequency());
					sch.setUnit(checklist.getUnit());
					sch.setChecklistState(checklistState);

					ChecklistSchedule savedChecklistSchedule = checklistScheduleDao.addChecklistSchedule(sch);
					if (savedChecklistSchedule == null) {
						return false;
					}

					ChecklistScheduleClaimed checklistScheduleClaimed = new ChecklistScheduleClaimed();

					checklistScheduleClaimed.setChecklistSchedule(savedChecklistSchedule);
					checklistScheduleClaimed.setTimestamp(currMillis);
					checklistScheduleClaimed.setUser(user);

					ChecklistScheduleClaimed savedChecklistScheduleClaimed = checklistScheduleClaimedDao
							.addClaimedChecklistSchedule(checklistScheduleClaimed);
					if (savedChecklistScheduleClaimed == null) {
						return false;
					}
				}
			}
		}

		for (ChecklistApproverUser AU : approverUsers) {

			long currMillis = System.currentTimeMillis();

			if (AU.getWorkflowLevel().getWorkflowLevelId() == Constants.WORKFLOW_LEVEL_ONE_ID) {

				ChecklistSchedule sch = new ChecklistSchedule();
				sch.setChecklist(checklist);
				sch.setStartTimestamp(currMillis);
				sch.setEndTimestamp(Constants.MANUAL_CHECKLIST_SCHEDULE_END_DATE_NULL);
				sch.setFrequency(checklist.getFrequency());
				sch.setUnit(checklist.getUnit());
				sch.setChecklistState(checklistState);

				ChecklistSchedule savedChecklistSchedule = checklistScheduleDao.addChecklistSchedule(sch);
				if (savedChecklistSchedule == null) {
					return false;
				}

				ChecklistScheduleClaimed checklistScheduleClaimed = new ChecklistScheduleClaimed();

				checklistScheduleClaimed.setChecklistSchedule(savedChecklistSchedule);
				checklistScheduleClaimed.setTimestamp(currMillis);
				checklistScheduleClaimed.setUser(AU.getUser());

				ChecklistScheduleClaimed savedChecklistScheduleClaimed = checklistScheduleClaimedDao
						.addClaimedChecklistSchedule(checklistScheduleClaimed);
				if (savedChecklistScheduleClaimed == null) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean deletePreviousSchedulesOfManualChecklist(Checklist checklist) {
		try {
			List<ChecklistSchedule> checklistSchedules = checklistScheduleDao
					.getChecklistSchedulesOfManualChecklist(checklist);

			for (ChecklistSchedule CS : checklistSchedules) {
				if (!checklistScheduleClaimedDao.deleteChecklistSchedulesClaimedByChecklistSchedule(CS)) {
					logger.error("couldn't delete checklist schedule claimed ID [{}] of checklist ID [{}]",
							CS.getChecklistScheduleId(), checklist.getChecklistId());
					return false;
				}

				if (!checklistPartiallySavedDataDao.deleteSavedChecklistDataByChecklistSchedule(CS)) {
					logger.error(
							"couldn't delete partially saved data of checklist schedule ID [{}] of checklist ID [{}]",
							CS.getChecklistScheduleId(), checklist.getChecklistId());
					return false;
				}

				if (!checklistScheduleDao.deleteChecklistScheduleByChecklistScheduleId(CS.getChecklistScheduleId())) {
					logger.error("couldn't delete checklist schedule ID [{}] of checklist ID [{}]",
							CS.getChecklistScheduleId(), checklist.getChecklistId());
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
			return false;
		}
		return true;
	}

	@Transactional
	public ResponseEntity<String> DeActivateChecklistByChecklistId(Integer checklistId) {
		Checklist checklist = checklistDao.getChecklistByChecklistId(checklistId);
		if (checklist == null) {
			logger.error("Error: while deactivating checklist id: [{}], does not exist", checklistId);
			return ResponseEntity.badRequest().body("Error: while deactivating checklist, checklist does not exit");
		}

		if (!checklist.isManual()) {
			if (!checklistScheduleDao
					.deleteChecklistSchedulesByChecklistHavingStartTimeGreaterThanCurrTime(checklist)) {
				logger.error("Error: while deactivating checklist id: [{}], deleting schedules", checklistId);
				return ResponseEntity.badRequest().body("Error: while deactivating checklist");
			}
		} else {
			if (!deletePreviousSchedulesOfManualChecklist(checklist)) {
				logger.error("Error: while deactivating manual checklist id: [{}]", checklistId);
				return ResponseEntity.badRequest().body("Error: while deactivating checklist");
			}
		}

		return checklistDao.deActivateChecklistByChecklistId(checklistId);
	}
}
