package com.engro.paperlessbackend.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dao.ChecklistApproverDao;
import com.engro.paperlessbackend.dao.ChecklistDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleSkippedDao;
import com.engro.paperlessbackend.dao.ChecklistStateDao;
import com.engro.paperlessbackend.dao.ChecklistStateFieldDao;
import com.engro.paperlessbackend.dao.ChecklistStatusHistoryDao;
import com.engro.paperlessbackend.dao.TagDao;
import com.engro.paperlessbackend.dao.TagDetailDao;
import com.engro.paperlessbackend.dao.TemplateStructureDao;
import com.engro.paperlessbackend.dao.WorkflowLevelDao;
import com.engro.paperlessbackend.dto.ChecklistStructureDto;
import com.engro.paperlessbackend.dto.ChecklistStructureGroupingDto;
import com.engro.paperlessbackend.dto.GetChecklistScheduleDto;
import com.engro.paperlessbackend.dto.GetChecklistStructureDto;
import com.engro.paperlessbackend.dto.HierarchyDto;
import com.engro.paperlessbackend.dto.TagDataForVisualizationDto;
import com.engro.paperlessbackend.dto.TagDataRowDto;
import com.engro.paperlessbackend.dto.TemplateDto;
import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistApproverGroup;
import com.engro.paperlessbackend.entities.ChecklistApproverUser;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistScheduleSkipped;
import com.engro.paperlessbackend.entities.ChecklistState;
import com.engro.paperlessbackend.entities.ChecklistStateField;
import com.engro.paperlessbackend.entities.ChecklistStatusHistory;
import com.engro.paperlessbackend.entities.Tag;
import com.engro.paperlessbackend.entities.TagDetail;
import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.entities.WorkflowLevel;
import com.engro.paperlessbackend.utils.ChecklistStateFieldUtil;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class DataVisualizationService {

	private static Logger logger = LoggerFactory.getLogger(DataVisualizationService.class);

	@Autowired
	HierarchyService hierarchyService;

	@Autowired
	TemplateStructureDao templateStructureDao;

	@Autowired
	TagDetailDao tagDetailDao;

	@Autowired
	TagDao tagDao;

	@Autowired
	ChecklistDao checklistDao;

	@Autowired
	ChecklistService checklistService;

	@Autowired
	ChecklistScheduleDao checklistScheduleDao;

	@Autowired
	ChecklistStatusHistoryDao checklistStatusHistoryDao;

	@Autowired
	WorkflowLevelDao workflowLevelDao;

	@Autowired
	ChecklistScheduleSkippedDao checklistScheduleSkippedDao;

	@Autowired
	ChecklistStateDao checklistStateDao;

	@Autowired
	ChecklistStateFieldDao checklistStateFieldDao;

	@Autowired
	ChecklistStateFieldUtil checklistStateFieldUtil;

	@Autowired
	DataCollectorService dataCollectorService;
	
	@Autowired
	ChecklistApproverDao checklistApproverDao;
	public Object getHierarchyForDataVisualization() {
		List<HierarchyDto> dataVisualizationHierarchy = hierarchyService.getHierarchy();
		List<HierarchyDto> dataVisualizationHierarchyTags = new ArrayList<>();

		for (int i = 0; i < dataVisualizationHierarchy.size(); i++) {

			HierarchyDto currNode = dataVisualizationHierarchy.get(i);
			String currNodeId = currNode.getId();
			logger.debug("[Current Node ID] : [{}]", currNodeId);
			TemplateDto templateDto = templateStructureDao
					.getAssetTemplateStructureByNodeId(Integer.valueOf(currNode.getId()));

			if (templateDto != null) {
				logger.debug("--[Asset Template Node ID] : [{}]", templateDto.getId());
				List<TemplateStructure> templateStructure = templateDto.getTemplateStructures();

				if (templateStructure != null) {

					for (TemplateStructure structure : templateStructure) {

						int fieldId = structure.getFieldId();
						int templateId = structure.getTemplateId();
						String tagId = Constants.ORGANIZATION_ID + "_" + templateId + "_" + fieldId;
						logger.debug("----[Tag ID] : [{}]", tagId);

						List<TagDetail> tagDetails = tagDetailDao.getTagDetailByTagIdAndDataType(tagId,
								Constants.TAG_DATA_TYPE_NUMERIC);

						if (tagDetails != null && !tagDetails.isEmpty()) {
							HierarchyDto newNodeDto = new HierarchyDto();
							TagDetail tagDetail = tagDetails.get(0);
							newNodeDto.setId(tagDetail.getTagId());
							newNodeDto.setDescription(tagDetail.getTagName());
							newNodeDto.setName(tagDetail.getDescription());
							newNodeDto.setParent(String.valueOf(currNodeId));
							newNodeDto.setType(Constants.HIERARCHY_NODE_TYPE_TAG);
							newNodeDto.setUoM(structure.getUnitOfMeasure());
							dataVisualizationHierarchyTags.add(newNodeDto);
						}
					}
				}
			}
		}

		for (HierarchyDto tag : dataVisualizationHierarchy) {
			dataVisualizationHierarchyTags.add(tag);
		}

		return dataVisualizationHierarchyTags;
	}

	public TagDataForVisualizationDto getTagsData(String tagName, String startDate, String endDate) {
		TagDataForVisualizationDto tagData = new TagDataForVisualizationDto();
		List<TagDetail> tagDetailList = tagDetailDao.getTagDetailByTagId(tagName);
		if (tagDetailList != null && !tagDetailList.isEmpty()) {
			TemplateStructure ts = templateStructureDao
					.getTemplateStructureByFieldId(tagDetailList.get(0).getTemplateField().getFieldId());
			if (ts != null) {
				tagData.setUnitOfMeasure(ts.getUnitOfMeasure());
				tagData.setTagName(ts.getDescription());
				tagData.setTagType(ts.getFieldType());
			} else {

				logger.error("Template Structure entry for Tag[{}] with field Id [{}]does not exist in TagDetail Table",
						tagName, tagDetailList.get(0).getTemplateField().getFieldId());
			}
		} else {
			logger.error("Tag[{}] does not exist in TagDetail Table", tagName);
			return tagData;
		}

		List<String> cols = new ArrayList<>();
		List<TagDataRowDto> dataRows = tagDao.getTagData(tagName, startDate, endDate);
		Object[][] data = new Object[dataRows.size()][2];
		for (int i = 0; i < dataRows.size(); i++) {
			data[i][0] = dataRows.get(i).getTimestamp();
			data[i][1] = dataRows.get(i).getValue();
		}
		cols.add("timestamp");
		cols.add("value");
		tagData.setColumns(cols);
		tagData.setData(data);
		return tagData;
	}

	public Object getLatestChecklistDataFromStatusHistory(Integer checklistId, String dateString, Integer direction) {

		Date start;
		Date end;
		Date date;
		List<ChecklistSchedule> checklistSchedules;
		List<GetChecklistScheduleDto> checklistDataList = new ArrayList<>();
		GetChecklistStructureDto getChecklistStructureDto = (GetChecklistStructureDto) checklistService
				.getChecklistDetailsByChecklistId(checklistId);

		try {
			if (direction.equals(Constants.CHECKLIST_SEARCH_DIRECTION_BACKWARD)) {
				date = Constants.SIMPLE_DATE_FORMAT.parse(dateString);
				checklistSchedules = checklistScheduleDao.getChecklistSchedulesByBackwardDirection(
						getChecklistStructureDto.getChecklist(), date.getTime());
			} else if (direction.equals(Constants.CHECKLIST_SEARCH_DIRECTION_FORWARD)) {
				date = Constants.SIMPLE_DATE_FORMAT.parse(dateString);
				checklistSchedules = checklistScheduleDao.getChecklistSchedulesByForwardDirection(
						getChecklistStructureDto.getChecklist(), date.getTime());
			} else {
				SimpleDateFormat df = new SimpleDateFormat(Constants.PATTERN_DATE_ONLY);
				start = df.parse(dateString);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				calendar.add(Calendar.DATE, 1);
				end = calendar.getTime();

				checklistSchedules = checklistScheduleDao.getChecklistSchedulesByBackwardDirection(
						getChecklistStructureDto.getChecklist(), end.getTime());
			}
		} catch (ParseException e) {
			logger.error("ParseException", e);
			return null;
		}

		for (ChecklistSchedule checklistSchedule : checklistSchedules) {

			checklistDataList.add(getChecklistDataByScheduleId(checklistSchedule.getChecklistScheduleId()));

		}
		return checklistDataList;
	}

	public Object getChecklistData(Integer checklistId, String startDate, String endDate) {

		Date start;
		Date end;
		try {
			start = Constants.SIMPLE_DATE_FORMAT.parse(startDate);
			end = Constants.SIMPLE_DATE_FORMAT.parse(endDate);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			return null;
		}
		List<GetChecklistScheduleDto> checklistDataList = new ArrayList<>();
		GetChecklistStructureDto getChecklistStructureDto = (GetChecklistStructureDto) checklistService
				.getChecklistDetailsByChecklistId(checklistId);
		List<ChecklistSchedule> checklistSchedules = checklistScheduleDao
				.getChecklistSchedules(getChecklistStructureDto.getChecklist(), start.getTime(), end.getTime());
		for (ChecklistSchedule checklistSchedule : checklistSchedules) {
			checklistDataList.add(getChecklistDataByScheduleId(checklistSchedule.getChecklistScheduleId()));
		}
		return checklistDataList;
	}

	
	private TreeMap<Timestamp, Tag> getAllTagsData(List<ChecklistStateField> checklistStateFields, Integer direction, int checklistId, long timestamp, Integer scheduleId ) throws ParseException {
		Date d = new Date(timestamp);
		String queryTimestampString = Constants.SIMPLE_DATE_FORMAT.format(d);
		TreeMap<Timestamp, Tag> timestampFieldIdMap = new TreeMap<>();
		for(ChecklistStateField stateField : checklistStateFields) {
			List<TagDetail> fieldTagDetail = tagDetailDao.getTagDetailByField(stateField.getTemplateStructure().getFieldId());
			if (fieldTagDetail != null && !fieldTagDetail.isEmpty()) {
				List<Tag>	dr;
				if (direction.equals(Constants.CHECKLIST_SEARCH_DIRECTION_BACKWARD)) {
						
						if(scheduleId !=null) {
							dr = tagDao.getTagDataBackwardDirection(fieldTagDetail.get(0).getTagId(), queryTimestampString,
									checklistId, scheduleId);
						}else {
							dr = tagDao.getTagDataBackwardDirection(fieldTagDetail.get(0).getTagId(), queryTimestampString,
									checklistId);
						}
				} else if (direction.equals(Constants.CHECKLIST_SEARCH_DIRECTION_FORWARD)) {
					if(scheduleId !=null) {
						dr = tagDao.getTagDataForwardDirection(fieldTagDetail.get(0).getTagId(), queryTimestampString,checklistId, scheduleId);
					}else {
						dr = tagDao.getTagDataForwardDirection(fieldTagDetail.get(0).getTagId(), queryTimestampString,checklistId);
					}
							
				} else {
					SimpleDateFormat df2 = new SimpleDateFormat(Constants.PATTERN_DATE_UPTO_MINUTES);
					String formattedDate = df2.format(d);
					Date start = df2.parse(formattedDate);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(start);
					calendar.add(Calendar.MINUTE, 1);
					Date end = calendar.getTime();
					dr = tagDao.getTagDataFirstValueInTimeRange(fieldTagDetail.get(0).getTagId(), Constants.SIMPLE_DATE_FORMAT.format(start), Constants.SIMPLE_DATE_FORMAT.format(end),
							checklistId);
				}
				
				
				if(!dr.isEmpty()) {
					
					List<ChecklistStatusHistory> checklistStatusHistory = checklistStatusHistoryDao.getChecklistScheduleStatusHistory(dr.get(0).getChecklistScheduleId());
					if(checklistStatusHistory != null && !checklistStatusHistory.isEmpty()) {
						if(!timestampFieldIdMap.containsKey(dr.get(0).getDataEntryTimestamp()))
							timestampFieldIdMap.put(dr.get(0).getDataEntryTimestamp(), dr.get(0));
					}
					
					
				}
				
			}
		}
		
		return timestampFieldIdMap;
	}
	
	public List<GetChecklistScheduleDto> getChecklistData(Integer checklistId, long timestamp, Integer direction, Integer scheduleId) {

		List<ChecklistSchedule> checklistSchedules = new ArrayList<>();
		List<GetChecklistScheduleDto> checklistDataList = new ArrayList<>();

		Checklist checklist = checklistDao.getChecklistByChecklistId(checklistId);

		List<ChecklistState> checklistStates = checklistStateDao.getAllChecklistStatesByChecklist(checklist);
		List<ChecklistStateField> checklistStateFields = checklistStateFieldDao
				.getChecklistStateFieldsByChecklistState(checklistStates);

		try {

			TreeMap<Timestamp, Tag> timestampFieldIdMap = getAllTagsData(checklistStateFields, direction, checklistId,
					timestamp, scheduleId);

			Tag tag;
			if (direction.equals(Constants.CHECKLIST_SEARCH_DIRECTION_BACKWARD)) {
				tag = timestampFieldIdMap.lastEntry().getValue();
			} else if (direction.equals(Constants.CHECKLIST_SEARCH_DIRECTION_FORWARD)) {
				tag = timestampFieldIdMap.firstEntry().getValue();
			} else {
				tag = timestampFieldIdMap.firstEntry().getValue();
			}
			checklistSchedules.add(checklistScheduleDao.getChecklistScheduleByScheduleId(tag.getChecklistScheduleId()));

			GetChecklistScheduleDto scheduleDTO = getChecklistDataByScheduleId(tag.getChecklistScheduleId());
			if (scheduleDTO.getDataEntryUser() != null) {
				long dataEntryTimestamp = scheduleDTO.getDataEntryTimestamp();

				if (getAllTagsData(checklistStateFields, Constants.CHECKLIST_SEARCH_DIRECTION_BACKWARD, checklistId,
						dataEntryTimestamp, null).isEmpty()) {
					scheduleDTO.setHasPrevious(false);
				}

				if (getAllTagsData(checklistStateFields, Constants.CHECKLIST_SEARCH_DIRECTION_FORWARD, checklistId,
						dataEntryTimestamp, null).isEmpty()) {
					scheduleDTO.setHasNext(false);
				}
			}
			checklistDataList.add(scheduleDTO);

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return checklistDataList;
	}

	public GetChecklistScheduleDto getChecklistDataByScheduleId(Integer checklistScheduleId) {

		ChecklistSchedule checklistSchedule = checklistScheduleDao
				.getChecklistScheduleByScheduleId(checklistScheduleId);

		GetChecklistStructureDto getChecklistStructureDto = (GetChecklistStructureDto) checklistService
				.getChecklistDetailsByChecklistId(checklistSchedule.getChecklist().getChecklistId());
		int checklistID= checklistSchedule.getChecklist().getChecklistId();
		List<ChecklistStatusHistory> checklistScheduleStatusHistory = checklistStatusHistoryDao
				.getChecklistScheduleStatusHistory(checklistSchedule.getChecklistScheduleId());

//		if (checklistScheduleStatusHistory == null || checklistScheduleStatusHistory.isEmpty()) {
//			return dataCollectorService.getSavedChecklistScheduleData(checklistSchedule, getChecklistStructureDto);
//		}

		GetChecklistScheduleDto scheduleDto = new GetChecklistScheduleDto();

		ChecklistState checklistState = checklistStateDao
				.getChecklistStateById(checklistSchedule.getChecklistState().getChecklistStateId());

		if (checklistState == null) {

		}
		List<ChecklistStructureGroupingDto> checklistStructureFromChecklistState = new ArrayList<>();
		List<ChecklistStateField> checklistStateFields = checklistStateFieldDao
				.getChecklistStateFieldsByChecklistState(checklistState);

		List<ChecklistStructureDto> checklistStructures = checklistStateFieldUtil
				.getChecklistStructureFromChecklistStateFields(checklistStateFields);
		checklistStructureFromChecklistState = checklistService.groupingChecklistStructureByAsset(checklistStructures);

		scheduleDto.setApproverGroups(getChecklistStructureDto.getApproverGroups());
		scheduleDto.setApproverUsers(getChecklistStructureDto.getApproverUsers());
		scheduleDto.setChecklist(getChecklistStructureDto.getChecklist());
		scheduleDto.setChecklistStructure(checklistStructureFromChecklistState);

		ChecklistScheduleSkipped checklistScheduleSkipped = checklistScheduleSkippedDao
				.getLatestChecklistScheduleSkipped(checklistSchedule);

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
		if (checklistScheduleSkipped != null)

		{
			/**
			 * User has Skipped the data entry.
			 */

			scheduleDto.setReasonForSkipping(checklistScheduleSkipped.getRemarks());
		}
		
		List<WorkflowLevel> allLevels= workflowLevelDao.getAllWorkflowLevel();
		
		Collections.sort(allLevels, new Comparator<WorkflowLevel>(){

	        public int compare(WorkflowLevel o1, WorkflowLevel o2) {
	            // compare two instance workflow levels by workflow level ID
	            return o1.getWorkflowLevelId().compareTo(o2.getWorkflowLevelId());
	        }
	    });
	    
		List<ChecklistApproverGroup> checklistApproverGroups = checklistApproverDao
				.getChecklistApproverGroupsByChecklistId(checklistID);
		List<ChecklistApproverUser> checklistApproverUsers = checklistApproverDao
				.getChecklistApproverUsersByChecklistId(checklistID);
		
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
		
		
		/*
		while(true) {
			
			WorkflowLevel wfLeveltemp= workflowLevelDao.getWorkFlowLevelById(max);
			if (wfLeveltemp== null) {
				break;
			}else {
				max= max+1;
			}
			
		}
		*/
		WorkflowLevel wfLevel0 = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ZERO_ID);
		
		WorkflowLevel wfLevel1 = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ONE_ID);
		/*

		WorkflowLevel wfLevel2 = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_TWO_ID);

		WorkflowLevel wfLevel3 = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_THREE_ID);
		*/

		scheduleDto.setChecklistScheduleStatusHistory(checklistScheduleStatusHistory);

		ChecklistStatusHistory levelOneHistory = checklistStatusHistoryDao
				.getLatestChecklistStatusHistoryByWorkflowLevel(checklistSchedule, wfLevel1);
		
		//New Additions
		List<ChecklistStatusHistory> approversHistory = new ArrayList<ChecklistStatusHistory>();
		ChecklistStatusHistory levelTwoHistory= new ChecklistStatusHistory();
		int approved= 2;
		for (int i = 2; i<allLevels.size()-1; i++) {
			levelTwoHistory= checklistStatusHistoryDao.getLatestChecklistStatusHistoryByCurrentAndPreviousWorkflowLevel(checklistSchedule, 
					allLevels.get(i+1),allLevels.get(i));
			if (levelTwoHistory == null) {continue;}
			else {approversHistory.add(levelTwoHistory); approved= i+1;}
			
		}
		//End- New Additions
		/*
		int diff= max-approversHistory.size();
		if (diff>2) {
			for (int i= approved; i< approved+diff-1; i++) {
				approversHistory.add(null);
			}
		}
		*/
		
		/*
		levelTwoHistory = checklistStatusHistoryDao
				.getLatestChecklistStatusHistoryByCurrentAndPreviousWorkflowLevel(checklistSchedule, wfLevel3,
						wfLevel2);
		if (levelTwoHistory == null) {
			levelTwoHistory = checklistStatusHistoryDao
					.getLatestChecklistStatusHistoryByCurrentAndPreviousWorkflowLevel(checklistSchedule, wfLevel0,
							wfLevel2);
		}
		*/
		ChecklistStatusHistory approvedHistory = checklistStatusHistoryDao
				.getLatestChecklistStatusHistoryByCurrentAndPreviousWorkflowLevel(checklistSchedule, wfLevel0,
						allLevels.get(approved));
		ChecklistStatusHistory approvedHistoryWithoutApprovers = checklistStatusHistoryDao
				.getLatestChecklistStatusHistoryByCurrentAndPreviousWorkflowLevel(checklistSchedule, wfLevel0,
						wfLevel1);
		
		/*If checklist has approvers and it's approved by one or more*/
		if (approved!=2 & max>1) {
			approversHistory.add(approvedHistory);
		}
		/*If checklist has no approvers or has approvers but is not approved by any*/
		else if(approvedHistoryWithoutApprovers==null) {
					approversHistory.add(approvedHistory);
		}

		if (levelOneHistory != null)
			levelOneHistory.setStatusTimestamp(data_timestamp);

		scheduleDto.setDataEntryTimestamp(data_timestamp);
		scheduleDto.setDataEntryUser(levelOneHistory);
		scheduleDto.setApprovers(approversHistory);
		//scheduleDto.setSecondApprover(approvedHistory);

		if (!checklistScheduleStatusHistory.isEmpty()) {
			scheduleDto.setWorkflowLevel(
					checklistScheduleStatusHistory.get(checklistScheduleStatusHistory.size() - 1).getWorkflowLevel());
			scheduleDto.setRemarks(
					checklistScheduleStatusHistory.get(checklistScheduleStatusHistory.size() - 1).getRemarks());

		}
		scheduleDto.setChecklistSchedule(checklistSchedule);

		return scheduleDto;
	}

