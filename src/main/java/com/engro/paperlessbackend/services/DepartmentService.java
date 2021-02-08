package com.engro.paperlessbackend.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dao.ChecklistDao;
import com.engro.paperlessbackend.dao.DepartmentDao;
import com.engro.paperlessbackend.dao.OrganizationDao;
import com.engro.paperlessbackend.dao.SectionDao;
import com.engro.paperlessbackend.dao.UsersDao;
import com.engro.paperlessbackend.entities.Department;
import com.engro.paperlessbackend.entities.Organization;
import com.engro.paperlessbackend.entities.Section;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class DepartmentService {

	private static Logger logger = LoggerFactory.getLogger(DepartmentService.class);

	@Autowired
	DepartmentDao departmentDao;

	@Autowired
	SectionDao sectionDao;

	@Autowired
	OrganizationDao organizationDao;

	@Autowired
	UsersDao usersDao;

	@Autowired
	ChecklistDao checklistDao;

	public Object getDepartmentsList() {
		return departmentDao.getDepartmentsList();
	}

	@Transactional
	public Object addDepartment(Department department) {
		Object newDept = null;
		try {
			if (departmentDao.isDepartmentNameExists(department)) {
				return ResponseEntity.badRequest().body("Department name already exists");
			}
			Organization org = organizationDao.getOrganizationByOrganizationId(Constants.ORGANIZATION_ID);
			department.setOrganization(org);
			newDept = departmentDao.addDepartment(department);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error: while adding department");
		}
		return newDept;
	}

	@Transactional
	public Object updateDepartment(Department department) {
		Department updatedDept = null;
		try {
			if (departmentDao.isDepartmentNameExists(department)) {
				return ResponseEntity.badRequest().body("Department name already exists");
			}
			Organization org = organizationDao.getOrganizationByOrganizationId(Constants.ORGANIZATION_ID);
			department.setOrganization(org);
			updatedDept = departmentDao.updateDepartment(department);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error: while updating department");
		}
		return updatedDept;
	}

	@Transactional
	public ResponseEntity<String> deleteDepartmentByDepartmentId(int departmentId) {

		Department department = departmentDao.getDepartmentByDepartmentId(departmentId);
		if (department == null) {
			logger.error("No section found in DB against department ID :[{}]", departmentId);
			return ResponseEntity.badRequest().body("Error occured while deleting department");
		}
		
		String errMsg = "Deletion is NOT allowed, ";
		
		List<Section> sections = sectionDao.getSectionsListByDepartmentId(departmentId);
		if(sections != null && !sections.isEmpty()) {
			errMsg += "this department contains section(s): ";
			for (int i = 0; i < sections.size(); i++) {
				errMsg += sections.get(i).getSectionName();
				if (i < sections.size() - 1) {
					errMsg += ",";
				}
			}
			logger.error("[Section ID : {}] - [{}]", departmentId, errMsg);
			return ResponseEntity.badRequest().body(errMsg);
		}

//		List<Users> users = usersDao.getUsersByDepartment(department);
//		if (users != null && !users.isEmpty()) {
//			errMsg += "this department contains user(s): ";
//			for (int i = 0; i < users.size(); i++) {
//				errMsg += users.get(i).getId();
//				if (i < users.size() - 1) {
//					errMsg += ",";
//				}
//			}
//			logger.error("[Section ID : {}] - [{}]", departmentId, errMsg);
//			return ResponseEntity.badRequest().body(errMsg);
//		}

//		List<Checklist> checklists = checklistDao.getChecklistsByDepartment(department);
//		if (checklists != null && !checklists.isEmpty()) {
//			errMsg += "this department contains checklist(s): ";
//			for (int i = 0; i < checklists.size(); i++) {
//				errMsg += checklists.get(i).getName();
//				if (i < checklists.size() - 1) {
//					errMsg += ",";
//				}
//			}
//			logger.error("[Section ID : {}] - [{}]", departmentId, errMsg);
//			return ResponseEntity.badRequest().body(errMsg);
//		}
		
		if (!departmentDao.deleteDepartmentByDepartmentId(departmentId)) {
			return ResponseEntity.badRequest().body("Department Deletion Failed!");
		}
		return ResponseEntity.ok().body("Department deleted successfully");
	}
}
