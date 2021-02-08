package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistScheduleClaimed;
import com.engro.paperlessbackend.entities.Users;

@Repository
public interface ChecklistScheduleClaimedRepository extends JpaRepository<ChecklistScheduleClaimed, Integer> {

	@Query(value = "SELECT * FROM tbl_checklist_schedule_claimed WHERE checklist_schedule_id_fk IN "
			+ "(SELECT checklist_schedule_id FROM tbl_checklist_schedule WHERE checklist_id_fk = ? AND "
			+ "start_timestamp <= ?)", nativeQuery = true)
	List<ChecklistScheduleClaimed> getClaimedChecklistDataByChecklistId(Integer checklistId, Long currTime);

	List<ChecklistScheduleClaimed> findByChecklistSchedule(ChecklistSchedule checklistSchedule);

	List<ChecklistScheduleClaimed> findByChecklistScheduleAndUser(ChecklistSchedule checklistSchedule, Users user);

	@Modifying(clearAutomatically = true)
	void deleteByChecklistSchedule(ChecklistSchedule checklistSchedule);

}
