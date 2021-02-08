package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistApproverUser;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.entities.WorkflowLevel;

@Repository
public interface ChecklistApproverUserRepository extends JpaRepository<ChecklistApproverUser, Integer> {

	List<ChecklistApproverUser> findByChecklist(Checklist checklist);

	@Modifying(clearAutomatically = true)
	void deleteByChecklist(Checklist checklist);

	List<ChecklistApproverUser> findByUser(Users user);

	List<ChecklistApproverUser> findByUserAndWorkflowLevel(Users user, WorkflowLevel workFlowLevel);

	List<ChecklistApproverUser> findByUserAndWorkflowLevelGreaterThan(Users user, WorkflowLevel workFlowLevel);
}
