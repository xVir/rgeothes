package edu.ict.rgeothes.tools;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateBuilder {

	public static Date buildDate(int year, int month, int day) {
		
		Date result = new Date();
		
		result = DateUtils.setYears(result, year);
		result = DateUtils.setMonths(result, month);
		result = DateUtils.setDays(result, day);
		
		return result;
	}
	
}
