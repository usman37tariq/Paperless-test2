package com.engro.paperlessbackend.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dao.ResourceDao;
import com.engro.paperlessbackend.dao.RoleDao;
import com.engro.paperlessbackend.dao.RoleResourceDao;
import com.engro.paperlessbackend.dao.UsersDao;
import com.engro.paperlessbackend.dto.RoleDto;
import com.engro.paperlessbackend.entities.Resource;
import com.engro.paperlessbackend.entities.Role;
import com.engro.paperlessbackend.entities.RoleResource;
import com.engro.paperlessbackend.entities.Users;

@Component
public class RoleService {

	private static Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	RoleDao roleDao;

	@Autowired
	ResourceDao resourceDao;

	@Autowired
	RoleResourceDao roleResourceDao;

	@Autowired
	UsersDao usersDao;

	public Object getRolesList() {

		List<RoleDto> response = new ArrayList<RoleDto>();
		List<Role> rolesList = roleDao.getRolesList();

		for (Role rl : rolesList) {
			RoleDto roleDto = new RoleDto();
			roleDto.setRole(rl);
			roleDto.setResources(roleResourceDao.getRoleResourcesByRole(rl));
			response.add(roleDto);
		}
		return response;
	}

	@Transactional
	public Object addNewRole(RoleDto roleDto) {

		RoleDto response = new RoleDto();

		Role role = roleDto.getRole();
		List<RoleResource> resources = roleDto.getResources();
		Role newRole = roleDao.addNewRole(role);

		if (newRole != null) {

			response.setRole(newRole);
			List<RoleResource> roleResources = new ArrayList<RoleResource>();

			for (RoleResource rc : resources) {

				RoleResource roleResource = new RoleResource();
				Resource resource = resourceDao.getResourceByResourceId(rc.getResource().getResourceId());

				roleResource.setRole(newRole);
				roleResource.setResource(resource);
				roleResource.setAdd(rc.getAdd());
				roleResource.setDelete(rc.getDelete());
				roleResource.setEdit(rc.getEdit());
				roleResource.setRead(rc.getRead());

				roleResources.add(roleResource);
			}
			roleResources = roleResourceDao.addRoleResources(roleResources);
			response.setResources(roleResources);
		} else {
			/**
			 * return error
			 */
		}

		return response;
	}

	@Transactional
	public Object updateRole(RoleDto roleDto) {

		RoleDto response = new RoleDto();

		Role role = roleDto.getRole();
		List<RoleResource> resources = roleDto.getResources();
		Role updatedRole = roleDao.updateRole(role);

		if (updatedRole != null) {

			response.setRole(updatedRole);
			List<RoleResource> roleResources = new ArrayList<RoleResource>();

			if (!roleResourceDao.deleteRoleResourcesByRole(updatedRole)) {
				logger.error("Error occured while deleting the existing role resources information");
				return ResponseEntity.badRequest().body("Error: while updating role");
			}

			for (RoleResource rc : resources) {

				RoleResource roleResource = new RoleResource();
				Resource resource = resourceDao.getResourceByResourceId(rc.getResource().getResourceId());

				roleResource.setRole(updatedRole);
				roleResource.setResource(resource);
				roleResource.setAdd(rc.getAdd());
				roleResource.setDelete(rc.getDelete());
				roleResource.setEdit(rc.getEdit());
				roleResource.setRead(rc.getRead());

				roleResources.add(roleResource);
			}
			roleResources = roleResourceDao.addRoleResources(roleResources);
			response.setResources(roleResources);
		} else {
			logger.error("Updating role returned NULL");
			return ResponseEntity.badRequest().body("Error: while updating role");
		}
		return response;
	}

	@Transactional
	public Object deleteRoleByRoleId(Long roleId) {

		Role role = roleDao.getRoleByRoleId(roleId);
		List<Users> users = usersDao.getUsersByRole(role);

		if (users != null && !users.isEmpty()) {
			String errMsg = "Role cannot be deleted it is attached to the user(s): ";
			for (int i = 0; i < users.size(); i++) {
				errMsg += users.get(i).getUserName();
				if (i < (users.size() - 1)) {
					errMsg += ",";
				}
			}
			return ResponseEntity.badRequest().body(errMsg);
		}

		if (!roleResourceDao.deleteRoleResourcesByRole(role)) {
			logger.error("Role Resources Deletion failed from (tbl_role_resource)");
			return ResponseEntity.badRequest().body("Error occured while deleting role");
		}

		if (!roleDao.deleteRoleByRoleId(roleId)) {
			logger.error("Role Resources Deletion failed from (tbl_role)");
			return ResponseEntity.badRequest().body("Error occured while deleting role");
		}
		return ResponseEntity.ok().body("Role deleted successfully");
	}

	public Object getRoleByRoleId(Long roleId) {
		RoleDto roleDto = new RoleDto();
		Role role = roleDao.getRoleByRoleId(roleId);
		if (role != null) {
			roleDto.setRole(role);
			roleDto.setResources(roleResourceDao.getRoleResourcesByRole(role));
		}
		return roleDto;
	}

}
