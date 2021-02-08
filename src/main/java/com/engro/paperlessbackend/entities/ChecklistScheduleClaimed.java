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

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sun.istack.NotNull;

@Entity
@Table(name = "tbl_checklist_schedule_claimed")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistScheduleClaimed {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "checklist_schedule_claimed_id")
	private int id;
	
	@NotNull
	@Column(name = "time_stamp")
	private long timestamp;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_schedule_id_fk")
	private ChecklistSchedule checklistSchedule;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id_fk")
	private Users user;
	
	public ChecklistScheduleClaimed() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public ChecklistSchedule getChecklistSchedule() {
		return checklistSchedule;
	}

	public void setChecklistSchedule(ChecklistSchedule checklistSchedule) {
		this.checklistSchedule = checklistSchedule;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
}
