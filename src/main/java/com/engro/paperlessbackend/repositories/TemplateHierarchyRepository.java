package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.TemplateHierarchy;

@Repository
public interface TemplateHierarchyRepository extends JpaRepository<TemplateHierarchy, Integer> {

	@Modifying(clearAutomatically = true)
	void deleteByTemplateId(Integer templateId);

	List<TemplateHierarchy> findByTemplateIdAndNodeId(Integer templateId, Integer nodeId);
}