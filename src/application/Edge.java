package application;

public class Edge {

	private String restaurantID;
	private String userID;
	private int foodGrade;
	private int serviceGrade;
	private int decorGrade;
	
	public Edge(String id1,String id2, int foodGrade, int serviceGrade, int decorGrade) {
		synchronized (Graph.getInstance().getVertices()) {
			Vertex v1 = Graph.findVertexByID(id1);
			Vertex v2 = Graph.findVertexByID(id2);
			v1.add(v2);
			v2.add(v1);
		}
		this.restaurantID = id1;
		this.userID = id2;
		this.foodGrade = foodGrade;
		this.serviceGrade = serviceGrade;
		this.decorGrade = decorGrade;
	}
	
	@Override
	public String toString() {
		return "Edge: " + Graph.findVertexByID(restaurantID).getName() + " -- " + Graph.findVertexByID(userID).getName() + " weights = " + foodGrade+"/"+serviceGrade+"/"+decorGrade;
	}

	public String getRestaurantID() {
		return restaurantID;
	}

	public void setRestaurantID(String restaurantID) {
		this.restaurantID = restaurantID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getFoodGrade() {
		return foodGrade;
	}

	public void setFoodGrade(int foodGrade) {
		this.foodGrade = foodGrade;
	}

	public int getServiceGrade() {
		return serviceGrade;
	}

	public void setServiceGrade(int serviceGrade) {
		this.serviceGrade = serviceGrade;
	}

	public int getDecorGrade() {
		return decorGrade;
	}

	public void setDecorGrade(int decorGrade) {
		this.decorGrade = decorGrade;
	}
	
	
}
