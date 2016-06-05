package application.view;

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
    private void handleSave() {
//    	System.out.println("Saving vertices and edges to XML files");
    	Graph.getInstance().save();
    }
    
    @FXML
    private void handleLoad() {
    	if (!Graph.getInstance().load()) {
    		Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Files not found");
            alert.setContentText("Please make sure the data files are in the right folder");
            alert.showAndWait();
    	}
    }
    
    @FXML
    private void handleCrawl() {
    	ThreadPool tp = ThreadPool.getInstance();
    	if (!tp.isStopped()) {
    		String link1 = "http://www.iens.nl/restaurant/24339/amsterdam-le-restaurant";
        	String link2 = "http://www.iens.nl/restaurant/44/amsterdam-il-cavallino";
    		
    		tp.enqueue(new Crawler(link1));
    		tp.enqueue(new Crawler(link2));
    	}
    }
    
    @FXML
    private void handleStopCrawl() throws InterruptedException {
    	ThreadPool tp = ThreadPool.getInstance();
    	if (!tp.isStopped()) {
    		tp.stop();
    	}
    }
}
