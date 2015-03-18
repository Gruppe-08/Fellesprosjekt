package tests;

import static org.junit.Assert.*;
import javafx.application.Application;
import javafx.stage.Stage;

import org.junit.BeforeClass;
import org.junit.Test;

import controllers.AdminController;
import controllers.LoginController;
import controllers.WindowController;
import calendar.Window;




public class WindowControllerTest {
	WindowController controller = new WindowController();
	Object o;
	
	@Test
	public void testLoadLogin() {
		o = controller.loadPage(Window.LOGIN);
		assertNotNull(o);
	}
	
	@Test
	public void testLoadAdmin() {
		o = controller.loadPage(Window.ADMIN, new AdminController());
		assertNotNull(o);
		
		// If the controller is not set, loadPage should return null
		o = controller.loadPage(Window.ADMIN);
		assertNull(o);
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
