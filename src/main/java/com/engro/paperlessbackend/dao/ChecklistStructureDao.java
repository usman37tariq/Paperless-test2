package com.engro.paperlessbackend.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistStructure;
import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.repositories.ChecklistStructureRepository;

@Component
public class ChecklistStructureDao {

	private static Logger logger = LoggerFactory.getLogger(ChecklistStructureDao.class);

	@Autowired
	ChecklistStructureRepository checklistStructureRepository;

	@Autowired
	TemplateStructureDao templateStructureDao;

	@Transactional
	public boolean deleteFromChecklistStructureByNodeIdAndChecklistId(int checklistId, int nodeId) {
		try {
			checklistStructureRepository.deleteFromChecklistStructureByNodeIdAndChecklistId(checklistId, nodeId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	@Transactional
	public List<ChecklistStructure> addAssetTemplateToChecklistStructure(List<ChecklistStructure> checklistStructure) {
		List<ChecklistStructure> savedChecklistStructure = null;
		try {
			savedChecklistStructure = checklistStructureRepository.saveAll(checklistStructure);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedChecklistStructure;
	}

	public boolean deleteChecklistStructureFieldByFieldId(int fieldId) {
		try {
			checklistStructureRepository.deleteById(fieldId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public List<ChecklistStructure> getChecklistStructureByChecklist(Checklist checklist) {
		List<ChecklistStructure> structure = null;
		try {
			structure = checklistStructureRepository.findByChecklistIdOrderByUpdatedAtAscChecklistFieldIdAsc(checklist);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return structure;
	}

	public List<ChecklistStructure> getChecklistFieldsByTemplateField(TemplateStructure templateField) {
		List<ChecklistStructure> checklistFields = null;
		try {
			checklistFields = checklistStructureRepository.findByTemplateStructure(templateField);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return checklistFields;
	}

	public boolean deleteChecklistStructureFieldByTemplateField(TemplateStructure templateField) {
		try {
			checklistStructureRepository.deleteByTemplateStructure(templateField);
			logger.info("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
			return false;
		}
		return true;
	}
}
