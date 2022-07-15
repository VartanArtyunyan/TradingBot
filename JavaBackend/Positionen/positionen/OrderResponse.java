package positionen;

public class OrderResponse {
	
	private boolean succesfull;
	private boolean tradeReduced;
	private int tradeID;
	private String reasonForejection;

	public OrderResponse(boolean succesfull, boolean tradeReduced, int orderID) {
		this.succesfull = succesfull;
		this.tradeID = orderID;
		this.tradeReduced = tradeReduced;
		reasonForejection = "";
	}

	public boolean wasSuccesfull() {
		return succesfull;
	}

	public int getOrderID() {
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
