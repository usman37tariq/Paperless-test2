package com.engro.paperlessbackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "checklist_partially_saved_data")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistPartiallySavedData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "checklist_partially_saved_data_id")
	long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id_fk")
	Users user;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_schedule_id_fk")
	ChecklistSchedule checklistSchedule;

	@NotNull
	@Column(name = "data", columnDefinition = "CHARACTER VARYING NOT NULL")
	String data;

	public ChecklistPartiallySavedData() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public ChecklistSchedule getChecklistSchedule() {
		return checklistSchedule;
	}

	public void setChecklistSchedule(ChecklistSchedule checklistSchedule) {
		this.checklistSchedule = checklistSchedule;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
