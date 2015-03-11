package util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;


public class DateUtil {
	final static private DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	final static private DateTimeFormatter defaultTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	final static private DateTimeFormatter defaultDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static String getNow() {
		return LocalDateTime.now().format(defaultDateTimeFormatter);
	}
	
	public static boolean isAfterToday(LocalDateTime dateTime){
		return !dateTime.toLocalDate().isBefore(LocalDate.now());
	}

	public static LocalDateTime deserializeDateTime(String str) {
		return LocalDateTime.parse(str, defaultDateTimeFormatter);
	}
	
	public static String serializeDateTime(LocalDateTime localDateTime) {
		return localDateTime.format(defaultDateTimeFormatter);
	}

	public static LocalTime deserializeTime(String str) {
		return LocalTime.parse(str, defaultTimeFormatter);
	}
	
	public static String serializeTime(LocalTime localTime) {
		return localTime.format(defaultTimeFormatter);
	}
	
	public static LocalDate deserializeDate(String str) {
		return LocalDate.parse(str, defaultDateFormatter);
	}
	
	public static String serializeDate(LocalDate localDate) {
		return localDate.format(defaultDateFormatter);
	}
	
	public static int getDayOfWeek(String dateTimeString) {
		return deserializeDateTime(dateTimeString).getDayOfWeek().getValue();
	}
	
	public static int getWeekOfYear(String dateTimeString) {
		LocalDateTime dateTime = deserializeDateTime(dateTimeString);
		Calendar cal = Calendar.getInstance(Locale.GERMANY);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(dateTime.getYear(), dateTime.getMonthValue()-1, dateTime.getDayOfMonth());	//Subtract 1 for different definition of first month in year
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int getYear(String dateTimeString) {
		LocalDateTime dateTime = deserializeDateTime(dateTimeString);
		return dateTime.getYear();
	}
	
	public static LocalDate getDateOfDayInWeek(int year, int week, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setWeekDate(year, week, day);
		String calYear = String.valueOf(cal.get(Calendar.YEAR));
		String calMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
		String calDay = String.valueOf(cal.get(Calendar.DATE));
		if (Integer.valueOf(calMonth) < 10) {
			calMonth = "0" + calMonth;
		}
		if (Integer.valueOf(calDay) < 10) {
			calDay = "0" + calDay;
		}
		
		return deserializeDate(String.format("%s-%s-%s", calYear, calMonth, calDay));
	}
	
	public static int[] datesInSameMonth(LocalDate dateTime1, LocalDate dateTime2) {
		int[] months = new int[2];
		months[0] = dateTime1.getMonth().getValue();
		months[1] = dateTime2.getMonth().getValue();
		return months;
	}
	
	
	
	public static String presentString(String dateString){
		LocalDateTime from = deserializeDateTime(dateString.substring(0, 16));
		String month = from.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		String day = Integer.toString(from.getDayOfMonth());
		String time = String.format("%02d:%02d", from.getHour(), from.getMinute());
		String result = String.format("%s %s - %s", month, day, time);
		return result;
	}
}