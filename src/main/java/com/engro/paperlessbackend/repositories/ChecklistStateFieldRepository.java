package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.ChecklistState;
import com.engro.paperlessbackend.entities.ChecklistStateField;
import com.engro.paperlessbackend.entities.TemplateStructure;

@Repository
public interface ChecklistStateFieldRepository extends JpaRepository<ChecklistStateField, Long> {

	List<ChecklistStateField> findByChecklistState(ChecklistState checklistState);

	List<ChecklistStateField> findDistinctTemplateStructureByChecklistStateIn(List<ChecklistState> checklistStates);

	List<ChecklistStateField> findDistinctChecklistStateByTemplateStructure(TemplateStructure templateField);

	@Modifying(clearAutomatically = true)
	void deleteByTemplateStructure(TemplateStructure templateField);

	List<ChecklistStateField> findByChecklistStateOrderByChecklistStateFieldOrderIdAsc(ChecklistState checklistState);

}
