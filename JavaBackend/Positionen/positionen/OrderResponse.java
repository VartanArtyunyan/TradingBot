package positionen;

public class OrderResponse {
	
	private boolean succesfull;
	private boolean tradeReduced;
	private String tradeID;

	public OrderResponse(boolean succesfull, boolean tradeReduced, String orderID) {
		this.succesfull = succesfull;
		this.tradeID = orderID;
		this.tradeReduced = tradeReduced;
	}

	public boolean wasSuccesfull() {
		return succesfull;
	}

	public String getOrderID() {
		return tradeID;
	}

}
