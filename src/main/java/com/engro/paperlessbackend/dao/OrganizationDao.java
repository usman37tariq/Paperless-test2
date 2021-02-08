package com.engro.paperlessbackend.dao;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.Organization;
import com.engro.paperlessbackend.repositories.OrganizationRepository;

@Component
public class OrganizationDao {

	private static Logger logger = LoggerFactory.getLogger(OrganizationDao.class);

	@Autowired
	OrganizationRepository organizationRepository;

	public Organization getOrganizationByOrganizationId(String orgId) {
		try {
			Optional<Organization> org = organizationRepository.findById(orgId);
			logger.info("[SUCCESSFUL]");
			if (org.isPresent()) {
				return org.get();
			}
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return new Organization();
	}
}
