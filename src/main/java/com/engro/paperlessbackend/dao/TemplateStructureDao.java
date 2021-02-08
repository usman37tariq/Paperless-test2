package com.engro.paperlessbackend.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dto.GetAssetTemplateStructureDto;
import com.engro.paperlessbackend.dto.TemplateDto;
import com.engro.paperlessbackend.entities.Template;
import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.repositories.TemplateRepository;
import com.engro.paperlessbackend.repositories.TemplateStructureRepository;

@Component
public class TemplateStructureDao {

	private static Logger logger = LoggerFactory.getLogger(TemplateStructureDao.class);

	@Autowired
	TemplateStructureRepository templateStructureRepository;

	@Autowired
	TemplateRepository templateRepository;

	public List<TemplateStructure> getTemplateStructureByTemplateId(int templateId) {
		List<TemplateStructure> templateStructure = null;
		try {
			templateStructure = templateStructureRepository.findByTemplateIdOrderByFieldIdAsc(templateId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return templateStructure;
	}

	public TemplateStructure getTemplateStructureByFieldId(int fieldId) {
		try {
			Optional<TemplateStructure> templateStructure = templateStructureRepository.findById(fieldId);
			logger.info("[Successful]");
			if (templateStructure.isPresent()) {
				return templateStructure.get();
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return null;
	}

	@Transactional
	public TemplateStructure addTemplateStructureField(TemplateStructure templateStructure) {
		TemplateStructure templateObj = null;
		try {
			templateObj = templateStructureRepository.save(templateStructure);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return templateObj;
	}

	@Transactional
	public TemplateStructure updateTemplateStructureField(TemplateStructure templateStructure) {
		TemplateStructure savedTemplateStructure = null;
		try {
			Optional<TemplateStructure> currField = templateStructureRepository
					.findById(templateStructure.getFieldId());
			if (currField.isPresent()) {
				TemplateStructure field = currField.get();
				field.setDescription(templateStructure.getDescription());
				field.setFieldType(templateStructure.getFieldType());
				field.setLowerLimit(templateStructure.getLowerLimit());
				field.setUpperLimit(templateStructure.getUpperLimit());
				field.setOrderId(templateStructure.getOrderId());
				field.setUnitOfMeasure(templateStructure.getUnitOfMeasure());
				savedTemplateStructure = templateStructureRepository.save(field);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedTemplateStructure;
	}

	public boolean deleteTemplateStructureFieldById(int fieldId) {
		try {
			templateStructureRepository.deleteById(fieldId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public boolean deleteFromTemplateStructureByTemplateId(int id) {
		try {
			templateStructureRepository.deleteFromTemplateStructureByTemplateId(id);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public TemplateDto getAssetTemplateStructureByNodeId(int nodeId) {
		TemplateDto templateDto = new TemplateDto();
		try {
			List<Template> template = templateRepository.findAssetTemplateByNodeId(nodeId);
			if (template.isEmpty()) {
				return null;
			}
			List<TemplateStructure> templateStructure = templateStructureRepository
					.getTemplateStructureByNodeId(nodeId);
			templateDto.setId(template.get(0).getId());
			templateDto.setName(template.get(0).getName());
			templateDto.setDescription(template.get(0).getDescription());
			if (!templateStructure.isEmpty()) {
				templateDto.setTemplateStructures(templateStructure);
			}
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return null;
		}
		return templateDto;
	}

	public Object getAssetTemplateStructure(GetAssetTemplateStructureDto getAssetTemplateStructureDto) {
		List<TemplateStructure> templateStructure = new ArrayList<TemplateStructure>();
		List<Object> obj = null;
		try {
			List<Template> template = templateRepository
					.findTemplateByName(getAssetTemplateStructureDto.getTemplateName());
			if (template.isEmpty()) {
				return ResponseEntity.badRequest().body("Template with given name doesnot exist");
			}
			templateStructure = templateStructureRepository
					.getTemplateStructureByNodeId(getAssetTemplateStructureDto.getNodeId());
			obj.add(template);
			obj.add(templateStructure);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while retrieving template details");
		}
		return obj;
	}

	@Transactional
	public List<TemplateStructure> addTemplateToAssetTemplateStructure(List<TemplateStructure> assetTemplateStructure) {
		List<TemplateStructure> savedAssetTemplateStructure = null;

		try {
			savedAssetTemplateStructure = templateStructureRepository.saveAll(assetTemplateStructure);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedAssetTemplateStructure;
	}
}
