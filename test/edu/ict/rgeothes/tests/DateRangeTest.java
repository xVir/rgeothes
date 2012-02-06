package edu.ict.rgeothes.tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import edu.ict.rgeothes.search.DateRange;
import edu.ict.rgeothes.utils.DateUtils;

public class DateRangeTest {

	@Test
	public void testIsDateInRange() {
		
		DateRange range = new DateRange(2000, 2010);
		
		Date date1 = DateUtils.CreateDate(1999, 12, 31);
		assertFalse(range.isDateInRange(date1));
		
		Date date2 = DateUtils.CreateDate(2000, 12, 31);
		assertTrue(range.isDateInRange(date2));
		
		Date date3 = DateUtils.CreateDate(2010, 12, 31);
		assertTrue(range.isDateInRange(date3));
		
		Date date4 = DateUtils.CreateDate(2011, 2, 3);
		assertFalse(range.isDateInRange(date4));
	}
	
	
	public void testIntresects(){
		DateRange range1 = new DateRange(2000, 2010);
		DateRange range2 = new DateRange(1996, 1997);
		
		assertTrue(range1.intersects(range1));
		assertTrue(range2.intersects(range2));
		
		assertFalse(range1.intersects(range2));
		assertFalse(range2.intersects(range1));
		
		DateRange range3 = new DateRange(1997, 2000);
		assertTrue(range1.intersects(range3));
		assertTrue(range3.intersects(range1));
		assertTrue(range2.intersects(range3));
		assertTrue(range3.intersects(range2));
		
		DateRange range4 = new DateRange(1998,1999);
		assertFalse(range1.intersects(range4));
		assertFalse(range4.intersects(range1));
		assertFalse(range2.intersects(range4));
		assertFalse(range4.intersects(range2));
	}

}
