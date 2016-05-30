package application.view;

import application.Suggestion;
import application.ThreadPool;
import application.Vertex;

import java.util.ArrayList;
import java.util.List;

import application.Graph;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ApplicationViewController {

//    private Main mainApp;
    private Graph g = Graph.getInstance();
    
    @FXML
    private TextField root;
    @FXML
    private TextArea suggestions;

//	public void setMainApp(Main mainApp) {
//        this.mainApp = mainApp;
//    }
	
	@FXML
	private void foodSug() {
		System.out.println("food");
		ThreadPool.getInstance().stop();
		List<Suggestion> suggestion = new ArrayList<Suggestion>();
		String restaurant = root.getText();
		Vertex root = g.findVertexLink(restaurant);
		if (root == null) {
			showError();
		} else {
			suggestion = g.getSuggestions(root,1);
			printSuggestions(suggestion);
		}
	}
	
	@FXML
	private void serviceSug() {
		System.out.println("service");
		ThreadPool.getInstance().stop();
		List<Suggestion> suggestion = new ArrayList<Suggestion>();
		String restaurant = root.getText();
		Vertex root = g.findVertexLink(restaurant);
		if (root == null) {
			showError();
		} else {
			suggestion = g.getSuggestions(root,2);
			printSuggestions(suggestion);
		}
	}
	
	@FXML
	private void decorSug() {
		System.out.println("decor");
		ThreadPool.getInstance().stop();
		List<Suggestion> suggestion = new ArrayList<Suggestion>();
		String restaurant = root.getText();
		Vertex root = g.findVertexLink(restaurant);
		if (root == null) {
			showError();
		} else {
			suggestion = g.getSuggestions(root,3);
			printSuggestions(suggestion);
		}
	}
	
	private void showError() {
		Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Field");
        alert.setContentText("Restaurant not found");
        alert.showAndWait();
	}
	
	private void printSuggestions(List<Suggestion> list) {
		String toPrint = "";
		int i = 1;
		for (Suggestion s: list){
			toPrint += i + ". " + s.print();
			i++;
		}
		suggestions.setText(toPrint);
	}
}
