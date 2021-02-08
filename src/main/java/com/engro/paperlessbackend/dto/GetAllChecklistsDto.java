package com.engro.paperlessbackend.dto;

import com.engro.paperlessbackend.entities.Department;
import com.engro.paperlessbackend.entities.Section;

public class GetAllChecklistsDto{

	private int id;
	private String name;
	private Department department;
	private Section section;
	private String description;
	private String section_name;
	private String department_name;
	
	public GetAllChecklistsDto() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
		this.section_name = section.getSectionName();
		this.department_name = section.getDepartment().getName();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
}
