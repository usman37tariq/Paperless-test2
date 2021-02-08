package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistScheduleSkipped;

@Repository
public interface ChecklistScheduleSkippedRepository extends JpaRepository<ChecklistScheduleSkipped, Integer> {

	List<ChecklistScheduleSkipped> findByChecklistScheduleOrderBySkippedTimestampDesc(
			ChecklistSchedule checklistSchedule);
}
