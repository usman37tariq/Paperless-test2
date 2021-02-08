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
@Table(name = "checklist_state")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistState {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "checklist_state_id")
	long checklistStateId;

	@NotNull
	@Column(name = "time_stamp", columnDefinition = "bigint default (extract(epoch from now())::bigint) * 1000", insertable = false)
	long timeStamp;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_id_fk")
	Checklist checklist;

	@ManyToOne(fetch = FetchType.EAGER, optional = true) //TODO: make optional = false when end point is updated
	@JoinColumn(name = "user_id_fk")
	Users user;

	public ChecklistState() {}
	
	public long getChecklistStateId() {
		return checklistStateId;
	}

	public void setChecklistStateId(long checklistStateId) {
		this.checklistStateId = checklistStateId;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
}
