package positionen;

public class OrderResponse {
	
	private boolean succesfull;
	private boolean tradeReduced;
	private String tradeID;
	private String reasonForejection;

	public OrderResponse(boolean succesfull, boolean tradeReduced, String orderID) {
		this.succesfull = succesfull;
		this.tradeID = orderID;
		this.tradeReduced = tradeReduced;
		reasonForejection = "";
	}

	public boolean wasSuccesfull() {
		return succesfull;
	}

	public String getOrderID() {
		return tradeID;
	}
	
	public void setReasonForRejection(String reason) {
		this.reasonForejection = reason;
	}
	
	public String getReasonForRejection() {
		if(succesfull) return "the Order was not rejected";
		return reasonForejection;
	}

}
