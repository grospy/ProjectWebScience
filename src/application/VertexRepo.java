package application;

import java.util.HashMap;

public class VertexRepo extends AbstractRepo<Vertex>{

	@Override
	protected Vertex getObject(HashMap<String, String> objectProperties) {

		String id = objectProperties.get("id");
		String name = objectProperties.get("name");
        String link = objectProperties.get("link");
        boolean restaurant = Boolean.parseBoolean(objectProperties.get("restaurant"));
		
		Vertex oneVertex = new Vertex(id, name, link, restaurant);
		return oneVertex;
	}
	
}
