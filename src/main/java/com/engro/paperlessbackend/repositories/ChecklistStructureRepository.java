package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistStructure;
import com.engro.paperlessbackend.entities.TemplateStructure;

@Repository
public interface ChecklistStructureRepository extends JpaRepository<ChecklistStructure, Integer> {

	@Modifying(clearAutomatically = true)
	@Query(value = "DELETE FROM tbl_checklist_structure WHERE checklist_id_fk = ? AND template_field_id_fk IN (SELECT field_id FROM tbl_template_structure WHERE template_id_fk IN "
			+ "(SELECT template_id_fk FROM tbl_template_hierarchy WHERE node_id_fk = ?))", nativeQuery = true)
	void deleteFromChecklistStructureByNodeIdAndChecklistId(Integer checklistId, Integer nodeId);

	List<ChecklistStructure> findByTemplateStructure(TemplateStructure templateField);

	List<ChecklistStructure> findByChecklistIdOrderByUpdatedAtAscChecklistFieldIdAsc(Checklist chk);

	@Modifying(clearAutomatically = true)
	void deleteByTemplateStructure(TemplateStructure templateField);
}
