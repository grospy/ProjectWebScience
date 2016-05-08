package application;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	
	private Set<String> crawled = new HashSet<String>();
	private Graph graph;
	
	public Crawler(Graph graph) {
		this.graph = graph;
	}
	
	public void crawlRestaurant(String source) throws IOException {
		Document doc = Jsoup.connect(source).get();
		Elements meta_title = doc.select("meta[property=og:title]");
		Elements meta_url = doc.select("meta[property=og:url]");
		Vertex restaurant = new Vertex(meta_title.attr("content").toString(), meta_url.attr("content").toString(), 0);
		Elements reviews = doc.select("div[itemprop=review]");
//		ArrayList<Vertex> n = new ArrayList<Vertex>(10);
		for (Element oneReview: reviews) {
			Elements user = oneReview.select("div[class=memberProfile_name fontMedium]");
			String userLink = "http://www.iens.nl" + user.select("a[href]").first().attr("href").toString();
			String userName = user.select("span").first().text().toString();
			Vertex user_new = new Vertex(userName, userLink);
//			n.add(user_new);
			Elements allGrades = oneReview.select("ul[class=scoreList small-show]");
			float avGrade = getAverage(allGrades); 
			graph.addEdge(restaurant, user_new, avGrade);
		}
		crawled.add(restaurant.getId());
	}
	
	public void crawlUser(String link) throws IOException {
//	public void crawlUser(Vertex user) throws IOException {
//		Document doc = Jsoup.connect(user.getLink()).get();
		Document doc = Jsoup.connect(link).get();
		Elements reviews = doc.select("div[itemprop=review]");
		for (Element oneReview: reviews) {
			String restaurantLink = "http://www.iens.nl" + oneReview.select("div.memberProfile_name").select("a:eq(1)").attr("href").toString();
			String restaurantName = oneReview.select("div.memberProfile_name").select("a:eq(1)").first().text();
			Vertex restaurant_new = new Vertex(restaurantName, restaurantLink);
			Elements allGrades = oneReview.select("ul[class=scoreList small-show]");
			float avGrade = getAverage(allGrades);
			graph.addEdge(restaurant_new, new Vertex("MISHA", "link"), avGrade);
		}
		
	}
	
	private float getAverage(Elements allGrades) {
		int grade1 = Integer.parseInt(allGrades.select("li:eq(0)").text().replaceAll("[^0-9]", ""));
		int grade2 = Integer.parseInt(allGrades.select("li:eq(1)").text().replaceAll("[^0-9]", ""));
		int grade3 = Integer.parseInt(allGrades.select("li:eq(2)").text().replaceAll("[^0-9]", ""));
		float av = (grade1 + grade2 + grade3)/ (float) 3;
		float avGrade = (Math.round(av * 100))/ (float) 100;
		return avGrade;
	}

}
