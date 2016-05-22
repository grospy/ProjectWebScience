package application;

import java.util.ArrayList;

public class Graph {
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;
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

	public void addEdge(Vertex v1, Vertex v2, float cost) {
		Edge temp = findEdge(v1, v2);
		if (temp == null) {
			Edge e = new Edge(v1, v2, cost);
			edges.add(e);
			System.out.println("Adding edge" + v1.getName() + " - " + v2.getName() );
		}
	}

	private Vertex findVertex(Vertex v) {
		for (Vertex oneVertex : vertices) {
			if (oneVertex.compareTo(v) == 0)
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

	class Edge {
		Vertex v1;
		Vertex v2;
		float cost;

		public Edge(Vertex newV1, Vertex newV2, float cost) {
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
			this.cost = cost;

			v1.add(v2);
			v2.add(v1);
		}

		@Override
		public String toString() {
			return "Edge: " + v1.getName() + " -- " + v2.getName() + " weight = " + cost;
		}
	}
}