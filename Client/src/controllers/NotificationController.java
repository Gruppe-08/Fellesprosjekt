package controllers;

import rmi.NotificationCtrl;

public class NotificationController implements NotificationCtrl{

	@Override
	public void sayHello() {
		System.out.println("Hello notifications!");	
	}

}
