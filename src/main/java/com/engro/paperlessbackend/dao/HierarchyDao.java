package com.engro.paperlessbackend.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.Hierarchy;
import com.engro.paperlessbackend.repositories.HierarchyRepository;

@Component
public class HierarchyDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static Logger logger = LoggerFactory.getLogger(HierarchyDao.class);

	@Autowired
	HierarchyRepository hierarchyRepository;

	public Hierarchy addNewNodeToHierarchy(Hierarchy node) {
		Hierarchy hNode = null;
		try {
			hNode = hierarchyRepository.save(node);
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return hNode;
	}

	public List<Hierarchy> getHierarchy() {
		List<Hierarchy> nodesList = null;
		try {
			nodesList = hierarchyRepository.getHierarchy();
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return nodesList;
	}

	public Hierarchy updateNodeInHierarchy(Hierarchy node) {
		Hierarchy savedNode = null;
		try {
			Optional<Hierarchy> currNode = hierarchyRepository.findById(node.getId());
			if (currNode.isPresent()) {
				Hierarchy nd = currNode.get();
				nd.setName(node.getName());
				nd.setDescription(node.getDescription());
				nd.setParent(node.getParent().equals("#") ? -1 : Integer.valueOf(node.getParent()));
				nd.setSapRefNumber(node.getSapRefNumber());
				nd.setType(node.getType());
				savedNode = hierarchyRepository.save(nd);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return savedNode;
	}

	@Transactional
	public Hierarchy deleteNodeFromHierarchy(int nodeId) {
		Hierarchy deletedNode = null;
		try {
			Optional<Hierarchy> currNode = hierarchyRepository.findById(nodeId);
			if (currNode.isPresent()) {
				if (!deactivateChildNodes(nodeId)) {

				}
				Hierarchy nd = currNode.get();
				nd.setActiveStatus(0);
				deletedNode = hierarchyRepository.save(nd);
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return deletedNode;
	}

	public List<Hierarchy> getHierarchyAssetNodesHavingTemplate() {
		List<Hierarchy> assetNodes = new ArrayList<Hierarchy>();
		try {
			assetNodes = hierarchyRepository.getHierarchyAssetNodesHavingTemplate();
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return assetNodes;
	}

	public boolean isParentNodeAsset(int nodeId) {
		List<Hierarchy> node = new ArrayList<Hierarchy>();
		try {
			node = hierarchyRepository.isParentNodeAsset(nodeId);
			if (!node.isEmpty()) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	public boolean wasNodeTypeAsset(int nodeId) {
		List<Hierarchy> node = new ArrayList<Hierarchy>();
		try {
			node = hierarchyRepository.wasNodeTypeAsset(nodeId);
			if (!node.isEmpty()) {
				if (node.get(0).getType().equals("1")) {
					logger.info("[Successful]");
					return true;
				}
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	public boolean isChildrenHasLocationTypeNode(int nodeId) {
		List<Hierarchy> node = new ArrayList<Hierarchy>();
		try {
			node = hierarchyRepository.isChildrenHasLocationTypeNode(nodeId);
			if (!node.isEmpty()) {
				logger.info("[Successful]");
				return true;
			}
			logger.info("[Successful]");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	private boolean deactivateChildNodes(int nodeId) {
		final String UPDATE_CHILD_NODE_STATUS_SQL = "" + "WITH RECURSIVE tree AS " + "	( " + "		SELECT "
				+ "			node_id                        , " + "			node_active_status             , "
				+ "			ARRAY[]::INTEGER[] AS ancestors, "
				+ "			ARRAY[]::INTEGER[] AS ancestors_active_status " + "		FROM " + "			tbl_hierarchy "
				+ "		WHERE " + "			node_parent_id =-1 " + "		UNION ALL " + "		SELECT "
				+ "			tbl_hierarchy.node_id           , " + "			tbl_hierarchy.node_active_status, "
				+ "			tree.ancestors " + "				|| " + "				tbl_hierarchy.node_parent_id, "
				+ "			tree.ancestors_active_status " + "				|| "
				+ "				tbl_hierarchy.node_active_status " + "		FROM " + "			tbl_hierarchy, "
				+ "			tree " + "		WHERE " + "			tbl_hierarchy.node_parent_id = tree.node_id " + "	) "
				+ "UPDATE " + "	tbl_hierarchy " + "SET node_active_status =0 " + "WHERE " + "	node_id IN " + "	( "
				+ "		SELECT " + "			node_id " + "		FROM " + "			tree " + "		WHERE "
				+ "		" + nodeId + " = ANY(tree.ancestors) " + "	) " + ";";

		logger.info("[{}]", UPDATE_CHILD_NODE_STATUS_SQL);

		jdbcTemplate.execute(UPDATE_CHILD_NODE_STATUS_SQL);

		logger.info("Child nodes deactivation for nodeId[{}] Successful", nodeId);
		return true;
	}
}
