package com.engro.paperlessbackend.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistScheduleSkipped;
import com.engro.paperlessbackend.repositories.ChecklistScheduleSkippedRepository;

@Component
public class ChecklistScheduleSkippedDao {

	@Autowired
	ChecklistScheduleSkippedRepository checklistScheduleSkippedRepository;

	private static Logger logger = LoggerFactory.getLogger(ChecklistScheduleSkippedDao.class);

	public ChecklistScheduleSkipped saveChecklistScheduleSkipped(ChecklistScheduleSkipped checklistScheduleSkipped) {
		ChecklistScheduleSkipped savedChecklistScheduleSkipped = null;
		try {
			savedChecklistScheduleSkipped = checklistScheduleSkippedRepository.save(checklistScheduleSkipped);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return savedChecklistScheduleSkipped;
	}

	public List<ChecklistScheduleSkipped> getChecklistScheduleSkippedBySchedule(ChecklistSchedule checklistSchedule) {
		List<ChecklistScheduleSkipped> skipped = null;
		try {
			skipped = checklistScheduleSkippedRepository
					.findByChecklistScheduleOrderBySkippedTimestampDesc(checklistSchedule);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return skipped;
	}

	public ChecklistScheduleSkipped getLatestChecklistScheduleSkipped(ChecklistSchedule checklistSchedule) {
		ChecklistScheduleSkipped savedChecklistScheduleSkipped = null;
		try {
			List<ChecklistScheduleSkipped> list = getChecklistScheduleSkippedBySchedule(checklistSchedule);
			if (!list.isEmpty()) {
				return list.get(0);
			}
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return savedChecklistScheduleSkipped;
	}
}
