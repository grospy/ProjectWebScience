package application;

import java.util.ArrayList;

public class Vertex implements Comparable<Vertex> {
	private String id;
	private String name;
	private String link;
	private boolean restaurant = false;
	private ArrayList<Vertex> adj;

	public Vertex(String name,String link, int restaurant) {
		this.id = extractID(link);
		this.name = name;
		this.link = link;
		this.restaurant = true;
		this.adj = new ArrayList<>();
	}
	
	public Vertex(String name, String link) {
		this.id = extractID(link);
		this.name = name;
		this.link = link;
		this.adj = new ArrayList<>();
	}

	private String extractID(String link) {
		String id = link.replaceAll("[^0-9]", "");
		return id;
	}
	
	@Override
	public int compareTo(Vertex other) {
		if (this.id.equals(other.id)) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public void add(Vertex adjacent) {
		adj.add(adjacent);
	}
	
	public String toString() {
		String output = "";
		if (this.restaurant) {
			output += "Restaurant Info:\n";
			output += "ID: "+ this.id + "\n";
			output += "Name: "+ this.name + "\n";
			output += "Link: "+ this.link + "\n";
		} else {
			output += "User Info:\n";
			output += "ID: "+ this.id + "\n";
			output += "Name: "+ this.name + "\n";
			output += "Link: "+ this.link + "\n";
		}
		return output;
	}

	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

}
