package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {

	@Query(value = "SELECT * FROM tbl_template WHERE template_name = ?", nativeQuery = true)
	List<Template> findTemplateByName(String name);

	@Query(value = "SELECT * FROM tbl_template WHERE template_id NOT IN (SELECT template_id_fk FROM tbl_template_hierarchy) ORDER BY template_name ASC", nativeQuery = true)
	List<Template> getAllTemplates();

	@Query(value = "SELECT * FROM tbl_template WHERE template_id IN (SELECT template_id_fk FROM tbl_template_hierarchy JOIN tbl_hierarchy ON tbl_template_hierarchy.node_id_fk = tbl_hierarchy.node_id WHERE tbl_hierarchy.node_active_status = 1) ORDER BY template_name ASC", nativeQuery = true)
	List<Template> getAllAssetTemplates();

	@Query(value = "SELECT * FROM tbl_template WHERE template_id IN (SELECT template_id_fk FROM tbl_template_hierarchy WHERE node_id_fk = ?)", nativeQuery = true)
	List<Template> findAssetTemplateByNodeId(Integer nodeId);
}
