package com.engro.paperlessbackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.engro.paperlessbackend.entityid.RoleResourceId;

@Entity
@Table(name = "tbl_role_resource")
@EntityListeners(AuditingEntityListener.class)
@IdClass(RoleResourceId.class)
public class RoleResource {

	@Id
	@NotNull
	@ManyToOne
	@JoinColumn(name = "role_id_fk")
	private Role role;
	
	@Id
	@NotNull
	@ManyToOne
	@JoinColumn(name = "resource_id_fk")
	private Resource resource;
	
	@Column(name = "can_add")
	private int add;
	
	@Column(name = "can_view")
	private int read;
	
	@Column(name = "can_edit")
	private int edit;
	
	@Column(name = "can_delete")
	private int delete;
	
	public RoleResource() {}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getAdd() {
		return add;
	}

	public void setAdd(int add) {
		this.add = add;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getEdit() {
		return edit;
	}

	public void setEdit(int edit) {
		this.edit = edit;
	}

	public int getDelete() {
		return delete;
	}

	public void setDelete(int delete) {
		this.delete = delete;
	}
}
