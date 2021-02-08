package com.engro.paperlessbackend.dto;

public class NotificationCountDto {

	private int claimedCount;
	private int unClaimedCount;
	private int dueCount;
	private int overDueCount;
	private int approveCount;
	
	public NotificationCountDto() {}

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

	public int getApproveCount() {
		return approveCount;
	}

	public void setApproveCount(int approveCount) {
		this.approveCount = approveCount;
	}
}
