package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import util.DateUtil;


public class DateUtilTest {

	@Test
	public void testdatesInSameMonth(){
		int [] months = DateUtil.datesInSameMonth(DateUtil.deserializeDateTime("2015-03-18 12:00"), DateUtil.deserializeDateTime("2015-03-20 12:00"));
		assertTrue(months[0]==months[1]);
		int [] month = DateUtil.datesInSameMonth(DateUtil.deserializeDateTime("2015-04-18 12:00"), DateUtil.deserializeDateTime("2015-03-20 12:00"));
		assertFalse(month[0]==month[1]);
	}
	
	@Test
	public void testgetDayOfWeek(){
		int day = DateUtil.getDayOfWeek("2015-04-06 11:00");
		assertEquals(1, day);	
	}
	
	@Test
	public void testgetWeekofYear() {
		int week = DateUtil.getWeekOfYear("2015-03-18 11:00");
		assertEquals(12,week);
	}
	
	@Test
	public void testgetDateOfDayInWeek() {
		LocalDateTime date = DateUtil.getDateOfDayInWeek(2015, 12, 3);
		assertEquals("2015-03-17 12:00", DateUtil.serializeDateTime(date)); //blir ikke helt riktig
	}
	
	
}



