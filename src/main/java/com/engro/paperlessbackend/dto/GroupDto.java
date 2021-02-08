package com.engro.paperlessbackend.dto;

import java.util.List;

import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.Users;

public class GroupDto {

	private Group group;
	private List<Users> users;
	
	public GroupDto() {}
	
	public Group getGroup() {
		return group;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}
}
