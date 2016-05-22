package application;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler implements Runnable {
	
	private static volatile Set<String> crawled = new HashSet<String>();
	private Graph graph = Graph.getInstance();
	private ThreadPool threadPool = ThreadPool.getInstance();
	private String source;
	private Vertex object;
	
	public Crawler(String source) {
		this.source = source;
	}
	
	public Crawler(String source, Vertex userObj) {
		this.source = source;
		this.object = userObj;
	}
	
	public void startPoint(String source) throws IOException {
		
	} 
	
	public void crawlRestaurant(String source) throws IOException {
		Document doc = Jsoup.connect(source).get();
		Elements meta_title = doc.select("meta[property=og:title]");
		Elements meta_url = doc.select("meta[property=og:url]");
		Vertex restaurant = new Vertex(meta_title.attr("content").toString(), meta_url.attr("content").toString(), 0);
		Elements reviews = doc.select("div[itemprop=review]");
		for (Element oneReview: reviews) {
			Elements user = oneReview.select("div[class=memberProfile_name fontMedium]");
			String userLink = "http://www.iens.nl" + user.select("a[href]").first().attr("href").toString();
			String userName = user.select("span").first().text().toString();
			Vertex user_new = new Vertex(userName, userLink);
			Elements allGrades = oneReview.select("ul[class=scoreList small-show]");
			float avGrade = getAverage(allGrades); 
			graph.addEdge(restaurant, user_new, avGrade);
			if (!crawled.contains(user_new.getId())) {
				threadPool.enqueue(new Crawler(userLink, user_new));
			} 
		}
		synchronized (crawled) {
			crawled.add(restaurant.getId());
		}
	}
	
	public void crawlUser(Vertex user) throws IOException {
		Document doc = Jsoup.connect(user.getLink()).get();
		Elements reviews = doc.select("div[itemprop=review]");
		for (Element oneReview: reviews) {
			String restaurantLink = "http://www.iens.nl" + oneReview.select("div.memberProfile_name").select("a:eq(1)").attr("href").toString();
			String restaurantName = oneReview.select("div.memberProfile_name").select("a:eq(1)").first().text();
			Vertex restaurant_new = new Vertex(restaurantName, restaurantLink);
			Elements allGrades = oneReview.select("ul[class=scoreList small-show]");
			float avGrade = getAverage(allGrades);
			graph.addEdge(restaurant_new, user, avGrade);
			if (!crawled.contains(restaurant_new.getId())) {
				threadPool.enqueue(new Crawler(restaurantLink));
			} 
		}
		synchronized (crawled) {
			crawled.add(user.getId());
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

	@Override
	public void run() {
		if (source.contains("restaurant")) {
			try {
				crawlRestaurant(source);
			} catch (IOException e) {
				System.out.println("Error Crawling Restaurant");
			}
		} else {
			try {
				crawlUser(object);
			} catch (IOException e) {
				System.out.println("Error Crawling User Profile");
			}
		}
	}

}
