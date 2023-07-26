package adminComponents.mainScreen.app.include;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import adminComponents.mainScreen.app.AppController;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("app.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        // Get the AppController instance from the FXMLLoader
        AppController appController = fxmlLoader.getController();

        // Pass the primaryStage to the AppController
        appController.checkIfAdminExists();
        appController.setPrimaryStage(primaryStage);

        Scene scene  = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            try {
                appController.ServerUpdateAdminApplicationHasBeenClosed();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        primaryStage.show();
    }

}

