package application.view;

import java.io.IOException;

import application.Crawler;
import application.Graph;
import application.ThreadPool;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RootLayoutController {
    
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    @FXML
    private void handleAbout() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Information");
    	alert.setHeaderText("About");
    	alert.setContentText("Application created by:\n"
    						+ "Andrei Frunze (551107) & "
    						+ "Shamil Karimli (523001)\n\n"
    						+ "Project WebScience");
    	alert.showAndWait();
    }
    
    @FXML
    private void handleSave() throws IOException {
    	Graph.getInstance().toXML();
    }
    
    @FXML
    private void handleCrawl() {
    	String startLink = "http://www.iens.nl/restaurant/24339/amsterdam-le-restaurant";
		ThreadPool.getInstance().enqueue(new Crawler(startLink));
    }
    
    @FXML
    private void handleStopCrawl() throws InterruptedException {
    	ThreadPool.getInstance().stop();
    	Thread.sleep(5000);
    }
    
    @FXML
    private void handleTests() {
    	System.out.println(Graph.getInstance().toString());
    }
    
}
