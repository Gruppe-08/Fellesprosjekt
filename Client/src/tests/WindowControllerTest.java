package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.media.jfxmedia.logging.Logger;
import communication.ClassRegistration;

import controllers.AdminController;
import controllers.ConnectionController;
import controllers.LoginController;
import controllers.WindowController;
import calendar.State;
import calendar.Window;




public class WindowControllerTest {
	WindowController controller;
	ConnectionController connection;
	Pane pane;
	Object o;
	
	public WindowControllerTest(){
		
		try {
			controller = new WindowController();
			connection = new ConnectionController("localhost", 5437);
			pane = new Pane();
			ClassRegistration.register(connection);
		} catch (IOException e) {
			Logger.logMsg(Logger.ERROR, e.getMessage());
		}
		
	}
	
	@Test
	public void testLoadLogin() {
		// Login is loaded without a controller
		assertNotNull(controller.loadPage(Window.LOGIN, null, pane));
		assertNull(controller.loadPage(Window.LOGIN, new LoginController(), pane));
	}
	
	
	/*
	 * Stratup the application
	 * This is needed for the controller tests to run
	 */
	public static class AsNonApp extends Application {
	    @Override
	    public void start(Stage primaryStage) throws Exception {
	        // noop
	    }
	}
	
	@BeforeClass
	public static void initJFX() {
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(AsNonApp.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	}
}
