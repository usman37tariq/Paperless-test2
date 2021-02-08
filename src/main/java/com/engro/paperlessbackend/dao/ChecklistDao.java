package com.engro.paperlessbackend.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.dto.ChecklistDto;
import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.Section;
import com.engro.paperlessbackend.repositories.ChecklistRepository;
import com.engro.paperlessbackend.repositories.TemplateStructureRepository;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class ChecklistDao {

	private static Logger logger = LoggerFactory.getLogger(ChecklistDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	ChecklistRepository checklistRepository;

	@Autowired
	TemplateStructureRepository templateStructureRepository;

	@Autowired
	DepartmentDao departmentDao;

	@Autowired
	SectionDao sectionDao;

	@Transactional
	public Checklist addChecklist(Checklist checklist) {
		Checklist savedChecklist = null;
		try {
			savedChecklist = checklistRepository.save(checklist);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedChecklist;
	}

	public List<Checklist> getAllChecklists() {
		List<Checklist> checklists = null;
		try {
			checklists = checklistRepository.findByIsDeletedOrderByNameAsc(Constants.CHECKLIST_IS_NOT_DELETED);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return checklists;
	}

	@Transactional
	public Checklist updateChecklist(Checklist checklist) {
		Checklist savedChecklist = null;
		try {
			Optional<Checklist> currChecklist = checklistRepository.findById(checklist.getChecklistId());
			if (currChecklist.isPresent()) {
				Checklist chk = currChecklist.get();
//				Department department = departmentDao.getDepartmentByDepartmentId(checklist.getDepartment().getId());
				Section section = sectionDao.getSectionBySectionId(checklist.getSection().getSectionId());
				chk.setName(checklist.getName());
				chk.setDescription(checklist.getDescription());
//				chk.setDepartment(department);
				chk.setSection(section);
				savedChecklist = checklistRepository.save(chk);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedChecklist;
	}

	public Checklist getChecklistByChecklistId(int checklistId) {
		try {
			Optional<Checklist> checklist = checklistRepository.findById(checklistId);
			logger.info("[Successful]");
			if (checklist.isPresent()) {
				return checklist.get();
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return null;
	}

	@Transactional
	public ResponseEntity<String> deleteChecklistByChecklistId(int checklistId) {
		try {
			checklistRepository.deleteChecklistByChecklistId(checklistId);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Exception occured while deleting checklist");
		}
		return ResponseEntity.ok().body("Checklist Deleted Successfully");
	}

	@Transactional
	public Checklist updateChecklistSchedulingByChecklistId(ChecklistDto checklistDto) {
		Checklist savedChecklist = null;
		try {
			Optional<Checklist> currChecklist = checklistRepository
					.findById(checklistDto.getChecklist().getChecklistId());
			if (currChecklist.isPresent()) {
				Checklist chk = currChecklist.get();
				chk.setStartTime(checklistDto.getChecklist().getStartTime());
				chk.setEndTime(checklistDto.getChecklist().getEndTime());
				chk.setFrequency(checklistDto.getChecklist().getFrequency());
				chk.setUnit(checklistDto.getChecklist().getUnit());
				savedChecklist = checklistRepository.save(chk);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedChecklist;
	}

	@Transactional
	public boolean setActivationStatus(int checklistId) {
		try {
			checklistRepository.setActivationStatus(checklistId);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	@Transactional
	public ResponseEntity<String> deActivateChecklistByChecklistId(int checklistId) {
		try {
			checklistRepository.reSetActivationStatus(checklistId);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Checklist De-Activation Failed!");
		}
		return ResponseEntity.badRequest().body("Checklist De-Activated!");
	}

//	@Transactional
//	public boolean deleteChecklistByDepartmentId(int departmentId) {
//		try {
//			checklistRepository.deleteChecklistByDepartmentId(departmentId);
//			logger.info("[SUCCESSFUL]");
//		} catch (Exception ex) {
//			logger.error("[Exception] - [{}]", ex);
//			return false;
//		}
//		return true;
//	}

	@Transactional
	public boolean deleteChecklistBySectionId(int SectionId) {
		try {
			checklistRepository.deleteChecklistBySectionId(SectionId);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public List<Checklist> getChecklistsBySectionId(Section section) {
		List<Checklist> checklists = null;
		try {
			checklists = checklistRepository.findBySectionAndIsDeleted(section, Constants.CHECKLIST_IS_NOT_DELETED);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return checklists;
	}

//	public List<Checklist> getChecklistsByDepartment(Department department) {
//		List<Checklist> checklists = null;
//		try {
//			checklists = checklistRepository.findByDepartmentAndIsDeleted(department,
//					Constants.CHECKLIST_IS_NOT_DELETED);
//			logger.info("[SUCCESSFUL]");
//		} catch (Exception ex) {
//			logger.error("[Exception] - [{}]", ex);
//		}
//		return checklists;
//	}

	public List<Map<String, Object>> getChecklistsLinkedToNodeByNodeId(int nodeId) {
		String SQL_TO_GET_CHECKLISTS_FOR_NODE_IDS = ""
				+ "SELECT * FROM tbl_checklist WHERE is_deleted = 0 AND checklist_id IN " + "( "
				+ "SELECT DISTINCT checklist_id_fk FROM tbl_checklist_structure WHERE template_field_id_fk IN " + "( "
				+ "SELECT field_id FROM " + "tbl_template_structure WHERE template_id_fk IN " + "( "
				+ "SELECT template_id_fk FROM tbl_template_hierarchy WHERE node_id_fk IN " + "( " + "( "
				+ "Select node_id from " + "( " + "WITH RECURSIVE tree AS ( SELECT node_id, node_active_status , "
				+ "ARRAY[]::INTEGER[] AS ancestors, ARRAY[]::INTEGER[] AS ancestors_active_status FROM tbl_hierarchy "
				+ "WHERE node_parent_id =-1 UNION ALL SELECT tbl_hierarchy.node_id "
				+ ", tbl_hierarchy.node_active_status, tree.ancestors || "
				+ "tbl_hierarchy.node_parent_id, tree.ancestors_active_status || "
				+ "tbl_hierarchy.node_active_status FROM tbl_hierarchy, tree WHERE "
				+ "tbl_hierarchy.node_parent_id = tree.node_id ) SELECT node_id FROM " + "tree WHERE " + nodeId
				+ "= ANY(tree.ancestors) " + ") as temp " + "union " + "select " + nodeId + ") " + ") " + " " + ") "
				+ ") " + ");";

		logger.info("[{}]", SQL_TO_GET_CHECKLISTS_FOR_NODE_IDS);

		return jdbcTemplate.queryForList(SQL_TO_GET_CHECKLISTS_FOR_NODE_IDS);
	}
}
