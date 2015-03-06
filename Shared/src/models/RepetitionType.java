package models;

public enum RepetitionType {
	DAILY, WEEKLY, MONTHLY, YEARLY;
	
	public static RepetitionType fromString(String str) {
		str = str.toUpperCase();
		if(str.equals("DAILY"))
			return DAILY;
		if(str.equals("WEEKLY"))
			return WEEKLY;
		if(str.equals("MONTHLY"))
			return MONTHLY;
		if(str.equals("YEARLY"))
			return YEARLY;
		return null;
	}
}
