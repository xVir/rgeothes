package edu.ict.rgeothes.search;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;

public class DateRange {

	private int precision = Calendar.YEAR;

	private Date beginDate;
	private Date endDate;

	public DateRange() {
	}
	
	public DateRange(int year) {
		GregorianCalendar calendar = new GregorianCalendar(
				TimeZone.getTimeZone("UTC"));
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		beginDate = calendar.getTime();
		
		endDate = null;
	}

	public DateRange(int beginYear, int endYear) {

		GregorianCalendar calendar = new GregorianCalendar(
				TimeZone.getTimeZone("UTC"));
		calendar.set(Calendar.YEAR, beginYear);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		beginDate = calendar.getTime();

		calendar.set(Calendar.YEAR, endYear);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		endDate = calendar.getTime();

	}

	public DateRange(Date beginDate, Date endDate) {
		this.beginDate = beginDate;
		this.endDate = endDate;

	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isDateInRange(Date date) {
		assert beginDate != null;

		if (date == null) {
			return false;
		}

		final Date truncatedBeginDate = DateUtils
				.truncate(beginDate, precision);

		if (endDate != null) {
			Date truncatedEndDate = DateUtils.truncate(endDate, precision);

			if (date.before(truncatedEndDate) && date.after(truncatedBeginDate)) {
				return true;
			}

		} else {
			final Date truncatedDate = DateUtils.truncate(date, precision);
			if (truncatedBeginDate.compareTo(truncatedDate) == 0) {
				return true;
			}
		}

		return false;

	}

}
