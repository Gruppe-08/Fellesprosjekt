package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	final static private DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	final static private DateTimeFormatter defaultTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	final static private DateTimeFormatter defaultDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
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
}