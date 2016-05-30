package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {
	private static volatile ArrayList<Vertex> vertices;
	private static volatile ArrayList<Edge> edges;
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
			System.out.println(v1.getName()+" > "+ v1.getId() + " -- " + v2.getName());
		}
	}

	private static Vertex findVertex(Vertex v) {
		synchronized (vertices) {
			for (Vertex oneVertex : vertices) {
				if (oneVertex.compareTo(v) == 0)
					return oneVertex;
			}
			return null;	
		}
	}
	
	public Vertex findVertexLink(String id) {
		synchronized (vertices) {
			for (Vertex oneVertex : vertices) {
				if (oneVertex.getId().equals(id))
					return oneVertex;
			}
			return null;
		}
	}

	private Edge findEdge(Vertex v1, Vertex v2) {
		synchronized (edges) {
			for (Edge oneEdge : edges) {
				if ((oneEdge.v1.equals(v1) && oneEdge.v2.equals(v2)) || (oneEdge.v1.equals(v2) && oneEdge.v2.equals(v1))) {
					return oneEdge;
				}
			}
			return null;
		}
	}
	
	public void clearStorage() {
		Graph.vertices.clear();
		Graph.edges.clear();
	}
	
	// SUGGESTIONS HERE
	public List<Suggestion> getSuggestions(Vertex root,int x) {
		ArrayList<Suggestion> sg = new ArrayList<Suggestion>();
		List<Suggestion> top = new ArrayList<Suggestion>();
		ArrayList<String> suggested = new ArrayList<String>();
		for(Vertex user: root.getAdj()) {
			for (Vertex restaurant: user.getAdj()) {
				if (!restaurant.equals(root)) {
					
					float grade;
					if (x == 1) {
						grade = (float) findEdge(restaurant, user).foodGrade;
					} else if (x == 2) {
						grade = (float) findEdge(restaurant, user).serviceGrade;
					} else {
						grade = (float) findEdge(restaurant, user).decorGrade;
					}
					
					if ((!suggested.contains(restaurant.getName())) && (grade > 5)){
						sg.add(new Suggestion(restaurant, grade));
						suggested.add(restaurant.getName());
					} else if (grade > 5) {
						for(Suggestion s: sg) {
							if (s.getVertex().equals(restaurant)) {
								s.add(new Float(grade));
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
		if (sg.size() < 11) {
			top = sg.subList(0, sg.size());
		} else {
			top = sg.subList(0, 10);
		}
		return top;
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
	public void toXML() {
		
	}

	static class Edge {
		Vertex v1;
		Vertex v2;
		int foodGrade;
		int serviceGrade;
		int decorGrade;

		public Edge(Vertex newV1, Vertex newV2, int foodGrade, int serviceGrade, int decorGrade) {
			synchronized (vertices) {
				v1 = findVertex(newV1);
				v2 = findVertex(newV2);
			}
			if (v1 == null) {
				v1 = newV1;
				vertices.add(newV1);
			}
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