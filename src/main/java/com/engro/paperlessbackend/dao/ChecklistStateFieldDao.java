package com.engro.paperlessbackend.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.ChecklistState;
import com.engro.paperlessbackend.entities.ChecklistStateField;
import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.repositories.ChecklistStateFieldRepository;

@Component
public class ChecklistStateFieldDao {

	private static Logger logger = LoggerFactory.getLogger(ChecklistStateFieldDao.class);

	@Autowired
	ChecklistStateFieldRepository checklistStateFieldRepository;

	public List<ChecklistStateField> getChecklistStateFieldsByChecklistState(ChecklistState checklistState) {
		List<ChecklistStateField> checklistStateFields = null;
		try {
			checklistStateFields = checklistStateFieldRepository.findByChecklistStateOrderByChecklistStateFieldOrderIdAsc(checklistState);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistStateFields;
	}
	public List<ChecklistStateField> getChecklistStateFieldsByChecklistState(List<ChecklistState> checklistStates) {
		List<ChecklistStateField> checklistStateFields = null;
		try {
			checklistStateFields = checklistStateFieldRepository.findDistinctTemplateStructureByChecklistStateIn(checklistStates);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistStateFields;
	}
	public List<ChecklistStateField> addChecklistStateFields(List<ChecklistStateField> checklistStateFields) {
		List<ChecklistStateField> savedChecklistStateFields = null;
		try {
			savedChecklistStateFields = checklistStateFieldRepository.saveAll(checklistStateFields);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return savedChecklistStateFields;
	}
	public List<ChecklistStateField> getChecklistStateFieldsByTemplateStructureField(TemplateStructure templateField) {
		
		List<ChecklistStateField> checklistStateFields = null;
		try {
			checklistStateFields = checklistStateFieldRepository.findDistinctChecklistStateByTemplateStructure(templateField);
			logger.info("[Success]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return checklistStateFields;
	}
	public boolean deleteChecklistStateFieldByTemplateField(TemplateStructure templateField) {
		try {
			checklistStateFieldRepository.deleteByTemplateStructure(templateField);
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
			return false;
		}
		return true;
	}
}
