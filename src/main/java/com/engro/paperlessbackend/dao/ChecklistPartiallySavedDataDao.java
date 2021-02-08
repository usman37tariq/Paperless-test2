package com.engro.paperlessbackend.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.ChecklistPartiallySavedData;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.repositories.ChecklistPartiallySavedDataRepository;

@Component
public class ChecklistPartiallySavedDataDao {

	private static Logger logger = LoggerFactory.getLogger(ChecklistPartiallySavedDataDao.class);

	@Autowired
	ChecklistPartiallySavedDataRepository checklistPartiallySavedDataRepository;

	public ChecklistPartiallySavedData saveChecklistData(ChecklistPartiallySavedData checklistPartiallySavedData) {
		ChecklistPartiallySavedData savedChecklistData = null;

		try {
			savedChecklistData = checklistPartiallySavedDataRepository.save(checklistPartiallySavedData);
			logger.info("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return savedChecklistData;
	}

	public ChecklistPartiallySavedData getSavedChecklistDataByChecklistScheduleIdAndUserId(
			ChecklistSchedule checklistSchedule, Users user) {
		ChecklistPartiallySavedData checklistData = null;
		try {
			checklistData = checklistPartiallySavedDataRepository.getTop1ByChecklistScheduleAndUser(checklistSchedule,
					user);
			logger.info("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistData;
	}

	public boolean deleteSavedChecklistDataByChecklistSchedule(ChecklistSchedule checklistSchedule) {
		try {
			checklistPartiallySavedDataRepository.deleteByChecklistSchedule(checklistSchedule);
			logger.info("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
			return false;
		}
		return true;
	}

	public ChecklistPartiallySavedData getSavedChecklistDataByChecklistSchedule(ChecklistSchedule checklistSchedule) {
		ChecklistPartiallySavedData checklistData = null;
		try {
			checklistData = checklistPartiallySavedDataRepository.getTop1ByChecklistSchedule(checklistSchedule);
			logger.info("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistData;

	}
}
