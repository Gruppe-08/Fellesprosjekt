package tests;

import static org.junit.Assert.*;

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
	
}



