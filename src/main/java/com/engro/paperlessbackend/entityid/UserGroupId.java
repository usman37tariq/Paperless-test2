package com.engro.paperlessbackend.entityid;

import java.io.Serializable;

import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.Users;

public class UserGroupId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String user;
	private int group;
	
	public UserGroupId() {}
	
	public UserGroupId(Users user, Group group) {
        this.user = user.getId();
        this.group = group.getId();
    }

}
