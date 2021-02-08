package com.engro.paperlessbackend.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dao.ChecklistScheduleDao;
import com.engro.paperlessbackend.dao.ChecklistStateFieldDao;
import com.engro.paperlessbackend.dao.ChecklistStructureDao;
import com.engro.paperlessbackend.dao.TagDao;
import com.engro.paperlessbackend.dao.TagDetailDao;
import com.engro.paperlessbackend.dao.TemplateStructureDao;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistStateField;
import com.engro.paperlessbackend.entities.ChecklistStructure;
import com.engro.paperlessbackend.entities.Tag;
import com.engro.paperlessbackend.entities.TagDetail;
import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class TemplateStructureService {

	private static Logger logger = LoggerFactory.getLogger(TemplateStructureService.class);

	@Autowired
	TemplateStructureDao templateStructureDao;

	@Autowired
	ChecklistStructureDao checklistStructureDao;

	@Autowired
	TagDetailDao tagDetailDao;

	@Autowired
	TagDao tagDao;

	@Autowired
	ChecklistStateFieldDao checklistStateFieldDao;

	@Autowired
	ChecklistScheduleDao checklistScheduleDao;

	public Object addTemplateStructureField(TemplateStructure templateStructure) {

		if (templateStructure.getDescription().isEmpty()) {
			logger.error("Description cannot be empty!");
			return ResponseEntity.badRequest().body("Description cannot be empty!");
		}

		if (templateStructure.getLowerLimit() != null && templateStructure.getUpperLimit() != null) {
			if (templateStructure.getLowerLimit() > templateStructure.getUpperLimit()) {
				logger.error("Low limit cannot be greater than High limit");
				return ResponseEntity.badRequest().body("Low limit cannot be greater than High limit");
			}
			if (templateStructure.getLowerLimit().equals(templateStructure.getUpperLimit())) {
				logger.error("Low limit cannot be equal to High limit");
				return ResponseEntity.badRequest().body("Low limit cannot be equal to High limit");
			}
		}

		return templateStructureDao.addTemplateStructureField(templateStructure);
	}

	public Object getTemplateStructureById(Integer id) {
		return templateStructureDao.getTemplateStructureByTemplateId(id);
	}

	public Object updateTemplateStructureField(TemplateStructure templateStructure) {

		if (templateStructure.getDescription().isEmpty()) {
			logger.error("Description cannot be empty!");
			return ResponseEntity.badRequest().body("Description cannot be empty!");
		}

		TemplateStructure oldTemplateStructure = templateStructureDao
				.getTemplateStructureByFieldId(templateStructure.getFieldId());

		if (templateStructure.getLowerLimit() != null && templateStructure.getUpperLimit() != null) {

			if (templateStructure.getLowerLimit() > templateStructure.getUpperLimit()) {
				logger.error("Low limit cannot be greater than High limit");
				return ResponseEntity.badRequest().body("Low limit cannot be greater than High limit");
			}

			if (templateStructure.getLowerLimit().equals(templateStructure.getUpperLimit())) {
				logger.error("Low limit cannot be equal to High limit");
				return ResponseEntity.badRequest().body("Low limit cannot be equal to High limit");
			}

		}

		if ((oldTemplateStructure.isText() && !templateStructure.isText())
				|| (!oldTemplateStructure.isText() && templateStructure.isText())) {
			logger.error("Type change not allowed");
			return ResponseEntity.badRequest().body("Type change not allowed");
		}
		/**
		 * Update description in Tag Detail Table for SSA(Iridium)
		 */
		List<TagDetail> tagDetailList = tagDetailDao.getTagDetailByField(templateStructure.getFieldId());

		if (tagDetailList != null && !tagDetailList.isEmpty()) {

			TagDetail tagDetail = tagDetailList.get(0);

			// in case of type change, check if data exists against this field
			if (!oldTemplateStructure.getFieldType().equalsIgnoreCase(templateStructure.getFieldType())) {

				List<Tag> tagDataList = tagDao.getLatestDataForTag(tagDetail.getTagId());
				if (tagDataList != null && !tagDataList.isEmpty()) {
					logger.error("Type change not allowed , Data available against this parameter");
					return ResponseEntity.badRequest()
							.body("Type change not allowed, Data available against this parameter");
				}
			}

			if (!oldTemplateStructure.getDescription().equals(templateStructure.getDescription())) {
				tagDetail.setDescription(templateStructure.getDescription());
				tagDetailDao.updateTagDetail(tagDetail);
			}
		}
		return templateStructureDao.updateTemplateStructureField(templateStructure);
	}

	@Transactional
	public ResponseEntity<String> deleteTemplateStructureFieldByFieldId(int fieldId) {

		TemplateStructure templateField = templateStructureDao.getTemplateStructureByFieldId(fieldId);
		if (templateField == null) {
			logger.error("Error occured while deleting template field");
			return ResponseEntity.badRequest().body("Error occured while deleting template field");
		}

		/// check if the field is being used in a checklist
		List<ChecklistStructure> checklistFields = checklistStructureDao
				.getChecklistFieldsByTemplateField(templateField);
		String errMsg = "Deletion NOT allowed, this field is being used in checklist(s) : ";
		boolean flag = false;
		if (checklistFields != null && !checklistFields.isEmpty()) {
			for (int i = 0; i < checklistFields.size(); i++) {
				if (checklistFields.get(i).getChecklist().getIsDeleted() == Constants.CHECKLIST_IS_NOT_DELETED) {
					flag = true;
					errMsg += checklistFields.get(i).getChecklist().getName();
					if (i < checklistFields.size() - 1) {
						errMsg += ",";
					}
				}
			}
		}
		if (flag) {
			logger.error("Deletion NOT allowed, this field is being used in checklist(s) [Checklist Structure Check]");
			return ResponseEntity.badRequest().body(errMsg);
		}

		//// checklist checklist state
		List<ChecklistStateField> checklistStateFields = checklistStateFieldDao
				.getChecklistStateFieldsByTemplateStructureField(templateField);

		for (ChecklistStateField stateField : checklistStateFields) {

			List<ChecklistSchedule> checklistSchedules = checklistScheduleDao
					.getChecklistSchedulesByChecklistState(stateField.getChecklistState());
			if (checklistSchedules != null && !checklistSchedules.isEmpty()) {
				for(ChecklistSchedule schedule: checklistSchedules) {
					if(schedule.getChecklist().getIsDeleted()==0) {
						logger.error(
								"Deletion NOT allowed, this field is being used in previously generated checklist schedules [Checklist State Check]");
						return ResponseEntity.badRequest().body("Deletion NOT allowed,  Data available against this parameter");
					}
				}
				
			}
		}

		//// drop table in which the tag is being used
		List<TagDetail> tagDetails = tagDetailDao.getTagDetailsByTemplateField(templateField);

		if (tagDetails != null && !tagDetails.isEmpty()) {

			List<Tag> tagDataList = tagDao.getLatestDataForTag(tagDetails.get(0).getTagId());

			if (tagDataList != null && !tagDataList.isEmpty()) {
				logger.error("Deletion NOT allowed,  Data available against this parameter");
				return ResponseEntity.badRequest()
						.body("Deletion NOT allowed,  Data available against this parameter");
			}

			for (TagDetail tagDetail : tagDetails) {
				if (!tagDao.dropTagTableByName(tagDetail.getTagId())) {
					logger.error("Error occured while dropping table [{}]", tagDetail.getTagId());
					return ResponseEntity.badRequest().body("Error occured while deleting template field");
				}
			}
		}

		//// delete corresponding rows from tag detail
		if (!tagDetailDao.deleteTagDetailsByTemplateField(templateField)) {
			logger.error("Error while deleting records from tag details against template field ID [{}]",
					templateField.getFieldId());
			return ResponseEntity.badRequest().body("Error occured while deleting template field");
		}

		if(!checklistStateFieldDao.deleteChecklistStateFieldByTemplateField(templateField)) {
			logger.error("Error while deleting records from tag details against template field ID [{}]",
					templateField.getFieldId());
			return ResponseEntity.badRequest().body("Error occured while deleting template field");
		}
		
		/// delete from checklist structure
		if (!checklistStructureDao.deleteChecklistStructureFieldByTemplateField(templateField)) {
			logger.error("Error while deleting records from checklist structure against template field ID [{}]",
					templateField.getFieldId());
			return ResponseEntity.badRequest().body("Error occured while deleting template field");
		}

		/// delete from checklist structure
		if (!templateStructureDao.deleteTemplateStructureFieldById(templateField.getFieldId())) {
			logger.error("Error while deleting from template structure against template field ID [{}]",
					templateField.getFieldId());
			return ResponseEntity.badRequest().body("Error occured while deleting template field");
		}

		logger.info("Field deleted Successfully");
		return ResponseEntity.ok().body("Field deleted successfully");
	}

}
