package com.engro.paperlessbackend.dto;

/**
 *
 * don't change this dto in the future, its mapping is static
 * 
 */

import com.engro.paperlessbackend.entities.Users;

public class UserLoginDto {

	public Users user;
	public AccessDto hierachyBuilder;
	public AccessDto assetBuilder;
	public AccessDto checklistBuilder;
	public AccessDto dataCollector;
	public AccessDto dataVisualization;
	public AccessDto userManagement;
	
	public UserLoginDto() {}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public AccessDto getHierachyBuilder() {
		return hierachyBuilder;
	}

	public void setHierachyBuilder(AccessDto hierachyBuilder) {
		this.hierachyBuilder = hierachyBuilder;
	}

	public AccessDto getAssetBuilder() {
		return assetBuilder;
	}

	public void setAssetBuilder(AccessDto assetBuilder) {
		this.assetBuilder = assetBuilder;
	}

	public AccessDto getChecklistBuilder() {
		return checklistBuilder;
	}

	public void setChecklistBuilder(AccessDto checklistBuilder) {
		this.checklistBuilder = checklistBuilder;
	}

	public AccessDto getDataCollector() {
		return dataCollector;
	}

	public void setDataCollector(AccessDto dataCollector) {
		this.dataCollector = dataCollector;
	}

	public AccessDto getDataVisualization() {
		return dataVisualization;
	}

	public void setDataVisualization(AccessDto dataVisualization) {
		this.dataVisualization = dataVisualization;
	}

	public AccessDto getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(AccessDto userManagement) {
		this.userManagement = userManagement;
	}
}
