package com.engro.paperlessbackend.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dao.ChecklistDao;
import com.engro.paperlessbackend.dao.HierarchyDao;
import com.engro.paperlessbackend.dao.TemplateDao;
import com.engro.paperlessbackend.dto.HierarchyDto;
import com.engro.paperlessbackend.entities.Hierarchy;
import com.engro.paperlessbackend.entities.Template;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class HierarchyService {

	private static Logger logger = LoggerFactory.getLogger(HierarchyService.class);

	@Autowired
	HierarchyDao hierarchyDao;

	@Autowired
	TemplateDao templateDao;

	@Autowired
	ChecklistDao checklistDao;

	@Autowired
	private ModelMapper modelMapper;

	public Object addNewNodeToHierarchy(Hierarchy node) {
		Hierarchy newNode = new Hierarchy();
		try {
			if (node.getType().equals(Constants.HIERARCHY_NODE_TYPE_LOCATION) && hierarchyDao
					.isParentNodeAsset(node.getParent().equals(Constants.HIERARCHY_NODE_PARENT_ID_FOR_UI) ? -1
							: Integer.valueOf(node.getParent()))) {
				return ResponseEntity.badRequest().body("Location cannot be added under an asset");
			}
			if (node.getName().equalsIgnoreCase("") || node.getName().equalsIgnoreCase("null")
					|| node.getParent().equalsIgnoreCase("") || node.getParent().equalsIgnoreCase("null")) {
				return ResponseEntity.badRequest().body("Node Name is Empty");
			}
			newNode = hierarchyDao.addNewNodeToHierarchy(node);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return ResponseEntity.badRequest().body("Error occured while adding node to hierarchy");
		}
		return newNode;
	}

	public List<HierarchyDto> getHierarchy() {
		List<HierarchyDto> nodesDtoList = new ArrayList<HierarchyDto>();
		try {
			List<Hierarchy> nodes = hierarchyDao.getHierarchy();
			Type listType = new TypeToken<List<HierarchyDto>>() {
			}.getType();
			nodesDtoList = modelMapper.map(nodes, listType);
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return nodesDtoList;
	}

	public Object updateNodeInHierarchy(Hierarchy node) {
		Hierarchy updatedNode = new Hierarchy();
		try {
			int parentId = node.getParent().equals(Constants.HIERARCHY_NODE_PARENT_ID_FOR_UI) ? -1
					: Integer.valueOf(node.getParent());
			int nodeId = node.getId();

			if (node.getType().equals(Constants.HIERARCHY_NODE_TYPE_ASSET)
					&& hierarchyDao.isChildrenHasLocationTypeNode(nodeId)) {
				return ResponseEntity.badRequest().body("Asset cannot have a location type node");
			}

			if (node.getType().equals(Constants.HIERARCHY_NODE_TYPE_LOCATION)) {

				if (hierarchyDao.isParentNodeAsset(parentId)) {
					return ResponseEntity.badRequest().body("Location can not be added under an asset");
				}
			}

			if (node.getName().equalsIgnoreCase("") || node.getName().equalsIgnoreCase("null")
					|| node.getParent().equalsIgnoreCase("") || node.getParent().equalsIgnoreCase("null")) {
				return ResponseEntity.badRequest().body("Node Name is Empty");
			}
			updatedNode = hierarchyDao.updateNodeInHierarchy(node);

			Template assetTemplate = templateDao.findAssetTemplateByNodeId(updatedNode.getId());
			if (assetTemplate != null) {
				assetTemplate.setName(updatedNode.getName());
				templateDao.updateTemplate(assetTemplate);
			}

			logger.info("[SUCCESSFULL]");
		} catch (Exception ex) {
			logger.error("Exception:", ex);
			return ResponseEntity.badRequest().body("Error occured while updating hierarchy node");
		}
		return updatedNode;
	}

	public ResponseEntity<String> deleteNodeFromHierarchy(int nodeId) {

		List<Map<String, Object>> result = checklistDao.getChecklistsLinkedToNodeByNodeId(nodeId);

		if (result != null && !result.isEmpty()) {
			String errMsg = "cannot delete this asset/location, this asset/location and its children are being used in checklist(s): ";
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> row = result.get(i);
				errMsg += row.get("checklist_name");
				if(i < result.size()-1) {
					errMsg += ",";
				}
			}
			logger.error(errMsg);
			return ResponseEntity.badRequest().body(errMsg);
		}
		hierarchyDao.deleteNodeFromHierarchy(nodeId);
		
		logger.info("[Successful]");
		return ResponseEntity.ok().body("Hierarchy node deleted successfully");
	}

	public Object getHierarchyAssetNodesHavingTemplate() {
		return hierarchyDao.getHierarchyAssetNodesHavingTemplate();
	}
}
