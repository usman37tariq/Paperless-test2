package com.engro.paperlessbackend.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.Template;
import com.engro.paperlessbackend.repositories.TemplateRepository;

@Component
public class TemplateDao {

	private static Logger logger = LoggerFactory.getLogger(TemplateDao.class);

	@Autowired
	TemplateRepository templateRepository;

	@Autowired
	TemplateStructureDao templateStructureDao;

	@Autowired
	TemplateHierarchyDao templateHierarchyDao;

	@Transactional
	public Template addTemplate(Template template) {
		Template newTemplate = null;
		try {
			newTemplate = templateRepository.save(template);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return newTemplate;
	}

	public List<Template> getAllTemplates() {
		List<Template> templates = null;
		try {
			templates = templateRepository.getAllTemplates();
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return templates;
	}

	public List<Template> getAllAssetTemplates() {
		List<Template> assetTemplates = null;
		try {
			assetTemplates = templateRepository.getAllAssetTemplates();
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return assetTemplates;
	}

	public Template getTemplateById(int id) {
		try {
			Optional<Template> template = templateRepository.findById(id);
			logger.info("[Successful]");
			if (template.isPresent()) {
				return template.get();
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return null;
	}

	@Transactional
	public Template updateTemplate(Template template) {
		Template savedTemplate = null;
		try {
			Optional<Template> currTemplate = templateRepository.findById(template.getId());
			if (currTemplate.isPresent()) {
				Template tmp = currTemplate.get();
				tmp.setName(template.getName());
				tmp.setDescription(template.getDescription());
				savedTemplate = templateRepository.save(tmp);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedTemplate;
	}

	@Transactional
	public ResponseEntity<String> deleteTemplateById(int templateId) {
		try {
			Optional<Template> currTemplate = templateRepository.findById(templateId);
			if(!currTemplate.isPresent()) {
				logger.error("No Template found with ID : [{}]", templateId);
				return ResponseEntity.badRequest().body("Template deletion failed");
			}
			if (!templateStructureDao.deleteFromTemplateStructureByTemplateId(currTemplate.get().getId())) {
				logger.error("Error occured while deleting template structure");
				return ResponseEntity.badRequest().body("Template deletion failed");
			}
			if (!templateHierarchyDao.deleteTemplateFromHierarchyByTemplateId(currTemplate.get().getId())) {
				logger.error("Error occured while deleting template attached to hierarchy");
				return ResponseEntity.badRequest().body("Template deletion failed");
			}
			templateRepository.deleteById(currTemplate.get().getId());
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Template deletion failed");
		}
		return ResponseEntity.ok().body("Template Deleted Successfully");
	}

	public Template findAssetTemplateByNodeId(int nodeId) {
		List<Template> assetTemplate = null;

		try {
			assetTemplate = templateRepository.findAssetTemplateByNodeId(nodeId);
			logger.info("[Successful]");
			if (assetTemplate != null && !assetTemplate.isEmpty()) {
				return assetTemplate.get(0);
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return null;
	}
}
