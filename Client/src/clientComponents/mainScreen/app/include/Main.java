package clientComponents.mainScreen.app.include;

import clientComponents.mainScreen.app.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        AppController appController = fxmlLoader.getController();
        appController.setPrimaryStage(primaryStage);

        Scene scene  = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

