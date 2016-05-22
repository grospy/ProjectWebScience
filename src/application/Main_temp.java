package application;

import java.io.IOException;

public class Main_temp {

	public static void main(String[] args) throws IOException {
		Graph graph = Graph.getInstance();
		ThreadPool tp = ThreadPool.getInstance();
		tp.enqueue(new Crawler("http://www.iens.nl/restaurant/24339/amsterdam-le-restaurant"));
		
		
//		System.out.println(graph);
//		System.out.println(graph.edgesToString());
	}

}
