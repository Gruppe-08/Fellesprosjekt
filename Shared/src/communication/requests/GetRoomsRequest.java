package communication.requests;

import java.time.LocalDateTime;

public class GetRoomsRequest {
	private boolean onlyAvailable = false;
	private String fromTime, toTime;
	
	public GetRoomsRequest() {}
	
	public GetRoomsRequest(String fromTime, String toTime) {
		onlyAvailable = true;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public boolean getOnlyAvailable() { return onlyAvailable; }

	public String getFromTime() { return fromTime; }

	public String getToTime() { return toTime; }
}
