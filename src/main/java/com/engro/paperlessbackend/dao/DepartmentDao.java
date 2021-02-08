package com.engro.paperlessbackend.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.Department;
import com.engro.paperlessbackend.repositories.DepartmentRepository;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class DepartmentDao {

	private static Logger logger = LoggerFactory.getLogger(DepartmentDao.class);

	@Autowired
	DepartmentRepository departmentRepository;

	public Department getDepartmentByDepartmentId(int deptId) {
		try {
			Optional<Department> dept = departmentRepository.findById(deptId);
			logger.info("[Successful]");
			if (dept.isPresent()) {
				return dept.get();
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return null;
	}

	public List<Department> getDepartmentsList() {
		List<Department> departmentList = null;
		try {
			departmentList = departmentRepository.findByIsDeletedOrderByNameAsc(Constants.DEPARTMENT_IS_NOT_DELETED);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return departmentList;
	}

	@Transactional
	public Department addDepartment(Department department) {
		Department dept = null;
		try {
			dept = departmentRepository.save(department);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dept;
	}

	public Department updateDepartment(Department department) {
		Department savedDept = null;
		try {
			Optional<Department> dept = departmentRepository.findById(department.getId());
			if (dept.isPresent()) {
				department.setId(dept.get().getId());
				department.setOrganization(department.getOrganization());
				savedDept = departmentRepository.save(department);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedDept;
	}

	public boolean deleteDepartmentByDepartmentId(int departmentId) {
		try {
//			departmentRepository.deleteByDepartmentId(departmentId);
			departmentRepository.deleteById(departmentId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public boolean isDepartmentNameExists(Department department) {
		List<Department> departments = new ArrayList<Department>();
		try {
			departments = departmentRepository.isDepartmentNameExists(department.getId(), department.getName());
			logger.info("[Successful]");
			if (!departments.isEmpty()) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}
}
