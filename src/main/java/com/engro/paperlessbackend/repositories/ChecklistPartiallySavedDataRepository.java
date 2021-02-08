package com.engro.paperlessbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.ChecklistPartiallySavedData;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.Users;

@Repository
public interface ChecklistPartiallySavedDataRepository extends JpaRepository<ChecklistPartiallySavedData, Long> {

	ChecklistPartiallySavedData getTop1ByChecklistScheduleAndUser(ChecklistSchedule checklistSchedule, Users user);

	ChecklistPartiallySavedData getTop1ByChecklistSchedule(ChecklistSchedule checklistSchedule);

	@Modifying(clearAutomatically = true)
	void deleteByChecklistSchedule(ChecklistSchedule checklistSchedule);

}
