package application;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Vertex implements Comparable<Vertex> {
	private String id;
	private String name;
	private String link;
	private boolean restaurant = false;
	private volatile ArrayList<Vertex> adj;

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
	
	public Vertex(String id) throws IOException {
		this.id = id;
		Document doc;
		String source;
		if (id.length() < 6) {
			source = "http://www.iens.nl/restaurant/" + id;
			doc = Jsoup.connect(source).get();
			this.name = doc.select("meta[property=og:title]").attr("content").toString();
			this.link = doc.select("meta[property=og:url]").attr("content").toString();
			this.restaurant = true;
		} else {
			source = "http://www.iens.nl/profiel/" + id;
			doc = Jsoup.connect(source).get();
			this.name = doc.select("span[itemprop=author]").first().text().toString();
			this.link = doc.select("meta[property=og:url]").attr("content").toString();
		}
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
	
	public ArrayList<Vertex> getAdj() {
		return adj;
	}

}
