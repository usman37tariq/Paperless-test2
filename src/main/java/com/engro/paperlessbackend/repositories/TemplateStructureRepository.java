package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.TemplateStructure;

@Repository
public interface TemplateStructureRepository extends JpaRepository<TemplateStructure, Integer> {

	@Modifying(clearAutomatically = true)
	@Query(value = "DELETE FROM tbl_template_structure WHERE template_id_fk = ?", nativeQuery = true)
	void deleteFromTemplateStructureByTemplateId(Integer templateId);

	@Query(value = "SELECT * FROM tbl_template_structure WHERE template_id_fk IN (SELECT template_id_fk FROM tbl_template_hierarchy WHERE node_id_fk = ?) ORDER BY field_id ASC", nativeQuery = true)
	List<TemplateStructure> getTemplateStructureByNodeId(Integer nodeId);

	List<TemplateStructure> findByTemplateIdOrderByFieldIdAsc(Integer templateId);
}
