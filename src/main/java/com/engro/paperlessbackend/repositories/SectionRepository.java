package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Department;
import com.engro.paperlessbackend.entities.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE tbl_section SET is_deleted = 1 WHERE department_id_fk = ?", nativeQuery = true)
	void deleteByDepartmentId(Integer departmentId);

	@Query(value = "SELECT * FROM (SELECT * FROM tbl_section WHERE section_id != ?)temp WHERE section_name = ? AND is_deleted = 0", nativeQuery = true)
	List<Section> isSectionNameAlreadyExists(Integer sectionId, String sectionName);

	List<Section> findByDepartmentAndIsDeletedOrderBySectionNameAsc(Department dep, Integer checklistIsNotDeleted);

	List<Section> findByIsDeletedOrderBySectionNameAsc(Integer sectionIsNotDeleted);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE tbl_section SET is_deleted = 1 WHERE section_id = ?", nativeQuery = true)
	void deleteBySectionId(Integer sectionId);
}
