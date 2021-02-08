package com.engro.paperlessbackend.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dao.ChecklistDao;
import com.engro.paperlessbackend.dao.ChecklistScheduleDao;
import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.utils.Constants;

@Component
public class SchedulerService {

	@Autowired
	ChecklistDao checklistDao;

	@Autowired
	ChecklistScheduleDao checklistScheduleDao;

	@Autowired
	ChecklistService checklistService;

	private static Logger logger = LoggerFactory.getLogger(SchedulerService.class);

	@Scheduled(cron = "0 0 0 1/1 * *") // on 7PM PKT 12AM GMT everyday
//	@Scheduled(cron = "0 1/1 * * * *") //at 10 31 AM everyday
//	@Scheduled(cron = "*/60 * * * * *") //every 60 seconds
	void runScheduler() {
		logger.info("***********Running Scheduler***********");

		List<Checklist> checklists = checklistDao.getAllChecklists();

		LocalDate localDate = LocalDate.now();
		LocalDateTime startOfDay = localDate.atStartOfDay();
		LocalDateTime endOfDay = LocalTime.MAX.atDate(localDate);

		long utcStartOfTheDayInMillis = startOfDay.atOffset(OffsetDateTime.now().getOffset()).toInstant()
				.toEpochMilli();
		long utcEndOfTheDayInMillis = endOfDay.atOffset(OffsetDateTime.now().getOffset()).toInstant().toEpochMilli();

		logger.info("start of the day Timestamp:[{}], millis:[{}] ", new Timestamp(utcStartOfTheDayInMillis),
				utcStartOfTheDayInMillis);
		logger.info("end of the day Timestamp:[{}], millis:[{}]", new Timestamp(utcEndOfTheDayInMillis),
				utcEndOfTheDayInMillis);
		for (Checklist checklist : checklists) {
			if (checklist.getActivationStatus().equals(Constants.CHECKLIST_STATUS_PENDING) || checklist.isManual())
				continue;
			List<ChecklistSchedule> checklistSchedules = checklistService.getChecklistSchedulesInTimeRange(checklist,
					utcStartOfTheDayInMillis, utcEndOfTheDayInMillis);
			checklistScheduleDao.addChecklistSchedulesList(checklistSchedules);

		}
	}

}