//	public GetChecklistScheduleDto getChecklistDataByScheduleId(Integer checklistScheduleId) {
//
//		ChecklistSchedule checklistSchedule = checklistScheduleDao
//				.getChecklistScheduleByScheduleId(checklistScheduleId);
//
//		GetChecklistStructureDto getChecklistStructureDto = (GetChecklistStructureDto) checklistService
//				.getChecklistDetailsByChecklistId(checklistSchedule.getChecklist().getChecklistId());
//
//		GetChecklistScheduleDto scheduleDto = new GetChecklistScheduleDto();
//
//		scheduleDto.setApproverGroups(getChecklistStructureDto.getApproverGroups());
//		scheduleDto.setApproverUsers(getChecklistStructureDto.getApproverUsers());
//		scheduleDto.setChecklist(getChecklistStructureDto.getChecklist());
//		scheduleDto.setChecklistStructure(getChecklistStructureDto.getChecklistStructure());
//
//		ChecklistScheduleSkipped checklistScheduleSkipped = checklistScheduleSkippedDao
//				.getLatestChecklistScheduleSkipped(checklistSchedule);
//
//		for (ChecklistStructureGroupingDto checklistStructure : scheduleDto.getChecklistStructure()) {
//			for (ChecklistStructureDto checklistStructureDto : checklistStructure.getStructure()) {
//				List<TagDetail> tagDetail = tagDetailDao
//						.getTagDetailByField(checklistStructureDto.getTemplateFieldId());
//				if (tagDetail != null && !tagDetail.isEmpty()) {
//					List<Tag> dataRows = tagDao.getTagDataAllFields(tagDetail.get(0).getTagId(),
//							checklistSchedule.getChecklistScheduleId());
//
//					if (dataRows.isEmpty()) {
//						checklistStructureDto.setValue(null);
//						checklistStructureDto.setRemarks(null);
//					} else {
//						checklistStructureDto.setValue(dataRows.get(0).getValue());
//						checklistStructureDto.setRemarks(dataRows.get(0).getRemarks());
//					}
//				}
//			}
//		}
//		if (checklistScheduleSkipped != null)
//
//		{
//			/**
//			 * User has Skipped the data entry.
//			 */
//
//			scheduleDto.setReasonForSkipping(checklistScheduleSkipped.getRemarks());
//		}
//
//		WorkflowLevel wfLevel2 = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_TWO_ID);
//
//		WorkflowLevel wfLevel3 = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_THREE_ID);
//
//		WorkflowLevel wfLevel1 = workflowLevelDao.getWorkFlowLevelById(Constants.WORKFLOW_LEVEL_ONE_ID);
//
//		List<ChecklistStatusHistory> checklistScheduleStatusHistory = checklistStatusHistoryDao
//				.getChecklistScheduleStatusHistory(checklistSchedule.getChecklistScheduleId());
//		scheduleDto.setChecklistScheduleStatusHistory(checklistScheduleStatusHistory);
//
//		ChecklistStatusHistory levelOneHistory = checklistStatusHistoryDao
//				.getLatestChecklistStatusHistoryByWorkflowLevel(checklistSchedule, wfLevel1);
//		ChecklistStatusHistory levelTwoHistory = checklistStatusHistoryDao
//				.getLatestChecklistStatusHistoryByWorkflowLevel(checklistSchedule, wfLevel2);
//		ChecklistStatusHistory approvedHistory = checklistStatusHistoryDao
//				.getLatestChecklistStatusHistoryByWorkflowLevel(checklistSchedule, wfLevel3);
//
//		scheduleDto.setDataEntryUser((levelOneHistory == null) ? null : levelOneHistory);
//		scheduleDto.setFirstApprover((levelTwoHistory == null) ? null : levelTwoHistory);
//		scheduleDto.setSecondApprover((approvedHistory == null) ? null : approvedHistory);
//
//		if (!checklistScheduleStatusHistory.isEmpty()) {
//			scheduleDto.setWorkflowLevel(
//					checklistScheduleStatusHistory.get(checklistScheduleStatusHistory.size() - 1).getWorkflowLevel());
//			scheduleDto.setRemarks(
//					checklistScheduleStatusHistory.get(checklistScheduleStatusHistory.size() - 1).getRemarks());
//		}
//		scheduleDto.setChecklistSchedule(checklistSchedule);
//
//		return scheduleDto;
//	}

}
