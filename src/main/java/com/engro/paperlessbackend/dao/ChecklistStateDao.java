package com.engro.paperlessbackend.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistState;
import com.engro.paperlessbackend.repositories.ChecklistStateRepository;

@Component
public class ChecklistStateDao {
	
	private static Logger logger = LoggerFactory.getLogger(ChecklistStateDao.class);

	@Autowired
	ChecklistStateRepository checklistStateRepository;
	
	public ChecklistState getLatestChecklistStateByChecklist(Checklist checklist) {
		ChecklistState checklistState = null;
		try {
			checklistState = checklistStateRepository.findTop1ByChecklistOrderByChecklistStateIdDesc(checklist);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistState;
	}
	
	public List<ChecklistState> getAllChecklistStatesByChecklist(Checklist checklist) {
		List<ChecklistState> checklistStates = null;
		try {
			checklistStates = checklistStateRepository.findByChecklist(checklist);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistStates;
	}
	public ChecklistState getFristChecklistStateInBackwardDirection(Checklist checklist, long timestamp) {
		ChecklistState checklistState = null;
		try {
			checklistState = checklistStateRepository.findTop1ByChecklistAndTimeStampLessThanEqual(checklist, timestamp);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistState;
	}
	
	public ChecklistState getFristChecklistStateInForwardDirection(Checklist checklist, long timestamp) {
		ChecklistState checklistState = null;
		try {
			checklistState = checklistStateRepository.findTop1ByChecklistAndTimeStampGreaterThanEqual(checklist, timestamp);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistState;
	}
	public ChecklistState addNewChecklistState(ChecklistState checklistState) {
		ChecklistState savedChecklistState = null;
		try {
			savedChecklistState = checklistStateRepository.save(checklistState);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return savedChecklistState;
	}
	
	public ChecklistState getChecklistStateById(long id) {
		try {
			Optional<ChecklistState> checklistState = checklistStateRepository.findById(id);
			logger.info("[Success]");
			if(checklistState.isPresent()) {
				return checklistState.get();
			}
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return null;
	}
}
