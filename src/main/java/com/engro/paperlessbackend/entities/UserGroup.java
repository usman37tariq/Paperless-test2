package com.engro.paperlessbackend.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.engro.paperlessbackend.entityid.UserGroupId;

@Entity
@Table(name = "tbl_user_group")
@EntityListeners(AuditingEntityListener.class)
@IdClass(UserGroupId.class)
public class UserGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id_fk")
	private Users user;
	
	@Id
	@NotNull
	@ManyToOne
	@JoinColumn(name = "group_id_fk")
	private Group group;
	
	public UserGroup() {}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}
