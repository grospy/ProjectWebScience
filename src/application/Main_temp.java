package application;

import java.io.IOException;

public class Main_temp {

	public static void main(String[] args) throws IOException {
//		Graph graph = Graph.getInstance();
		ThreadPool tp = ThreadPool.getInstance();
		
		//Test edge duplicates
//		Vertex v1 = new Vertex("24339");
//		Vertex v2 = new Vertex("418769");
//		graph.addEdge(v1, v2, 10, 10, 10);
//		graph.addEdge(v1, v2, 10, 10, 10);
//		graph.addEdge(v2, v1, 10, 10, 10);
//		System.out.println(graph.edgesToString());
		
		//Test restaurant location
		String startLink = "http://www.iens.nl/restaurant/24339/amsterdam-le-restaurant";
		tp.enqueue(new Crawler(startLink));
		
//		String otherLink = "http://www.iens.nl/restaurant/35452/rotterdam-rosso";
//		tp.enqueue(new Crawler(otherLink));
		
		
//		System.out.println(graph);
//		System.out.println(graph.edgesToString());
	}

}
