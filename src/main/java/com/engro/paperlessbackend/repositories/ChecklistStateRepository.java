package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistState;

@Repository
public interface ChecklistStateRepository extends JpaRepository<ChecklistState, Long>{

	ChecklistState findTop1ByChecklistOrderByChecklistStateIdDesc(Checklist checklist);

	ChecklistState findTop1ByChecklistAndTimeStampLessThanEqual(Checklist checklist, long timestamp);

	ChecklistState findTop1ByChecklistAndTimeStampGreaterThanEqual(Checklist checklist, long timestamp);

	List<ChecklistState> findByChecklist(Checklist checklist);

}
