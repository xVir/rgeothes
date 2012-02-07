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
		beginDate = edu.ict.rgeothes.utils.DateUtils.CreateDate(year, Calendar.JANUARY, 1);
		endDate = null;
	}

	public DateRange(int beginYear, int endYear) {
		beginDate = edu.ict.rgeothes.utils.DateUtils.CreateDate(beginYear, Calendar.JANUARY, 1);
		endDate = edu.ict.rgeothes.utils.DateUtils.CreateDate(endYear, Calendar.DECEMBER, 31);
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

	/**
	 * Returns true only if specified date is in this range
	 * @param date
	 * @return
	 */
	public boolean isDateInRange(Date date) {
		assert beginDate != null;

		if (date == null) {
			return false;
		}

		final Date roundedBeginDate = DateUtils
				.round(beginDate, precision);

		if (endDate != null) {
			Date roundedEndDate = DateUtils.round(endDate, precision);

			Date roundedDate = DateUtils.round(date, precision);
			
			if (date.before(roundedEndDate) 
					&& date.after(roundedBeginDate)) {
				return true;
			}
			
			if (roundedDate.equals(roundedBeginDate) || 
					roundedDate.equals(roundedEndDate)) {
				return true;
			}
		

		} else {
			final Date truncatedDate = DateUtils.round(date, precision);
			if (roundedBeginDate.compareTo(truncatedDate) == 0) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Returns true if two date ranges have common points 
	 * @param dateRange
	 * @return
	 */
	public boolean intersects(DateRange dateRange) {
		
		if(contains(dateRange) || dateRange.contains(this)){
			return true;
		}
		
		if (dateRange.isDateInRange(beginDate) && !dateRange.isDateInRange(endDate)) {
			return true;
		}
		
		if (!dateRange.isDateInRange(beginDate) && dateRange.isDateInRange(endDate)) {
			return true;
		}
		
		return false;
	}

	/**
	 * Returns true if this date range fully contains specified date range
	 * @param dateRange
	 * @return
	 */
	public boolean contains(DateRange dateRange) {
		
		if (isDateInRange(dateRange.getBeginDate()) && isDateInRange(dateRange.getEndDate())) {
			return true;
		}
		
		// TODO create unit test for DateRange.contains
		return false;
	}

}
