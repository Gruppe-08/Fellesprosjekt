package server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sun.media.jfxmedia.logging.Logger;

import communication.requests.AppointmentRequest;
import communication.requests.AuthenticationRequest;
import communication.requests.BusyCheckRequest;
import communication.requests.ChangeAppointmentStatusRequest;
import communication.requests.CreateUserRequest;
import communication.requests.DeleteAppointmentRequest;
import communication.requests.GetGroupsRequest;
import communication.requests.GetRoomsRequest;
import communication.requests.GetUsersRequest;
import communication.requests.CreateGroupRequest;
import communication.requests.GroupRequest;
import communication.requests.NotificationRequest;
import communication.requests.PutAppointmentRequest;
import communication.requests.UpdateUserRequest;
import communication.responses.AppointmentResponse;
import communication.responses.AuthenticationResponse;
import communication.responses.BaseResponse;
import communication.responses.BusyCheckResponse;
import communication.responses.CreateUserResponse;
import communication.responses.GetUsersResponse;
import communication.responses.GroupResponse;
import communication.responses.NotificationResponse;
import communication.responses.PutAppointmentResponse;
import communication.responses.RoomResponse;
import controllers.GroupController;
import controllers.AppointmentController;
import controllers.NotificationController;
import controllers.RoomController;
import controllers.UserController;
import communication.ClassRegistration;

public class CalendarServer extends Server {

	public CalendarServer() {
		
		//Register all classes that can be sent or received by server
		ClassRegistration.register(this);
		
		//Listen for connections
		this.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				//We can safely cast the connection to a clientConnection
				ClientConnection clientConnection = (ClientConnection)connection;
				
				//If the object is an instance of Authentication, we may want to authenticate a client
				if(object instanceof AuthenticationRequest) {

					//If the client is already authenticated, we ignore the request

	    			if(clientConnection.isAuthenticated) {
	    				return;
	    			}
	    			AuthenticationRequest request = (AuthenticationRequest)object;
	    			AuthenticationResponse response = UserController.handleAuthenticationRequest(request);
	    			
					clientConnection.isAuthenticated = response.wasSuccessful();
					if (clientConnection.isAuthenticated) {
						response.setUser(UserController.getUser(request.getUsername()));
						clientConnection.username = request.getUsername();
					}

	    			clientConnection.sendTCP(response);
	    		}
				//If the object is an instance of CreateUserRequest, we want to create the new user
				else if(object instanceof CreateUserRequest) {
					CreateUserRequest request = (CreateUserRequest)object;
					CreateUserResponse response = UserController.handleCreateUserRequest(request);
					clientConnection.sendTCP(response);
				}
				
				//If the client is not authenticated and the object is not an instance of Authentication, a client is attempting
				//to illegally access information
				else if(!clientConnection.isAuthenticated) {
    				return; 
    			}
	    		//--ALL OTHER METHODS SHOULD BE BEYOND THIS POINT--
				else if(object instanceof NotificationRequest){
					NotificationRequest req = (NotificationRequest)object;
					if (req.isRead()) {
						NotificationController.setReadNotification(req.getNotificationId());
					}
					else {
						String username = clientConnection.username;
						NotificationResponse response = NotificationController.getNotificationResponse(username);
						clientConnection.sendTCP(response);
					}
				}
				else if(object instanceof AppointmentRequest) {
					AppointmentRequest request = (AppointmentRequest)object;
					AppointmentResponse response = AppointmentController.handleAppointmentRequest(
							request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof PutAppointmentRequest) {
					PutAppointmentRequest request = (PutAppointmentRequest)object;
				
					PutAppointmentResponse response = AppointmentController.handlePutAppointmentRequest(
							clientConnection.username, request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof DeleteAppointmentRequest) {
					DeleteAppointmentRequest request = (DeleteAppointmentRequest) object;
					BaseResponse response = AppointmentController.handleDeleteAppointment(request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof GetUsersRequest) {
					GetUsersRequest request = (GetUsersRequest) object;
				
					GetUsersResponse response = UserController.handleGetUsersRequest(request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof BusyCheckRequest) {
					BusyCheckRequest request = (BusyCheckRequest) object;
					BusyCheckResponse response = UserController.handleBusyCheck(request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof CreateGroupRequest){
					CreateGroupRequest request = (CreateGroupRequest) object;
					BaseResponse response = GroupController.handleCreateGroupRequest(request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof GetGroupsRequest){
					GetGroupsRequest request = (GetGroupsRequest) object;
					GroupResponse response = GroupController.handleGetGroupsRequest(request);
					clientConnection.sendTCP(response);
				} 
				else if(object instanceof UpdateUserRequest){
					UpdateUserRequest request = (UpdateUserRequest) object;
					BaseResponse response = UserController.handleUpdateUserRequest(request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof GetRoomsRequest) {
					GetRoomsRequest request = (GetRoomsRequest) object;
					RoomResponse response = RoomController.handleGetRoomsRequest(request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof GroupRequest){
					GroupRequest request = (GroupRequest) object;
					GroupResponse response = GroupController.handleGroupRequest(request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof ChangeAppointmentStatusRequest) {
					ChangeAppointmentStatusRequest request = (ChangeAppointmentStatusRequest) object;
					BaseResponse response = AppointmentController.handleStatusChangeRequest(request);
					clientConnection.sendTCP(response);
				}
				else if(object instanceof FrameworkMessage) {}
				//This must always come last!
				else
					Logger.logMsg(Logger.DEBUG, "Request of type: " +
							object.getClass().getName() +
							" was not handled by any controller and was dropped");
			}
			
			public void connected(Connection connection) {
				Logger.logMsg(Logger.DEBUG, "A new client connected");
			}
		});
	}
	@Override
	protected ClientConnection newConnection() {
		//We will use our own ClientConnection to track who has logged in
		return new ClientConnection();
	}

	//Extended connection class that includes a field for keeping track of login state
	public static class ClientConnection extends Connection {
		public boolean isAuthenticated = false;
		public String username = "";
		
		public ClientConnection() {
			super();
		}
	}

	public static void main(String[] args) {
		Logger.setLevel(Logger.DEBUG);
		
		DatabaseConnector.initializeDatabase();
		CalendarServer srv = new CalendarServer();
		srv.start();
		int port = 5437;
		try {
			srv.bind(port);			
		}
		catch(IOException e) {
			Logger.logMsg(Logger.ERROR, e.getMessage());
			System.exit(1);
		}
		Logger.logMsg(Logger.INFO, "Server running at port " + port);
	}
}
