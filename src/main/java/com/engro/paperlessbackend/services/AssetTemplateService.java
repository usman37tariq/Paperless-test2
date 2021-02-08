package com.engro.paperlessbackend.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.controllers.HierarchyController;
import com.engro.paperlessbackend.dao.TemplateDao;
import com.engro.paperlessbackend.dao.TemplateHierarchyDao;
import com.engro.paperlessbackend.dao.TemplateStructureDao;
import com.engro.paperlessbackend.dto.AssetTemplateAdditionDto;
import com.engro.paperlessbackend.dto.AssetTemplateDto;
import com.engro.paperlessbackend.dto.GetAssetTemplateStructureDto;
import com.engro.paperlessbackend.entities.Template;
import com.engro.paperlessbackend.entities.TemplateHierarchy;
import com.engro.paperlessbackend.entities.TemplateStructure;

@Component
public class AssetTemplateService {

	private static Logger logger = LoggerFactory.getLogger(HierarchyController.class);
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	TemplateStructureDao templateStructureDao;
	
	@Autowired
	TemplateDao templateDao;
	
	@Autowired
	TemplateHierarchyDao templateHierarchyDao;
	
	@Transactional
	public Object addAssetTemplate(AssetTemplateDto assetTemplate) {	
		TemplateHierarchy templateHierarchy = new TemplateHierarchy();
		Template newTemplate = new Template(); 
		try {
			Template template = new Template();
			template.setName(assetTemplate.getName());
			template.setDescription(assetTemplate.getDescription());
			newTemplate = templateDao.addTemplate(template);
			if(newTemplate == null) {
				return ResponseEntity.badRequest().body("Error occured while adding asset template");
			}
			
			logger.info("{Node ID] - [{}]", assetTemplate.getNodeId());
			logger.info("{Template ID] - [{}]", newTemplate.getId());
			
			templateHierarchy.setNodeId(assetTemplate.getNodeId());
			templateHierarchy.setTemplateId(newTemplate.getId());
			
			if(!templateHierarchyDao.addTemplateToHierarchyNode(templateHierarchy)) {
				return ResponseEntity.badRequest().body("Error while adding asset template");
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]",ex);
			return ResponseEntity.badRequest().body("Error occured while adding asset template");
		}
		return newTemplate;
	}
	
	@Transactional
	public Object addTemplateToAssetTemplateStructure(AssetTemplateAdditionDto assetTemplateDto) {
		List<TemplateStructure> retTemplateStructure = null;
		try {
			List<TemplateStructure> templateStructure = templateStructureDao.getTemplateStructureByTemplateId(assetTemplateDto.getTemplateId());
			
			List<TemplateStructure> assetTemplateStructure = new ArrayList<TemplateStructure>();
			for(TemplateStructure structure : templateStructure) {
				TemplateStructure structureField = new TemplateStructure();
				structureField.setDescription(structure.getDescription());
				structureField.setFieldType(structure.getFieldType());
				structureField.setLowerLimit(structure.getLowerLimit());
				structureField.setOrderId(structure.getOrderId());
				structureField.setTemplateId(assetTemplateDto.getAssetTemplateId());
				structureField.setUnitOfMeasure(structure.getUnitOfMeasure());
				structureField.setUpperLimit(structure.getUpperLimit());
				
				assetTemplateStructure.add(structureField);
			}
			retTemplateStructure = templateStructureDao.addTemplateToAssetTemplateStructure(assetTemplateStructure);
			
			retTemplateStructure = templateStructureDao.getTemplateStructureByTemplateId(assetTemplateDto.getAssetTemplateId());
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]",ex);
			return ResponseEntity.badRequest().body("Error occured while adding template");
		}
		return retTemplateStructure;
	}

	public Object getAssetTemplateStructureByNodeId(int nodeId) {
		return templateStructureDao.getAssetTemplateStructureByNodeId(nodeId);
	}

	public Object getAssetTemplateStructure(GetAssetTemplateStructureDto getAssetTemplateStructureDto) {
		return templateStructureDao.getAssetTemplateStructure(getAssetTemplateStructureDto);
	}
}
