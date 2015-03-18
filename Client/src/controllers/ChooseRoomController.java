package controllers;

import calendar.State;
import communication.requests.GetRoomsRequest;
import communication.responses.PutAppointmentResponse;
import communication.responses.RoomResponse;
import models.Room;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ChooseRoomController {

	@FXML
	private AnchorPane root;
    @FXML
    private TableView<Room> root_table;

    @FXML
    private TableColumn<Room, String> name_column;

    @FXML
    private TableColumn<Room, String> capacity_column;
    
    void initialize(Stage myStage, AppointmentController controller) {
    	if(controller.appointment.getStartTime() == null || controller.appointment.getEndTime() == null) {
    		root.getChildren().remove(root_table);
    		System.out.println("invalid");
    		return;
    	}
    	
    	root_table.setRowFactory(new Callback<TableView<Room>, TableRow<Room>>() {
			@Override
			public TableRow<Room> call(TableView<Room> param) {
				TableRow<Room> row = new TableRow<Room>();
				row.setOnMouseClicked(new EventHandler<Event>() {
					@Override
					public void handle(Event event) {
						controller.setRoom(row.getItem());
						myStage.close();
					}			
				});
				return row;
			}
		});
    	name_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Room, String> param) {
				return new ReadOnlyStringWrapper(param.getValue().getName());
			}
		});
    	capacity_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Room, String> param) {
				return new ReadOnlyStringWrapper(param.getValue().getCapacity().toString());
			}
		});
    	
    	GetRoomsRequest request = new GetRoomsRequest(
    			controller.appointment.getStartTime(),
    			controller.appointment.getEndTime());
    	State.getConnectionController().sendTCP(request);
    	RoomResponse response = (RoomResponse) State.getConnectionController().getObject(
    			"communication.responses.RoomResponse");
    	
    	for(Room room : response.getRooms()) {
    		root_table.getItems().add(room);
    	}
    }

}
