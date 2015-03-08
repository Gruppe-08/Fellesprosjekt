package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	final static private DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public static boolean isAfterToday(LocalDateTime dateTime){
		return !dateTime.toLocalDate().isBefore(LocalDate.now());
	}

	public static LocalDateTime deserialize(String str) {
		return LocalDateTime.parse(str, defaultFormatter);
	}
	
	public static String serialize(LocalDateTime localDateTime) {
		return localDateTime.format(defaultFormatter);
	}
}