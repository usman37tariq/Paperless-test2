package com.engro.paperlessbackend.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.Role;
import com.engro.paperlessbackend.entities.RoleResource;
import com.engro.paperlessbackend.repositories.RoleResourceRepository;

@Component
public class RoleResourceDao {

	private static Logger logger = LoggerFactory.getLogger(RoleResourceDao.class);

	@Autowired
	RoleResourceRepository roleResourceRepository;

	public List<RoleResource> addRoleResources(List<RoleResource> roleResources) {
		List<RoleResource> savedRoleResources = null;

		try {
			savedRoleResources = roleResourceRepository.saveAll(roleResources);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return savedRoleResources;
	}

	public List<RoleResource> getRoleResourcesByRole(Role role) {
		List<RoleResource> roleResources = null;

		try {
			roleResources = roleResourceRepository.findByRole(role);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return roleResources;
	}

	public boolean deleteRoleResourcesByRole(Role role) {
		try {
			roleResourceRepository.deleteByRole(role);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
			return false;
		}
		return true;
	}
}
