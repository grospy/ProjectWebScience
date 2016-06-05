package application;

import java.util.ArrayList;

public class Vertex implements Comparable<Vertex> {
	private String id;
	private String name;
	private String link;
	private boolean restaurant = false;
	private volatile ArrayList<Vertex> adj;

	public Vertex(String id, String name, String link, boolean restaurant) {
		this.id = id;
		this.name = name;
		this.link = link;
		this.restaurant = restaurant;
		this.adj = new ArrayList<>();
	}
	
	public Vertex(String name,String link, boolean restaurant) {
		this.id = extractID(link);
		this.name = name;
		this.link = link;
		this.restaurant = restaurant;
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	

	public boolean getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(boolean restaurant) {
		this.restaurant = restaurant;
	}

	public ArrayList<Vertex> giveAdj() {
		return this.adj;
	}
}
