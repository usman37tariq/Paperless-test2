package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.Hierarchy;

@Repository
public interface HierarchyRepository extends JpaRepository<Hierarchy, Integer> {

	@Query(value = "SELECT * FROM tbl_hierarchy WHERE node_active_status = 1 ORDER BY node_type ASC, node_name ASC", nativeQuery = true)
	List<Hierarchy> getHierarchy();

	String UPDATE_CHILD_NODE_STATUS_SQL = "" + "WITH RECURSIVE tree AS " + "	( " + "		SELECT "
			+ "			node_id                        , " + "			node_active_status             , "
			+ "			ARRAY[]::INTEGER[] AS ancestors, " + "			ARRAY[]::INTEGER[] AS ancestors_active_status "
			+ "		FROM " + "			tbl_hierarchy " + "		WHERE " + "			node_parent_id =-1 "
			+ "		UNION ALL " + "		SELECT " + "			tbl_hierarchy.node_id           , "
			+ "			tbl_hierarchy.node_active_status, " + "			tree.ancestors " + "				|| "
			+ "				tbl_hierarchy.node_parent_id, " + "			tree.ancestors_active_status "
			+ "				|| " + "				tbl_hierarchy.node_active_status " + "		FROM "
			+ "			tbl_hierarchy, " + "			tree " + "		WHERE "
			+ "			tbl_hierarchy.node_parent_id = tree.node_id " + "	) " + "UPDATE " + "	tbl_hierarchy "
			+ "SET node_active_status =0 " + "WHERE " + "	node_id IN " + "	( " + "		SELECT "
			+ "			node_id " + "		FROM " + "			tree " + "		WHERE "
			+ "			? = ANY(tree.ancestors) " + "	) " + ";";

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "WITH RECURSIVE tree AS (    SELECT node_id, node_active_status, ARRAY[]::INTEGER[] AS ancestors, ARRAY[]::INTEGER[] AS ancestors_active_status    FROM tbl_hierarchy WHERE node_parent_id =-1       UNION ALL       SELECT tbl_hierarchy.node_id, tbl_hierarchy.node_active_status, tree.ancestors || tbl_hierarchy.node_parent_id,    tree.ancestors_active_status || tbl_hierarchy.node_active_status    FROM tbl_hierarchy, tree    WHERE tbl_hierarchy.node_parent_id = tree.node_id  )   update tbl_hierarchy set node_active_status =0 where node_id in   (  SELECT node_id FROM tree WHERE   ? = ANY(tree.ancestors)  )", nativeQuery = true)
	void deActivateAllChildNodesOfDeletedNode(Integer nodeId);

	@Query(value = "SELECT * FROM tbl_hierarchy WHERE node_active_status = 1 AND node_id IN "
			+ "(SELECT node_id_fk FROM tbl_template_hierarchy JOIN tbl_template_structure ON "
			+ "tbl_template_hierarchy.template_id_fk = tbl_template_structure.template_id_fk) ORDER BY node_name", nativeQuery = true)
	List<Hierarchy> getHierarchyAssetNodesHavingTemplate();

	@Query(value = "SELECT * FROM tbl_hierarchy WHERE node_id = ? AND node_type = '1'", nativeQuery = true)
	List<Hierarchy> isParentNodeAsset(Integer nodeId);

	@Query(value = "SELECT * FROM tbl_hierarchy WHERE node_id = ?", nativeQuery = true)
	List<Hierarchy> wasNodeTypeAsset(Integer nodeId);

	@Query(value = "SELECT * FROM tbl_hierarchy WHERE node_parent_id = ? AND node_type = '0'", nativeQuery = true)
	List<Hierarchy> isChildrenHasLocationTypeNode(Integer nodeId);
}
