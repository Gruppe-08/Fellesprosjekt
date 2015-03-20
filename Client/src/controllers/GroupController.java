package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import calendar.State;
import calendar.Window;
import communication.requests.GroupRequest;
import communication.responses.GroupResponse;
import models.Group;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class GroupController implements Initializable {

	@FXML private Label nameTextField;
	@FXML private TextArea descriptionTextArea;
	@FXML private ListView<Group> groupList;
	@FXML private ListView<String> memberList;
	@FXML private Button addButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				State.getWindowController().loadPage(Window.CREATE_GROUP);
			}
		});

		//Sets cell factories for the groupList and memberList views
		groupList.setCellFactory(new Callback<ListView<Group>, ListCell<Group>>() {
			@Override
			public ListCell<Group> call(ListView<Group> param) {
				return new GroupCell();
			}
		});

		groupList.selectionModelProperty().get().selectedItemProperty().addListener(new ChangeListener<Group>() {

			@Override
			public void changed(ObservableValue<? extends Group> observable, Group oldValue, Group newValue) {
				nameTextField.setText(newValue.getName());
				populateMemberList(newValue.getMembers());
			}
		});
		
		populateGroupList();
		
	}

	public ArrayList<Group> getGroups() {
		GroupRequest req = new GroupRequest();
		req.setUsername(State.getUser().getUsername());
		req.setType("groups");
		State.getConnectionController().sendTCP(req);
		GroupResponse response = (GroupResponse)State.getConnectionController().getObject("communication.responses.GroupResponse");
		return response.getGroups();
	}

	private void populateGroupList() {
		groupList.setItems(FXCollections.observableArrayList(getGroups()));
	}
	
	private void populateMemberList(ArrayList<String> items) {
		memberList.setItems(FXCollections.observableArrayList(items));
	}

	class GroupCell extends ListCell<Group> {

		private Button btn;
		private Label label;
		private Group item;
		private GridPane pane;

		public GroupCell() {
			super();
			btn = new Button("Leave group");
			pane = new GridPane();
			label = new Label();

			btn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					leaveGroup(item.getGroupID(), State.getUser().getUsername());
				}

			});

			pane.add(btn, 0, 0);
			pane.add(label, 1, 0);
		}

		@Override 
		protected void updateItem(Group item, boolean empty) {
			this.item = item;
			super.updateItem(item, empty);
			if (item != null) {
				label.setText(item.getName());                          
				setGraphic(pane);
			} else {
				setGraphic(null);
			}		
		}
	}

	private void leaveGroup(int groupId, String username) {
		System.out.println(groupId + username);
		GroupRequest req = new GroupRequest();
		req.setType("leave");
		req.setGroupId(groupId);
		req.setUsername(username);
		State.getConnectionController().sendTCP(req);
		State.getConnectionController().getObject("communication.responses.GroupResponse");
	}
}


