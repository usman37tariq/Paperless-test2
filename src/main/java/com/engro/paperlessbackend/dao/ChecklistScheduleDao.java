package com.engro.paperlessbackend.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistState;
import com.engro.paperlessbackend.repositories.ChecklistScheduleRepository;

@Component
public class ChecklistScheduleDao {

	private static Logger logger = LoggerFactory.getLogger(ChecklistScheduleDao.class);

	@Autowired
	ChecklistScheduleRepository checklistScheduleRepository;

	public List<ChecklistSchedule> addChecklistSchedulesList(List<ChecklistSchedule> checklistSchedules) {
		List<ChecklistSchedule> cs = new ArrayList<>();
		for (ChecklistSchedule checklistSchedule : checklistSchedules) {

			try {
				cs.add(checklistScheduleRepository.save(checklistSchedule));
			} catch (ConstraintViolationException ex) {
				logger.error("[EXCEPTION] -[{}] ", ex.getMessage());
			} catch (DataIntegrityViolationException ex) {
				logger.error("[EXCEPTION] -[{}] ", ex.getMessage());
			}
		}
		logger.info("[SUCCESSFUL]");
		return cs;
	}

	public ChecklistSchedule addChecklistSchedule(ChecklistSchedule checklistSchedule) {
		ChecklistSchedule cs = null;
		try {
			cs = checklistScheduleRepository.save(checklistSchedule);
		} catch (Exception ex) {
			logger.error("[EXCEPTION] -[{}] ", ex);
		}

		logger.info("[SUCCESSFUL]");
		return cs;
	}

	@Transactional
	public boolean deleteChecklistSchedulesByChecklistHavingStartTimeGreaterThanCurrTime(Checklist checklist) {
		try {
			checklistScheduleRepository.deleteByChecklistAndStartTimestampGreaterThan(checklist,
					System.currentTimeMillis());
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
			return false;
		}
		return true;
	}

	@Transactional
	public boolean deleteChecklistScheduleByChecklistScheduleId(int checklistScheduleId) {
		try {
			checklistScheduleRepository.deleteById(checklistScheduleId);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
			return false;
		}
		return true;
	}

	public List<ChecklistSchedule> getChecklistSchedulesByChecklist(Checklist checklist) {
		List<ChecklistSchedule> checklistSchedules = new ArrayList<ChecklistSchedule>();

		try {
			checklistSchedules = checklistScheduleRepository.findByChecklistAndStartTimestampLessThanEqual(checklist,
					System.currentTimeMillis());
			logger.debug("[Successful]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return checklistSchedules;
	}

	public ChecklistSchedule getChecklistScheduleByScheduleId(int checklistScheduleId) {
		try {
			Optional<ChecklistSchedule> schedule = checklistScheduleRepository.findById(checklistScheduleId);
			logger.info("[SUCCESSFUL]");
			if (schedule.isPresent()) {
				return schedule.get();
			}
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return new ChecklistSchedule();
	}

	public List<ChecklistSchedule> getChecklistSchedules(Checklist checklist, long start, long end) {
		List<ChecklistSchedule> checklistSchedules = new ArrayList<>();

		try {
			checklistSchedules = checklistScheduleRepository
					.findByChecklistAndStartTimestampGreaterThanEqualAndEndTimestampLessThanEqual(checklist, start,
							end);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return checklistSchedules;
	}

	public List<ChecklistSchedule> getChecklistScheduleInTimeRange(Checklist checklist, long start, long end) {
		List<ChecklistSchedule> checklistSchedules = new ArrayList<>();

		try {

			checklistSchedules = checklistScheduleRepository.findSchedulesInDateRange(checklist.getChecklistId(), start,
					end);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return checklistSchedules;
	}

	public List<ChecklistSchedule> getChecklistSchedulesByForwardDirection(Checklist checklist, long date) {
		List<ChecklistSchedule> checklistSchedules = new ArrayList<>();

		try {
			checklistSchedules = checklistScheduleRepository.findSchedulesInForwardDirection(checklist.getChecklistId(),
					date);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return checklistSchedules;
	}

	public List<ChecklistSchedule> getChecklistSchedulesByBackwardDirection(Checklist checklist, long date) {
		List<ChecklistSchedule> checklistSchedules = new ArrayList<>();

		try {
			checklistSchedules = checklistScheduleRepository
					.findSchedulesInBackwardDirection(checklist.getChecklistId(), date);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return checklistSchedules;
	}

	public List<ChecklistSchedule> getPreviousChecklistSchedules(Checklist checklist, long date, int scheduleId) {
		List<ChecklistSchedule> checklistSchedules = new ArrayList<>();

		try {
			checklistSchedules = checklistScheduleRepository.findPreviousSchedule(checklist.getChecklistId(), date,
					scheduleId);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return checklistSchedules;
	}

	public List<ChecklistSchedule> getNextChecklistSchedules(Checklist checklist, long date, int scheduleId) {
		List<ChecklistSchedule> checklistSchedules = new ArrayList<>();

		try {
			checklistSchedules = checklistScheduleRepository.findNextSchedule(checklist.getChecklistId(), date,
					scheduleId);
			logger.info("[SUCCESSFUL]");
		} catch (Exception ex) {
			logger.error("[EXCEPTION] - [{}]", ex);
		}
		return checklistSchedules;
	}

	public List<ChecklistSchedule> getChecklistSchedulesOfManualChecklist(Checklist checklist) {
		List<ChecklistSchedule> checklistSchedules = null;
		try {
			checklistSchedules = checklistScheduleRepository
					.getChecklistSchedulesOfManualChecklist(checklist.getChecklistId());
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return checklistSchedules;
	}

	public List<ChecklistSchedule> getChecklistSchedulesByChecklistState(ChecklistState checklistState) {
		List<ChecklistSchedule> checklistSchedules = null;
		try {
			checklistSchedules = checklistScheduleRepository.findByChecklistState(checklistState);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return checklistSchedules;
	}

}
