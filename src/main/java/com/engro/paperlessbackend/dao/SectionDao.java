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
import com.engro.paperlessbackend.entities.Section;
import com.engro.paperlessbackend.repositories.SectionRepository;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class SectionDao {

	private static Logger logger = LoggerFactory.getLogger(SectionDao.class);

	@Autowired
	SectionRepository sectionRepository;

	public List<Section> getSectionsListByDepartmentId(int departmentId) {
		List<Section> sectionList = null;
		try {
			Department dep = new Department();
			dep.setId(departmentId);
			sectionList = sectionRepository.findByDepartmentAndIsDeletedOrderBySectionNameAsc(dep,
					Constants.SECTION_IS_NOT_DELETED);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return sectionList;
	}

	public Section getSectionBySectionId(int sectionId) {
		try {
			Optional<Section> section = sectionRepository.findById(sectionId);
			logger.info("[Successful]");
			if (section.isPresent()) {
				return section.get();
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return null;
	}

	@Transactional
	public boolean deleteSectionsByDepartmentId(int departmentId) {
		try {
			sectionRepository.deleteByDepartmentId(departmentId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public List<Section> getSectionsList() {
		List<Section> sections = null;
		try {
			sections = sectionRepository.findByIsDeletedOrderBySectionNameAsc(Constants.SECTION_IS_NOT_DELETED);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return sections;
	}

	public boolean isSectionNameAlreadyExists(Section section) {
		List<Section> sections = new ArrayList<Section>();
		try {
			sections = sectionRepository.isSectionNameAlreadyExists(section.getSectionId(), section.getSectionName());
			logger.info("[Successful]");
			if (!sections.isEmpty()) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	@Transactional
	public Section addSection(Section section) {
		Section savedSection = null;
		try {
			savedSection = sectionRepository.save(section);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedSection;
	}

	@Transactional
	public Section updateSection(Section section) {
		Section savedSection = null;
		try {
			Optional<Section> currSection = sectionRepository.findById(section.getSectionId());
			if (currSection.isPresent()) {
				section.setSectionId(currSection.get().getSectionId());
				savedSection = sectionRepository.save(section);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedSection;
	}

	public boolean deleteSectionBySectionId(int sectionId) {
		try {
//			sectionRepository.deleteBySectionId(sectionId);
			sectionRepository.deleteById(sectionId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}
}
