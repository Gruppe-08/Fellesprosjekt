package controllers;

import models.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import calendar.State;
import communication.requests.CreateGroupRequest;
import communication.responses.BaseResponse;

public class GroupController {
	public static void createGroup(Group group){
		CreateGroupRequest req = new CreateGroupRequest(group);
		State.getConnectionController().sendTCP(req);
		
		BaseResponse res = (BaseResponse) State.getConnectionController().getObject("communication.responses.BaseResponse");
		
		Alert groupAlert;
		if (res.wasSuccessful()){
			groupAlert = new Alert(AlertType.INFORMATION ,"Group created");	
		} else {
			groupAlert = new Alert(AlertType.WARNING, "Faled to create group.");
		}
		groupAlert.showAndWait();
	}

}
