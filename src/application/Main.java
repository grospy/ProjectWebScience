package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ThreadPool threadPool = ThreadPool.getInstance();
//    private boolean started = false;
    private boolean stopped = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WebCrawler");

        initRootLayout();
        showAppGUI();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(null);
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAppGUI() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(null);
            AnchorPane app = (AnchorPane) loader.load();

            rootLayout.setCenter(app);
            
//            AppLayoutController controller = loader.getController();
//            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

//    public static void main(String[] args) {
//        launch(args);
//    }

	public void startCrawler() {

	}

	public void stopCrawler() {
		if(!stopped) {
			threadPool.stop();
		}
		stopped = true;
//		started = false;
	}
    
}