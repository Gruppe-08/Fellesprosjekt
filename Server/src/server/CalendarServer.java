package server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.management.remote.NotificationResult;

import models.Appointment;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sun.media.jfxmedia.logging.Logger;

import communication.requests.AppointmentRequest;
import communication.requests.AuthenticationRequest;
import communication.requests.BusyCheckRequest;
import communication.requests.CreateUserRequest;
import communication.requests.DeleteAppointmentRequest;
import communication.requests.GetGroupsRequest;
import communication.requests.GetUsersRequest;
import communication.requests.CreateGroupRequest;
import communication.requests.CreateUserRequest;
import communication.requests.DeleteAppointmentRequest;
import communication.requests.GetUsersRequest;
import communication.requests.NotificationRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.AppointmentResponse;
import communication.responses.AuthenticationResponse;
import communication.responses.BaseResponse;
import communication.responses.BusyCheckResponse;
import communication.responses.CreateUserResponse;
import communication.responses.GroupResponse;
import communication.responses.NotificationResponse;
import communication.responses.PutAppointmentResponse;
import controllers.GroupController;
import controllers.AppointmentController;
import controllers.NotificationController;
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
	    			AuthenticationResponse response = UserController.handleAuthenticationResponse(request);
	    			
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
	    		/* TODO: Provide more cases for different models, we need to create some model 
	    		 * to send that allows the server to know what models the client requested.
	    		 */
				else if(object instanceof NotificationRequest){
					NotificationRequest req = (NotificationRequest)object;
					if (req.getType().equals("read")) {
						NotificationController.setReadNotification(req.getAppointmentId());
					}
					else if (req.getType().equals("status")) {
						NotificationController.setStatus(req.getAppointmentId(), clientConnection.username, req.getStatus());
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
				
					BaseResponse response = UserController.handleGetUsersResponse(request);
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
