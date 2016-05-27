package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Graph {
	private static ArrayList<Vertex> vertices;
	private static ArrayList<Edge> edges;
	private static Graph oneInstance = null;
	
	private Graph() {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
	}
	
	public static Graph getInstance(){
		if (oneInstance == null) {
			oneInstance = new Graph();
		}
		return oneInstance;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void addEdge(Vertex v1, Vertex v2, int foodGrade, int serviceGrade, int decorGrade) {
		Edge temp = findEdge(v1, v2);
		if (temp == null) {
			Edge e = new Edge(v1, v2, foodGrade, serviceGrade, decorGrade);
			edges.add(e);
		}
	}

	private static Vertex findVertex(Vertex v) {
		for (Vertex oneVertex : vertices) {
			if (oneVertex.compareTo(v) == 0)
				return oneVertex;
		}
		return null;
	}
	
	public Vertex findVertexLink(String id) {
		for (Vertex oneVertex : vertices) {
			if (oneVertex.getId() == id)
				return oneVertex;
		}
		return null;
	}

	private Edge findEdge(Vertex v1, Vertex v2) {
		for (Edge oneEdge : edges) {
			if ((oneEdge.v1.equals(v1) && oneEdge.v2.equals(v2)) || (oneEdge.v1.equals(v2) && oneEdge.v2.equals(v1))) {
				return oneEdge;
			}
		}
		return null;
	}
	
	public void clearStorage() {
		Graph.vertices.clear();
		Graph.edges.clear();
	}
	
	// SUGGESTIONS HERE
	public ArrayList<Suggestion> getSuggestions(Vertex root,int x) {
		ArrayList<Suggestion> sg = new ArrayList<Suggestion>();
		ArrayList<Vertex> adjUsers = root.getAdj();
		for(Vertex user: adjUsers) {
			for (Vertex restaurant: user.getAdj()) {
				if (!restaurant.equals(root)) {
					
					float grade;
					if (x == 1) {
						grade = findEdge(restaurant, user).foodGrade;
					} else if (x == 2) {
						grade = findEdge(restaurant, user).serviceGrade;
					} else {
						grade = findEdge(restaurant, user).decorGrade;
					}
					
					if (!Suggestion.suggestions.contains(restaurant)) {
						sg.add(new Suggestion(restaurant, grade));
					} else {
						for(Suggestion s: sg) {
							float oldGrade = s.getGrade();
							if (s.getVertex().equals(restaurant)) {
								s.setGrade(oldGrade+grade/2); 
							}
						}
					}
				}
			}
		}
		Collections.sort(sg);
		return sg;
	}
	
	@Override
	public String toString() {
		String output = "";
		for (Vertex oneVertex : vertices) {
			output += oneVertex.toString() + "\n";
		}
		return output;
	}

	public String edgesToString() {
		String output = "";
		for (Edge oneEdge : edges) {
			output += oneEdge + "\n";
		}
		return output;
	}
	
	// manually write xml -> to be replaced
	public void toXML() throws IOException {
		File file = new File("edgesXML.xml");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		fileWriter.write("<edges>");
		String content;
		for (Edge e: edges) {
			content = "";
			content += "<edge>";
			content += "<restaurant>" + e.v1.getName() + "</restaurant>";
			content += "<user>" + e.v2.getName() + "</user>";
			content += "<food>" + e.foodGrade + "</food>";
			content += "<service>" + e.serviceGrade + "</service>";
			content += "<decor>" + e.decorGrade + "</decor>";
			content += "</edge>";
			fileWriter.write(content);
		}
		fileWriter.write("</edges>");
		fileWriter.flush();
		fileWriter.close();
	}

	static class Edge {
		Vertex v1;
		Vertex v2;
		int foodGrade;
		int serviceGrade;
		int decorGrade;

		public Edge(Vertex newV1, Vertex newV2, int foodGrade, int serviceGrade, int decorGrade) {
			v1 = findVertex(newV1);
			if (v1 == null) {
				v1 = newV1;
				vertices.add(newV1);
			}
			v2 = findVertex(newV2);
			if (v2 == null) {
				v2 = newV2;
				vertices.add(newV2);
			}
			this.foodGrade = foodGrade;
			this.serviceGrade = serviceGrade;
			this.decorGrade = decorGrade;

			v1.add(v2);
			v2.add(v1);
		}
		
		public Edge(String id1, String id2, int foodGrade, int serviceGrade, int decorGrade) throws IOException {
			Vertex v1 = new Vertex(id1);
			Vertex v2 = new Vertex(id2);
			vertices.add(v1);
			vertices.add(v2);
			this.foodGrade = foodGrade;
			this.serviceGrade = serviceGrade;
			this.decorGrade = decorGrade;

			v1.add(v2);
			v2.add(v1);
		}

		@Override
		public String toString() {
			return "Edge: " + v1.getName() + " -- " + v2.getName() + " weights = " + foodGrade+"/"+serviceGrade+"/"+decorGrade;
		}
	}

}