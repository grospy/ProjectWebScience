package application;

import java.io.IOException;

public class Main_temp {

	public static void main(String[] args) throws IOException {
		Graph graph = new Graph();
		
		Crawler c = new Crawler(graph);
//		c.crawlRestaurant("http://www.iens.nl/restaurant/24339/amsterdam-le-restaurant");
		c.crawlUser("http://www.iens.nl/profiel/376478");
		
		System.out.println(graph);
		System.out.println(graph.edgesToString());
	}

}
