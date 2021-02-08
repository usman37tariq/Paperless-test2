package com.engro.paperlessbackend.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistScheduleClaimed;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.repositories.ChecklistScheduleClaimedRepository;

@Component
public class ChecklistScheduleClaimedDao {

	private static Logger logger = LoggerFactory.getLogger(ChecklistScheduleClaimedDao.class);

	@Autowired
	ChecklistScheduleClaimedRepository checklistScheduleClaimedRepository;

	public List<ChecklistScheduleClaimed> getClaimedChecklistDataByChecklistId(int checklistId) {
		List<ChecklistScheduleClaimed> checklistClaimed = new ArrayList<ChecklistScheduleClaimed>();
		try {
			checklistClaimed = checklistScheduleClaimedRepository.getClaimedChecklistDataByChecklistId(checklistId,
					System.currentTimeMillis());
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return checklistClaimed;
	}

	public List<ChecklistScheduleClaimed> getClaimedChecklistDataByChecklistScheduleAndUser(
			ChecklistSchedule checklistSchedule, Users user) {
		List<ChecklistScheduleClaimed> checklistClaimed = new ArrayList<ChecklistScheduleClaimed>();
		try {
			checklistClaimed = checklistScheduleClaimedRepository.findByChecklistScheduleAndUser(checklistSchedule,
					user);
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return checklistClaimed;
	}

	public List<ChecklistScheduleClaimed> getChecklistSchedulesBySchedule(ChecklistSchedule schedule) {
		List<ChecklistScheduleClaimed> checklistSchedulesClaimed = null;
		try {
			checklistSchedulesClaimed = checklistScheduleClaimedRepository.findByChecklistSchedule(schedule);
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return checklistSchedulesClaimed;
	}

	public ChecklistScheduleClaimed addClaimedChecklistSchedule(ChecklistScheduleClaimed claimChecklist) {
		ChecklistScheduleClaimed ch = new ChecklistScheduleClaimed();
		try {
			ch = checklistScheduleClaimedRepository.save(claimChecklist);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - ", ex);
		}
		return ch;
	}

	@Transactional
	public boolean deleteChecklistSchedulesClaimedByChecklistSchedule(ChecklistSchedule checklistSchedule) {
		try {
			checklistScheduleClaimedRepository.deleteByChecklistSchedule(checklistSchedule);
			logger.info("[SUCCESSFUL]");
			return true;
		} catch (Exception e) {
			logger.error("[EXCEPTION] - ", e);
		}
		return false;
	}
}
