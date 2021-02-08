package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistState;

@Repository
public interface ChecklistScheduleRepository extends JpaRepository<ChecklistSchedule, Integer> {

	List<ChecklistSchedule> findByChecklistAndStartTimestampLessThanEqual(Checklist checklist, long currentTimeMillis);

	@Modifying(clearAutomatically = true)
	void deleteByChecklistAndStartTimestampGreaterThan(Checklist checklist, long currentTimeMillis);

	List<ChecklistSchedule> findByChecklistAndStartTimestampGreaterThanEqualAndEndTimestampLessThanEqual(
			Checklist checklist, long start, long end);

	@Query(value = "SELECT * FROM tbl_checklist_schedule WHERE checklist_schedule_id = (SELECT checklist_schedule_id FROM ("
			+ " SELECT tbl_checklist_status_history.checklist_schedule_id," + " MAX(status_timestamp)"
			+ " FROM tbl_checklist_status_history"
			+ " INNER JOIN tbl_checklist_schedule ON tbl_checklist_schedule.checklist_schedule_id = tbl_checklist_status_history.checklist_schedule_id"
			+ " WHERE " + " tbl_checklist_schedule.checklist_id_fk = ? AND previous_workflow_level_id_fk = 1"
			+ " GROUP BY tbl_checklist_status_history.checklist_schedule_id"
			+ " HAVING MAX(status_timestamp) < ? ORDER BY MAX(status_timestamp) DESC LIMIT 1 ) as a)", nativeQuery = true)
	List<ChecklistSchedule> findSchedulesInBackwardDirection(int checklistId, long timestamp);

	@Query(value = "SELECT * FROM tbl_checklist_schedule WHERE checklist_schedule_id = (SELECT checklist_schedule_id FROM ("
			+ " SELECT tbl_checklist_status_history.checklist_schedule_id," + " MAX(status_timestamp)"
			+ " FROM tbl_checklist_status_history"
			+ " INNER JOIN tbl_checklist_schedule on tbl_checklist_schedule.checklist_schedule_id = tbl_checklist_status_history.checklist_schedule_id"
			+ " WHERE " + " tbl_checklist_schedule.checklist_id_fk = ? AND previous_workflow_level_id_fk = 1"
			+ " GROUP BY tbl_checklist_status_history.checklist_schedule_id"
			+ " HAVING MAX(status_timestamp) > ? ORDER BY MAX(status_timestamp) LIMIT 1 ) as a)", nativeQuery = true)
	List<ChecklistSchedule> findSchedulesInForwardDirection(int checklistId, long timestamp);

	@Query(value = "SELECT * FROM tbl_checklist_schedule WHERE checklist_schedule_id = (SELECT checklist_schedule_id FROM ("
			+ " SELECT tbl_checklist_status_history.checklist_schedule_id," + " MAX(status_timestamp)"
			+ " FROM tbl_checklist_status_history"
			+ " INNER JOIN tbl_checklist_schedule on tbl_checklist_schedule.checklist_schedule_id = tbl_checklist_status_history.checklist_schedule_id"
			+ " WHERE " + " tbl_checklist_schedule.checklist_id_fk = ? AND previous_workflow_level_id_fk = 1"
			+ " GROUP BY tbl_checklist_status_history.checklist_schedule_id"
			+ " HAVING MAX(status_timestamp) BETWEEN ? AND ? ORDER BY MAX(status_timestamp) LIMIT 1 ) as a)", nativeQuery = true)
	List<ChecklistSchedule> findSchedulesInDateRange(int checklistId, long startTimestamp, long endTimestamp);

	@Query(value = "SELECT * FROM tbl_checklist_schedule WHERE checklist_id_fk = ? AND end_timestamp IS NULL AND checklist_schedule_id NOT IN (SELECT checklist_schedule_id FROM tbl_checklist_status_history)", nativeQuery = true)
	List<ChecklistSchedule> getChecklistSchedulesOfManualChecklist(Integer checklistId);

	@Query(value = "SELECT * FROM tbl_checklist_schedule WHERE checklist_schedule_id IN (SELECT tbl_checklist_status_history.checklist_schedule_id FROM tbl_checklist_status_history JOIN tbl_checklist_schedule ON  tbl_checklist_status_history.checklist_schedule_id = tbl_checklist_schedule.checklist_schedule_id  WHERE checklist_id_fk = ? AND previous_workflow_level_id_fk = 1 AND status_timestamp < ? AND tbl_checklist_status_history.checklist_schedule_id <> ? ORDER BY status_timestamp  DESC LIMIT 1)", nativeQuery = true)
	List<ChecklistSchedule> findPreviousSchedule(int checklistId, long timestamp, int scheduleId);

	@Query(value = "SELECT * FROM tbl_checklist_schedule WHERE checklist_schedule_id IN (SELECT tbl_checklist_status_history.checklist_schedule_id FROM tbl_checklist_status_history JOIN tbl_checklist_schedule ON  tbl_checklist_status_history.checklist_schedule_id = tbl_checklist_schedule.checklist_schedule_id  WHERE checklist_id_fk = ? AND previous_workflow_level_id_fk = 1 AND status_timestamp > ? AND tbl_checklist_status_history.checklist_schedule_id <> ? ORDER BY status_timestamp  ASC LIMIT 1)", nativeQuery = true)
	List<ChecklistSchedule> findNextSchedule(int checklistId, long timestamp, int scheduleId);

	List<ChecklistSchedule> findByChecklistState(ChecklistState checklistState);
}
