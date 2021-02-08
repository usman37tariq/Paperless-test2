package com.engro.paperlessbackend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dao.TemplateDao;
import com.engro.paperlessbackend.entities.Template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TemplateService {

	private static Logger logger = LoggerFactory.getLogger(TemplateService.class);

	@Autowired
	TemplateDao templateDao;

	@Autowired
	ModelMapper modelMapper;

	public Object addTemplate(Template template) {
		Template newTemplate = new Template();
		try {
			if (template.getName().equals("")) {
				return ResponseEntity.badRequest().body("Template Name Cannot be Empty");
			}
			newTemplate = templateDao.addTemplate(template);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while adding template");
		}
		return newTemplate;
	}

	public Object getAllTemplates() {
		return templateDao.getAllTemplates();
	}

	public Object getAllAssetTemplates() {
		return templateDao.getAllAssetTemplates();
	}

	public Object updateTemplate(Template template) {
		Template updatedTemplate = new Template();
		try {
			if (template.getName().equals("")) {
				return ResponseEntity.badRequest().body("Template Name Cannot be Empty");
			}
			updatedTemplate = templateDao.updateTemplate(template);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while updating template");
		}
		return updatedTemplate;
	}

	public Object getTemplateById(int id) {
		return templateDao.getTemplateById(id);
	}

	public ResponseEntity<String> deleteTemplateById(int templateId) {
		return templateDao.deleteTemplateById(templateId);
	}
}
