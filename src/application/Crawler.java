package application;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

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
	
	public void crawlRestaurant(String source) throws IOException, InterruptedException {
		
		Document doc = Jsoup.connect(source).get();
		String meta_title = doc.select("meta[property=og:title]").attr("content").toString();
		String meta_url = doc.select("meta[property=og:url]").attr("content").toString();
		Vertex restaurant = new Vertex(meta_title, meta_url, 0);
		if (doc.select("span[itemprop=addressLocality]").first().text().toString().equals("Amsterdam")) {
			for (int i = 1; i<6; i++) {
//				String pageLink = source + "#recensies-restaurantNav:perPagina=10&pagina=" + i + "&";
//				
//				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
//			    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
//				@SuppressWarnings("resource")
//				final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
//				webClient.getOptions().setCssEnabled(false);
//				webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//				webClient.waitForBackgroundJavaScript(1000);
//				final HtmlPage page = webClient.getPage(pageLink);
//				webClient.waitForBackgroundJavaScriptStartingBefore(1000);
				
		        try {
//		            doc = Jsoup.parse(page.asXml());
		            
		         // Selects all review blocks
					Elements reviews = doc.select("div[itemprop=review]");
					
					//Iterates through the review blocks
					for (Element oneReview: reviews) {
						Elements user = oneReview.select("div[class=memberProfile_name fontMedium]");
						try {
							String userLink = "http://www.iens.nl" + user.select("a[href]").first().attr("href").toString();
							String userName = user.select("span").first().text().toString();
							Vertex user_new = new Vertex(userName, userLink);
							
							// Selects the grades from a list
							Elements allGrades = oneReview.select("ul[class=scoreList small-show]");
							int food = Integer.parseInt(allGrades.select("li:eq(0)").text().replaceAll("[^0-9]", ""));
							int service = Integer.parseInt(allGrades.select("li:eq(1)").text().replaceAll("[^0-9]", ""));
							int decor = Integer.parseInt(allGrades.select("li:eq(2)").text().replaceAll("[^0-9]", ""));

							//Creating an Edge
							graph.addEdge(restaurant, user_new, food, service, decor);
	
							//Add user to the crawling queue
							if (!crawled.contains(user_new.getId())) {
								threadPool.enqueue(new Crawler(userLink, user_new));
							}
							
						} catch(Exception e) {
							System.out.println("Ignoring review: Error with a review for " + restaurant.getName());
						} 
					}
					
					synchronized (crawled) {
						crawled.add(restaurant.getId());
					}
		            
		            
		        } catch (Exception e) {
		             e.printStackTrace();
		        }
			}
		}
	}
	
	public void crawlUser(Vertex user) throws IOException {
		
		Document doc = Jsoup.connect(user.getLink()).get();
		
		// Selects all review blocks
		Elements reviews = doc.select("div[itemprop=review]");
		
		//Iterates through the review blocks
		for (Element oneReview: reviews) {
			String restaurantLink = "http://www.iens.nl" + oneReview.select("div.memberProfile_name").select("a:eq(1)").attr("href").toString();
			Document localDoc = Jsoup.connect(restaurantLink).get();
			
			try {
				if (localDoc.select("span[itemprop=addressLocality]").first().text().toString().equals("Amsterdam")) {
					String restaurantName = oneReview.select("div.memberProfile_name").select("a:eq(1)").first().text();
					Vertex restaurant_new = new Vertex(restaurantName, restaurantLink);
					// Selects the grades from a list
					Elements allGrades = oneReview.select("ul[class=scoreList small-show]");
					try {
						int food = Integer.parseInt(allGrades.select("li:eq(0)").text().replaceAll("[^0-9]", ""));
						int service = Integer.parseInt(allGrades.select("li:eq(1)").text().replaceAll("[^0-9]", ""));
						int decor = Integer.parseInt(allGrades.select("li:eq(2)").text().replaceAll("[^0-9]", ""));
						//Creates an Edge
						graph.addEdge(user, restaurant_new, food, service, decor);
					} catch (Exception e){
						System.out.println("Review without grade or not approved yet");
					}
					if (!crawled.contains(restaurant_new.getId())) {
						threadPool.enqueue(new Crawler(restaurantLink));
					}
				}
			} catch (Exception e) {
				System.out.println("Restaurant Location Undetermined");
			}
		}
		synchronized (crawled) {
			crawled.add(user.getId());
		}
	}

	@Override
	public void run() {
		if (source.contains("restaurant")) {
			try {
				crawlRestaurant(source);
			} catch (IOException | InterruptedException e) {
				System.out.println("Error Crawling Restaurant at:" + source);
			}
		} else {
			try {
				crawlUser(object);
			} catch (IOException e) {
				System.out.println("Error Crawling User Profile at:" + source);
			}
		}
	}

}
