package positionen;

public class OrderResponse {
	
	private boolean succesfull;
	private String tradeID;

	public OrderResponse(boolean succesfull, String orderID) {
		this.succesfull = succesfull;
		this.tradeID = orderID;
	}

	public boolean wasSuccesfull() {
		return succesfull;
	}

	public String getOrderID() {
		return tradeID;
	}

}
