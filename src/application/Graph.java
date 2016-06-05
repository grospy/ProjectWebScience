package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {
	private static VertexRepo vertR = new VertexRepo();
	private static EdgeRepo edgeR = new EdgeRepo();
	private static Graph oneInstance = null;
	
	private Graph() {}
	
	public static Graph getInstance(){
		if (oneInstance == null) {
			oneInstance = new Graph();
		}
		return oneInstance;
	}

	public List<Edge> getEdges() {
		return edgeR.getAll();
	}
	
	public List<Vertex> getVertices() {
		return vertR.getAll();
	}
	
	public void save() {
		try {
			vertR.saveAllToXml();
			edgeR.saveAllToXml();
		} catch (FileNotFoundException e) {
			System.out.println("SAVE ERROR");
		}
		
	}
	
	public boolean load() {
		File f1 = new File("storage/application.edgeStorage.xml"); 
		File f2 = new File("storage/application.vertexStorage.xml"); 
		if (f1.exists() && f2.exists()) {
			vertR.loadAllFromXML();
			edgeR.loadAllFromXML();	
			return true;
		} else {
			return false;
		}
	}

	public void addEdge(Vertex v1, Vertex v2, int foodGrade, int serviceGrade, int decorGrade) {
		Vertex localV1 = findVertex(v1);
		Vertex localV2 = findVertex(v2);
		if (localV1 == null) {
			vertR.save(v1);
			localV1 = v1;
		}
		if (localV2 == null) {
			vertR.save(v2);
			localV2 = v2;
		}
		Edge temp = findEdge(localV1, localV2);
		if (temp == null) {
			Edge e = new Edge(localV1.getId(), localV2.getId(), foodGrade, serviceGrade, decorGrade);
			edgeR.save(e);
		} else {
			System.out.println("Already exists");
		}
	}

	private static Vertex findVertex(Vertex v) {
		synchronized (vertR.getAll()) {
			for (Vertex oneVertex : vertR.getAll()) {
				if (oneVertex.compareTo(v) == 0)
					return oneVertex;
			}
			return null;
		}
	}
	
	public static Vertex findVertexByID(String id) {
		synchronized (vertR.getAll()) {
			for (Vertex oneVertex : vertR.getAll()) {
				if (oneVertex.getId().equals(id))
					return oneVertex;
			}
			return null;
		}
	}

	private Edge findEdge(Vertex v1, Vertex v2) {
		synchronized (edgeR.getAll()) {
			for (Edge oneEdge : edgeR.getAll()) {
				if (oneEdge.getRestaurantID().equals(v1.getId()) && oneEdge.getUserID().equals(v2.getId())) {
					return oneEdge;
				}
			}
			return null;
		}
	}
	
	@Override
	public String toString() {
		String output = "";
		List<Vertex> allData = vertR.getAll();
		for (Vertex oneVertex : allData) {
			output += oneVertex.toString() + "\n";
		}
		return output;
	}

	public String edgesToString() {
		String output = "";
		for (Edge oneEdge : edgeR.getAll()) {
			output += oneEdge + "\n";
		}
		return output;
	}
	
	// SUGGESTIONS HERE
	public List<Suggestion> getSuggestions(Vertex root,int x) {
		
		ArrayList<Suggestion> sg = new ArrayList<Suggestion>();
		List<Suggestion> top = new ArrayList<Suggestion>();
		ArrayList<String> suggested = new ArrayList<String>();
		int gradeLimit = 5;
		
		for(Vertex user: root.giveAdj()) {
			Edge e = findEdge(root, user);
			if ((e.getDecorGrade() > gradeLimit) && (e.getFoodGrade() > gradeLimit) && (e.getServiceGrade() > gradeLimit)) {
				
				for (Vertex restaurant: user.giveAdj()) {
					if (!restaurant.equals(root)) {
						
						float grade;
						if (x == 1) {
							grade = (float) findEdge(restaurant, user).getFoodGrade();
						} else if (x == 2) {
							grade = (float) findEdge(restaurant, user).getServiceGrade();
						} else {
							grade = (float) findEdge(restaurant, user).getDecorGrade();
						}
						
						if (!suggested.contains(restaurant.getName())){
							sg.add(new Suggestion(restaurant, grade));
							suggested.add(restaurant.getName());
						} else {
							for(Suggestion s: sg) {
								if (s.getVertex().equals(restaurant)) {
									s.add(new Float(grade));
								}
							}
						}
					}
				}
			} 
		}
		
		for(Suggestion s: sg) {
			s.calculateAV();
		}
		
		Collections.sort(sg);
		
		if (sg.size() < 10) {
			top = sg.subList(0, sg.size());
		} else {
			top = sg.subList(0, 10);
		}
		
		return top;
	}

}