package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistApproverGroup;
import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.WorkflowLevel;

@Repository
public interface ChecklistApproverGroupRepository extends JpaRepository<ChecklistApproverGroup, Integer> {

	List<ChecklistApproverGroup> findByChecklist(Checklist checklist);

	@Modifying(clearAutomatically = true)
	void deleteByChecklist(Checklist checklist);

	@Modifying(clearAutomatically = true)
	void deleteByGroup(Group group);

	List<ChecklistApproverGroup> findByGroup(Group group);

	List<ChecklistApproverGroup> findByGroupAndWorkflowLevel(Group group, WorkflowLevel workFlowLevel);

	List<ChecklistApproverGroup> findByGroupAndWorkflowLevelGreaterThan(Group group, WorkflowLevel workFlowLevel);
}
