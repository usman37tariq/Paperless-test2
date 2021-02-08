package com.engro.paperlessbackend.services;

import java.lang.reflect.Type;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dao.ChecklistDao;
import com.engro.paperlessbackend.dao.DepartmentDao;
import com.engro.paperlessbackend.dao.SectionDao;
import com.engro.paperlessbackend.dao.UsersDao;
import com.engro.paperlessbackend.dto.SectionDto;
import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.Department;
import com.engro.paperlessbackend.entities.Section;
import com.engro.paperlessbackend.entities.Users;

@Component
public class SectionService {

	private static Logger logger = LoggerFactory.getLogger(SectionService.class);

	@Autowired
	SectionDao sectionDao;

	@Autowired
	DepartmentDao departmentDao;

	@Autowired
	UsersDao usersDao;

	@Autowired
	ChecklistDao checklistDao;

	@Autowired
	ModelMapper modelMapper;

	public Object getSectionsListByDepartmentId(Integer id) {
		Object sectionDtoList = null;
		try {
			Type listType = new TypeToken<List<SectionDto>>() {
			}.getType();
			Object sections = sectionDao.getSectionsListByDepartmentId(id);
			sectionDtoList = modelMapper.map(sections, listType);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while retrieving sections list");
		}
		return sectionDtoList;
	}

	public Object getSectionsList() {
		return sectionDao.getSectionsList();
	}

	public Object addSection(Section section) {
		Section newSection = null;
		try {
			if (sectionDao.isSectionNameAlreadyExists(section)) {
				return ResponseEntity.badRequest().body("Section name already exists");
			}
			Department dep = departmentDao.getDepartmentByDepartmentId(section.getDepartment().getId());
			section.setDepartment(dep);
			newSection = sectionDao.addSection(section);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error: while adding section");
		}
		return newSection;
	}

	@Transactional
	public ResponseEntity<String> updateSection(Section section) {
		try {
			if (sectionDao.isSectionNameAlreadyExists(section)) {
				return ResponseEntity.badRequest().body("Section name already exists");
			}
			Department dep = departmentDao.getDepartmentByDepartmentId(section.getDepartment().getId());
			section.setDepartment(dep);
			sectionDao.updateSection(section);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error: while updating section");
		}
		return ResponseEntity.ok().body("Section Updated Successfully");
	}

	@Transactional
	public ResponseEntity<String> deleteSectionBySectionId(int sectionId) {

		Section section = sectionDao.getSectionBySectionId(sectionId);
		if (section == null) {
			logger.error("No section found in DB against section ID :[{}]", sectionId);
			return ResponseEntity.badRequest().body("Error occured while deleting section");
		}
		String errMsg = "Deletion is NOT allowed, ";

		List<Users> users = usersDao.getUsersBySectionId(section);
		if (users != null && !users.isEmpty()) {
			errMsg += "this section contains user(s): ";
			for (int i = 0; i < users.size(); i++) {
				errMsg += users.get(i).getUserName();
				if (i < users.size() - 1) {
					errMsg += ",";
				}
			}
			logger.error("[Section ID : {}] - [{}]", sectionId, errMsg);
			return ResponseEntity.badRequest().body(errMsg);
		}

		List<Checklist> checklists = checklistDao.getChecklistsBySectionId(section);
		if (checklists != null && !checklists.isEmpty()) {
			errMsg += "this section contains checklist(s): ";
			for (int i = 0; i < checklists.size(); i++) {
				errMsg += checklists.get(i).getName();
				if (i < checklists.size() - 1) {
					errMsg += ",";
				}
			}
			logger.error("[Section ID : {}] - [{}]", sectionId, errMsg);
			return ResponseEntity.badRequest().body(errMsg);
		}

		if (!sectionDao.deleteSectionBySectionId(sectionId)) {
			logger.error("Error occured while deleting from tbl_section against Section ID : [{}]", sectionId);
			return ResponseEntity.badRequest().body("Section Deletion Failed!");
		}
		logger.info("[Successful]");
		return ResponseEntity.ok().body("Section deleted successfully");
	}
}
