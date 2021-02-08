package com.engro.paperlessbackend.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.WorkflowLevel;
import com.engro.paperlessbackend.repositories.WorkflowLevelRepository;

@Component
public class WorkflowLevelDao {

	private static Logger logger = LoggerFactory.getLogger(WorkflowLevelDao.class);

	@Autowired
	WorkflowLevelRepository workFlowLevelRepository;

	public WorkflowLevel getWorkFlowLevelById(int levelId) {
		try {
			Optional<WorkflowLevel> level = workFlowLevelRepository.findById(levelId);
			logger.debug("[Successful]");
			if (level.isPresent()) {
				return level.get();
			}
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return null;
	}
	
	public ArrayList<WorkflowLevel> getAllWorkflowLevel() {
		ArrayList<WorkflowLevel> workflowLevels= new ArrayList<WorkflowLevel>();
		try {
			workflowLevels = (ArrayList<WorkflowLevel>) workFlowLevelRepository.findAll();
			logger.debug("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return workflowLevels;
	}
	
}
