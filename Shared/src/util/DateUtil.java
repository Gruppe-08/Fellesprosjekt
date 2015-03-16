package util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;


public class DateUtil {
	final static private DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	final static private DateTimeFormatter defaultTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	final static private DateTimeFormatter defaultDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private static final int TIME_START = 11;
	private static final int DATE_START = 0;
	private static final int DATE_END = 10;
	
	public static String getNow() {
		return LocalDateTime.now().format(defaultDateTimeFormatter);
	}
	
	public static boolean isAfterToday(LocalDateTime dateTime){
		return !dateTime.toLocalDate().isBefore(LocalDate.now());
	}

	public static LocalDateTime deserializeDateTime(String str)  throws DateTimeParseException {
		return LocalDateTime.parse(str, defaultDateTimeFormatter);
	}
	
	public static String serializeDateTime(LocalDateTime localDateTime)  throws DateTimeParseException {
		return localDateTime.format(defaultDateTimeFormatter);
	}

	public static LocalTime deserializeTime(String str) {
		return LocalTime.parse(str.substring(TIME_START), defaultTimeFormatter);
	}
	
	public static String serializeTime(LocalTime localTime)  throws DateTimeParseException {
		return localTime.format(defaultTimeFormatter);
	}
	
	public static LocalDate deserializeDate(String str) {
		return LocalDate.parse(str.substring(DATE_START,DATE_END), defaultDateFormatter);
	}
	
	public static String serializeDate(LocalDate localDate) throws DateTimeParseException {
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
	
	public static LocalDateTime getDateOfDayInWeek(int year, int week, int day) {
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
		
		return deserializeDateTime(String.format("%s-%s-%s 12:00", calYear, calMonth, calDay));
	}
	
	public static int[] datesInSameMonth(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		int[] months = new int[2];
		months[0] = dateTime1.getMonth().getValue();
		months[1] = dateTime2.getMonth().getValue();
		return months;
	}
	
	public static int getFirstDayOfYear(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, 0, 1);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Boolean isLeapYear(int year) {
		return (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0);
	}
	
	public static int getNumberOfWeeksInYear(int year) {
		if (getFirstDayOfYear(year) == 5) {
			return 53;
		}
		else if (isLeapYear(year) && (getFirstDayOfYear(year) == 4 || getFirstDayOfYear(year) == 5)) {
			return 53;
		}
		else return 52;
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