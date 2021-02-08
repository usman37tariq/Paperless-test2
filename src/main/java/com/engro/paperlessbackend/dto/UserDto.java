package com.engro.paperlessbackend.dto;

import java.util.List;

import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.Users;

public class UserDto {

	private Users user;
	private List<Group> groups;
	private String updatedBy;

	public UserDto() {
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}
