package com.engro.paperlessbackend.dto;

public class AccessDto {

	int read;
	int add;
	int edit;
	int delete;

	public AccessDto(){
		
	}
	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getAdd() {
		return add;
	}

	public void setAdd(int add) {
		this.add = add;
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
