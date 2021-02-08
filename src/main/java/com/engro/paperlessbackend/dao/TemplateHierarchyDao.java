package com.engro.paperlessbackend.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.TemplateHierarchy;
import com.engro.paperlessbackend.repositories.TemplateHierarchyRepository;

@Component
public class TemplateHierarchyDao {

	private static Logger logger = LoggerFactory.getLogger(TemplateHierarchyDao.class);

	@Autowired
	TemplateHierarchyRepository templateHierarchyRepository;

	@Transactional
	public boolean addTemplateToHierarchyNode(TemplateHierarchy templateHierarchy) {
		try {

			List<TemplateHierarchy> nodeTemplate = templateHierarchyRepository
					.findByTemplateIdAndNodeId(templateHierarchy.getTemplateId(), templateHierarchy.getNodeId());
			if (nodeTemplate == null || nodeTemplate.isEmpty()) {
				templateHierarchyRepository.save(templateHierarchy);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public boolean deleteTemplateFromHierarchyByTemplateId(int templateId) {
		try {
			templateHierarchyRepository.deleteByTemplateId(templateId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}
}
