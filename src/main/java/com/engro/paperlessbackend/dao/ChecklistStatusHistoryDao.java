package com.engro.paperlessbackend.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistStatusHistory;
import com.engro.paperlessbackend.entities.WorkflowLevel;
import com.engro.paperlessbackend.repositories.ChecklistStatusHistoryRepository;

@Component
public class ChecklistStatusHistoryDao {

	@Autowired
	ChecklistStatusHistoryRepository checklistStatusHistoryRepository;

	private static Logger logger = LoggerFactory.getLogger(ChecklistStatusHistoryDao.class);

	public ChecklistStatusHistory saveChecklistStatusHistory(ChecklistStatusHistory checklistStatus) {
		ChecklistStatusHistory savedStatus = new ChecklistStatusHistory();
		try {
			savedStatus = checklistStatusHistoryRepository.save(checklistStatus);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return savedStatus;
	}

	public ChecklistStatusHistory getLatestChecklistStatusHistoryByScheduleId(ChecklistSchedule checklistSchedule) {
		ChecklistStatusHistory latestChecklistStatusHistory = null;
		try {
			latestChecklistStatusHistory = checklistStatusHistoryRepository
					.findTop1ByChecklistScheduleOrderByStatusTimestampDesc(checklistSchedule);
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return latestChecklistStatusHistory;
	}

	public ChecklistStatusHistory getLatestChecklistStatusHistoryByWorkflowLevel(ChecklistSchedule checklistSchedule,
			WorkflowLevel workflowLevel) {
		ChecklistStatusHistory latestChecklistStatusHistory = null;
		try {
			latestChecklistStatusHistory = checklistStatusHistoryRepository
					.findTop1ByChecklistScheduleAndPreviousWorkflowLevelOrderByStatusTimestampDesc(checklistSchedule,
							workflowLevel);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return latestChecklistStatusHistory;
	}
	
	public ChecklistStatusHistory getLatestChecklistStatusHistoryByCurrentAndPreviousWorkflowLevel(ChecklistSchedule checklistSchedule, WorkflowLevel workflowLevel, WorkflowLevel previousWorkflowLevel) {
		ChecklistStatusHistory latestChecklistStatusHistory = null;
		try {
			latestChecklistStatusHistory = checklistStatusHistoryRepository.findTop1ByChecklistScheduleAndWorkflowLevelAndPreviousWorkflowLevelOrderByStatusTimestampDesc(checklistSchedule, workflowLevel, previousWorkflowLevel);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return latestChecklistStatusHistory;
	}

	public List<ChecklistStatusHistory> getChecklistScheduleStatusHistory(int checklistScheduleId) {
		List<ChecklistStatusHistory> checklistStatusHistory = null;
		ChecklistSchedule checklistSchedule = new ChecklistSchedule();
		checklistSchedule.setChecklistScheduleId(checklistScheduleId);
		try {
			checklistStatusHistory = checklistStatusHistoryRepository
					.findByChecklistScheduleOrderByStatusTimestampAsc(checklistSchedule);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return checklistStatusHistory;
	}

	public List<ChecklistStatusHistory> getChecklistStatusHistoryByScheduleAndWorkflowLevel(
			ChecklistSchedule checklistSchedule, WorkflowLevel level) {

		List<ChecklistStatusHistory> history = null;
		try {
			history = checklistStatusHistoryRepository.findByChecklistScheduleAndWorkflowLevel(checklistSchedule,
					level);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return history;
	}

	public ChecklistStatusHistory getLatestChecklistStatusHistoryByWorkflowLevelAndTimestamp(
			ChecklistSchedule checklistSchedule, WorkflowLevel workflowLevel, long timestamp) {
		ChecklistStatusHistory latestChecklistStatusHistory = null;
		try {
			latestChecklistStatusHistory = checklistStatusHistoryRepository
					.findTop1ByChecklistScheduleAndPreviousWorkflowLevelAndStatusTimestampLessThanOrderByStatusTimestampDesc(
							checklistSchedule, workflowLevel, timestamp);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return latestChecklistStatusHistory;
	}

	public ChecklistStatusHistory getChecklistStatusHistoryByWorkflowLevelAndTimestamp(
			ChecklistSchedule checklistSchedule, WorkflowLevel workflowLevel, long timestamp) {
		ChecklistStatusHistory latestChecklistStatusHistory = null;
		try {
			latestChecklistStatusHistory = checklistStatusHistoryRepository
					.findTop1ByChecklistScheduleAndPreviousWorkflowLevelAndStatusTimestampOrderByStatusTimestamp(
							checklistSchedule, workflowLevel, timestamp);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return latestChecklistStatusHistory;
	}

	public ChecklistStatusHistory getPreviousChecklistStatusHistoryByWorkflowLevelAndTimestamp(
			ChecklistSchedule checklistSchedule, WorkflowLevel wfLevel1, long statusTimestamp) {
		ChecklistStatusHistory latestChecklistStatusHistory = null;
		try {
			latestChecklistStatusHistory = checklistStatusHistoryRepository
					.findTop1ByChecklistScheduleAndPreviousWorkflowLevelAndStatusTimestampLessThanOrderByStatusTimestampDesc(
							checklistSchedule, wfLevel1, statusTimestamp);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return latestChecklistStatusHistory;
	}

	public ChecklistStatusHistory getNextChecklistStatusHistoryByWorkflowLevelAndTimestamp(
			ChecklistSchedule checklistSchedule, WorkflowLevel wfLevel1, long statusTimestamp) {
		ChecklistStatusHistory latestChecklistStatusHistory = null;
		try {
			latestChecklistStatusHistory = checklistStatusHistoryRepository
					.findTop1ByChecklistScheduleAndPreviousWorkflowLevelAndStatusTimestampGreaterThanOrderByStatusTimestampAsc(
							checklistSchedule, wfLevel1, statusTimestamp);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return latestChecklistStatusHistory;
	}
}
