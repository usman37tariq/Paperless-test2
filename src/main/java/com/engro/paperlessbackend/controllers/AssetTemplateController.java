package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.dto.AssetTemplateAdditionDto;
import com.engro.paperlessbackend.dto.AssetTemplateDto;
import com.engro.paperlessbackend.dto.GetAssetTemplateStructureDto;
import com.engro.paperlessbackend.services.AssetTemplateService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class AssetTemplateController {

	@Autowired
	AssetTemplateService assetTemplateService;
	
	@ApiOperation(value = "Add New Asset Template", tags = "Asset Template")
	@RequestMapping(method = RequestMethod.POST, value = "assettemplate")
	public Object addAssetTemplate(@RequestBody AssetTemplateDto assetTemplate) {
		return assetTemplateService.addAssetTemplate(assetTemplate);
	}
	
	@ApiOperation(value = "Add Template to Asset Template Structure", tags = "Asset Template")
	@RequestMapping(method = RequestMethod.POST, value = "assettemplate/template")
	public Object addTemplateToAssetTemplateStructure(@RequestBody AssetTemplateAdditionDto assetTemplate) {
		return assetTemplateService.addTemplateToAssetTemplateStructure(assetTemplate);
	}
	
	@ApiOperation(value = "Get Asset Template Structure (Deprecated)", tags = "Asset Template")
	@RequestMapping(method = RequestMethod.POST, value = "assettemplate/structure")
	public Object getTemplateStructure(@RequestBody GetAssetTemplateStructureDto getAssetTemplateStructureDto) {
		return assetTemplateService.getAssetTemplateStructure(getAssetTemplateStructureDto);
	}
	
	@ApiOperation(value = "Get Asset Template Structure by Node ID", tags = "Asset Template")
	@RequestMapping(method = RequestMethod.GET, value = "assettemplate/{nodeId}")
	public Object getTemplateStructureByNodeId(@PathVariable String nodeId) {
		return assetTemplateService.getAssetTemplateStructureByNodeId(Integer.valueOf(nodeId));
	}
}
