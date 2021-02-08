package com.engro.paperlessbackend.dto;

import java.util.List;

public class UserChecklistDataDto {

	private int claimedCount;
	private int unClaimedCount;
	private int dueCount;
	private int overDueCount;
	private List<ChecklistScheduleStatusDto> claimedChecklists;
	private List<ChecklistScheduleStatusDto> unClaimedChecklists;
	
	public UserChecklistDataDto() {}

	public int getClaimedCount() {
		return claimedCount;
	}

	public void setClaimedCount(int claimedCount) {
		this.claimedCount = claimedCount;
	}

	public int getUnClaimedCount() {
		return unClaimedCount;
	}

	public void setUnClaimedCount(int unClaimedCount) {
		this.unClaimedCount = unClaimedCount;
	}
	
	public int getDueCount() {
		return dueCount;
	}

	public void setDueCount(int dueCount) {
		this.dueCount = dueCount;
	}

	public int getOverDueCount() {
		return overDueCount;
	}

	public void setOverDueCount(int overDueCount) {
		this.overDueCount = overDueCount;
	}

	public List<ChecklistScheduleStatusDto> getClaimedChecklists() {
		return claimedChecklists;
	}

	public void setClaimedChecklists(List<ChecklistScheduleStatusDto> claimedChecklists) {
		this.claimedChecklists = claimedChecklists;
	}

	public List<ChecklistScheduleStatusDto> getUnClaimedChecklists() {
		return unClaimedChecklists;
	}

	public void setUnClaimedChecklists(List<ChecklistScheduleStatusDto> unClaimedChecklists) {
		this.unClaimedChecklists = unClaimedChecklists;
	}
}
