package server;

import java.sql.Connection;
import java.sql.DriverManager;

import com.sun.media.jfxmedia.logging.Logger;

public abstract class DatabaseConnector {
	
	private static Connection db;
	
	public static void initializeDatabase() {
		String url = "jdbc:mysql://80.202.24.127/calendar";
		String username = "root";
		String password = "eirik12345";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			db = DriverManager.getConnection(url, username, password);
		}
		catch (Exception e) {
			Logger.logMsg(Logger.ERROR, e.getMessage());
		}
	}

	public static Connection getDB() {
		return db;
	}
}
