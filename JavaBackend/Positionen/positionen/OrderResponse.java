package positionen;

public class OrderResponse {
	
	private boolean succesfull;
	private String orderID;

	public OrderResponse(boolean succesfull, String orderID) {
		this.succesfull = succesfull;
		this.orderID = orderID;
	}

	public boolean wasSuccesfull() {
		return succesfull;
	}

	public String getOrderID() {
		return orderID;
	}

}
