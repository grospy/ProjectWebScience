package application;

import java.util.HashMap;

public class EdgeRepo extends AbstractRepo<Edge> {

	@Override
	protected Edge getObject(HashMap<String, String> objectProperties) {
		
        String id1 = objectProperties.get("restaurantID");
        String id2 = objectProperties.get("userID");
        int foodGrade = Integer.parseInt(objectProperties.get("foodGrade"));
        int serviceGrade = Integer.parseInt(objectProperties.get("serviceGrade"));
        int decorGrade = Integer.parseInt(objectProperties.get("decorGrade"));
        
		Edge oneEdge = new Edge(id1, id2, foodGrade, serviceGrade, decorGrade);
		return oneEdge;
	}
}
