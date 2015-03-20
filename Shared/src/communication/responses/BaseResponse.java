package communication.responses;

public class BaseResponse {
	private boolean wasSuccessful = true;
	private String errorMessage;
	
	public BaseResponse() {}
	
	public boolean wasSuccessful() {
		return wasSuccessful;
	}
	public void setSuccessful(boolean wasSuccessful) {
		this.wasSuccessful = wasSuccessful;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
}
