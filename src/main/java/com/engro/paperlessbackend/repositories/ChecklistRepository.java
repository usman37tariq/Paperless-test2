package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.Section;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {

	@Modifying(clearAutomatically = true)
//	@Query(value = "UPDATE tbl_checklist SET is_deleted = 1, section_id_fk = null, department_id_fk = null WHERE checklist_id = ?", nativeQuery = true)
	@Query(value = "UPDATE tbl_checklist SET is_deleted = 1, section_id_fk = null WHERE checklist_id = ?", nativeQuery = true)
	void deleteChecklistByChecklistId(Integer checklistId);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE tbl_checklist SET activation_status = 'ACTIVE' WHERE checklist_id = ?", nativeQuery = true)
	void setActivationStatus(Integer checklistId);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE tbl_checklist SET activation_status = 'PENDING' WHERE checklist_id = ?", nativeQuery = true)
	void reSetActivationStatus(Integer checklistId);

//	@Modifying(clearAutomatically = true)
//	@Query(value = "UPDATE tbl_checklist SET is_deleted = 1, activation_status = 'PENDING' WHERE department_id_fk = ?", nativeQuery = true)
//	void deleteChecklistByDepartmentId(Integer departmentId);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE tbl_checklist SET is_deleted = 1, activation_status = 'PENDING' WHERE section_id_fk = ?", nativeQuery = true)
	void deleteChecklistBySectionId(Integer sectionId);

	List<Checklist> findBySectionAndIsDeleted(Section section, Integer checklistIsNotDeleted);

//	List<Checklist> findByDepartmentAndIsDeleted(Department department, Integer checklistIsNotDeleted);

	List<Checklist> findByIsDeletedOrderByNameAsc(Integer checklistIsNotDeleted);
}
