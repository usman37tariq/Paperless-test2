package com.engro.paperlessbackend.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.Role;
import com.engro.paperlessbackend.repositories.RoleRepository;

@Component
public class RoleDao {

	private static Logger logger = LoggerFactory.getLogger(ResourceDao.class);

	@Autowired
	RoleRepository roleRepository;

	public List<Role> getRolesList() {
		List<Role> roles = null;
		try {
			roles = roleRepository.findAll();
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return roles;
	}

	public Role addNewRole(Role role) {
		Role savedRole = null;
		try {
			savedRole = roleRepository.save(role);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return savedRole;
	}

	public Role updateRole(Role role) {
		Role updatedRole = null;
		try {
			Optional<Role> currRole = roleRepository.findById(role.getRoleId());
			if (currRole.isPresent()) {
				Role rl = currRole.get();
				rl.setRoleName(role.getRoleName());
				rl.setRoleDescription(role.getRoleDescription());
				updatedRole = roleRepository.save(rl);
			}
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return updatedRole;
	}

	public Role getRoleByRoleId(Long roleId) {
		try {
			Optional<Role> role = roleRepository.findById(roleId);
			if (role.isPresent()) {
				logger.info("[SUCCESSFUL]");
				return role.get();
			}
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return null;
	}

	public boolean deleteRoleByRoleId(Long roleId) {
		try {
			roleRepository.deleteById(roleId);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
			return false;
		}
		return true;
	}
}
