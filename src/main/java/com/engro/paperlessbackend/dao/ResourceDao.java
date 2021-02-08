package com.engro.paperlessbackend.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.Resource;
import com.engro.paperlessbackend.repositories.ResourceRepository;

@Component
public class ResourceDao {

	private static Logger logger = LoggerFactory.getLogger(ResourceDao.class);

	@Autowired
	ResourceRepository resourceRepository;

	public List<Resource> getAllResources() {
		List<Resource> resources = null;
		try {
			resources = resourceRepository.findAll();
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return resources;
	}

	public Resource getResourceByResourceId(int resourceId) {
		try {
			Optional<Resource> resource = resourceRepository.findById(resourceId);
			logger.info("[Successful]");
			if (resource.isPresent()) {
				return resource.get();
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return null;
	}
}
