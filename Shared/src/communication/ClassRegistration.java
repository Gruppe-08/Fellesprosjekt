package communication;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/* This class is responsible for registering all classes that can be serialized
 * through Kryo and subsequently sent over a connection. This class MUST be identical in
 * both server and client for the system to work properly. 
 */

public abstract class ClassRegistration {

	public static void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(communication.requests.AuthenticationRequest.class);
		kryo.register(communication.requests.PutAppointmentRequest.class);
		kryo.register(communication.requests.CreateUserRequest.class);
		kryo.register(communication.requests.AppointmentRequest.class);
		kryo.register(communication.requests.DeleteAppointmentRequest.class);
		kryo.register(communication.requests.CreateGroupRequest.class);
		
		kryo.register(communication.responses.AuthenticationResponse.class);
		kryo.register(communication.responses.AppointmentResponse.class);
		kryo.register(communication.responses.CreateUserResponse.class);
		kryo.register(communication.responses.BaseResponse.class);
		kryo.register(communication.responses.PutAppointmentResponse.class);
		
		kryo.register(models.User.class);
		kryo.register(models.RepetitionType.class);
		kryo.register(models.Room.class);
		kryo.register(models.Appointment.class);
		kryo.register(models.Group.class);
		
		kryo.register(java.util.ArrayList.class);
		kryo.register(java.time.LocalDateTime.class);
		
		kryo.register(javafx.beans.property.SimpleStringProperty.class);
	}
}
