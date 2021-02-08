package com.engro.paperlessbackend.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dto.ChecklistStructureDto;
import com.engro.paperlessbackend.entities.ChecklistStateField;

@Component
public class ChecklistStateFieldUtil {

	public List<ChecklistStructureDto> getChecklistStructureFromChecklistStateFields(List<ChecklistStateField> checklistStateFields) {
		
		List<ChecklistStructureDto> checklistStructures = new ArrayList<ChecklistStructureDto>();

		for (ChecklistStateField checklistStateField : checklistStateFields) {
			ChecklistStructureDto checklistStructureDto = new ChecklistStructureDto();
			checklistStructureDto.setChecklistFieldId(checklistStateField.getTemplateStructure().getFieldId());
			checklistStructureDto.setChecklistOrderId(checklistStateField.getChecklistStateFieldOrderId());
			checklistStructureDto.setDescription(checklistStateField.getTemplateStructure().getDescription());
			checklistStructureDto.setFieldType(checklistStateField.getTemplateStructure().getFieldType());
			checklistStructureDto.setLowerLimit(checklistStateField.getTemplateStructure().getLowerLimit());
			checklistStructureDto.setUpperLimit(checklistStateField.getTemplateStructure().getUpperLimit());
			checklistStructureDto.setUnitOfMeasure(checklistStateField.getTemplateStructure().getUnitOfMeasure());
			checklistStructureDto.setTemplateId(checklistStateField.getTemplateStructure().getTemplateId());
			checklistStructureDto.setTemplateFieldId(checklistStateField.getTemplateStructure().getFieldId());
			checklistStructures.add(checklistStructureDto);
		}
		
		return checklistStructures;
	}
}
