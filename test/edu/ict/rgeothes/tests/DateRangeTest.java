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
		
		DateRange range2 = new DateRange(2000);
		assertTrue(range2.isDateInRange(date2));
		assertFalse(range2.isDateInRange(date1));
		assertFalse(range2.isDateInRange(date3));
		assertFalse(range2.isDateInRange(date4));
	}
	
	@Test
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
		
		DateRange range5 = new DateRange(2002);
		assertTrue(range5.intersects(range5));
		assertTrue(range1.intersects(range5));
		assertTrue(range5.intersects(range1));
		assertFalse(range2.intersects(range5));
		assertFalse(range5.intersects(range5));
		
		DateRange range5_1 = new DateRange(2002);
		assertTrue(range5.intersects(range5_1));
		assertTrue(range5_1.intersects(range5));
		
		DateRange range6 = new DateRange(2003);
		assertFalse(range6.intersects(range5));
		assertFalse(range5.intersects(range6));	
	}
	
	@Test
	public void testContains(){
		DateRange range1 = new DateRange(2000, 2010);
		assertTrue(range1.contains(range1));
		
		DateRange range2 = new DateRange(1996, 1997);
		assertTrue(range2.contains(range2));
		
		assertFalse(range2.contains(range1));
		assertFalse(range2.contains(range1));
		
		DateRange range3 = new DateRange(1996, 1996);
		assertFalse(range1.contains(range3));
		assertFalse(range3.contains(range1));
		assertTrue(range2.contains(range3));
		assertFalse(range3.contains(range2));
		
		DateRange range4 = new DateRange(1998, 2001);
		assertFalse(range1.contains(range4));
		assertFalse(range2.contains(range4));
		assertFalse(range4.contains(range1));
		assertFalse(range4.contains(range2));
		
		DateRange range5 = new DateRange(2001, 2005);
		assertTrue(range1.contains(range5));
		assertFalse(range5.contains(range1));
		assertFalse(range2.contains(range5));
		assertFalse(range5.contains(range2));
		
		DateRange range6 = new DateRange(2002);
		assertTrue(range1.contains(range6));
		assertFalse(range6.contains(range1));
		assertFalse(range2.contains(range6));
		assertFalse(range6.contains(range2));
		
		DateRange range7 = new DateRange(2000);
		assertTrue(range1.contains(range7));
		
		DateRange range7_1 = new DateRange(2000);
		assertTrue(range7.contains(range7_1));
		assertTrue(range7_1.contains(range7));
		
		DateRange range8 = new DateRange(2010);
		assertTrue(range1.contains(range8));
		assertFalse(range7.contains(range8));
		assertFalse(range8.contains(range7));
		
	}

}
