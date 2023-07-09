package clientComponents.login;

import clientComponents.mainScreen.app.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class MainTestLogin extends Application {

    private AppController mainAppController;
    private LoginController loginController;

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/clientComponents/login/loginComponent.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        // Get the AppController instance from the FXMLLoader
        loginController = fxmlLoader.getController();
        mainAppController = new AppController();
        mainAppController.setPrimaryStage(primaryStage);
        loginController.setMainAppController(mainAppController);
        loginController.setPrimaryStage(primaryStage);

        Scene scene  = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

