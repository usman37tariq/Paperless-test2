package com.engro.paperlessbackend.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class CalcChecklistScheduleFrequency {

	public String unit;
	public Long frequency;

	public CalcChecklistScheduleFrequency() {
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getFrequency() {
		return frequency;
	}

	public void setFrequency(Long frequency) {
		this.frequency = frequency;
	}

	public CalcChecklistScheduleFrequency calcFrequencyOfChecklistSchedule(long startTime, long endTime) {
		Date start = new Date(startTime);
		Date end = new Date(endTime);

		long days = getDateDiff(start, end, TimeUnit.DAYS); // 31
		long hours = getDateDiff(start, end, TimeUnit.HOURS); // 744
		long minutes = getDateDiff(start, end, TimeUnit.MINUTES); // 44640

		return calcDiff(days, hours, minutes);

	}

	private CalcChecklistScheduleFrequency calcDiff(long days, long hours, long minutes) {

		CalcChecklistScheduleFrequency calc = new CalcChecklistScheduleFrequency();
		if (days != 0) {
			if (days % 28 == 0 || days % 29 == 0 || days % 30 == 0 || days % 31 == 0) {
				unit = "months";
				frequency = days / 28;
				calc.setUnit(unit);
				calc.setFrequency(frequency);
				return calc;
			}
		}
		if (hours != 0) {
			if (hours % 24 == 0) {
				unit = "days";
				frequency = hours / 24;
				calc.setUnit(unit);
				calc.setFrequency(frequency);
				return calc;
			}
		}
		if (minutes != 0) {
			if (minutes % 60 == 0) {
				unit = "hours";
				frequency = minutes / 28;
				calc.setUnit(unit);
				calc.setFrequency(frequency);
				return calc;
			} else {
				unit = "minutes";
				frequency = minutes;
				calc.setUnit(unit);
				calc.setFrequency(frequency);
				return calc;
			}
		}
		return calc;
	}

	private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();

		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
}
