package clientComponents.mainScreen.app.include;

import clientComponents.login.LoginController;
import clientComponents.mainScreen.app.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class MainTest extends Application {

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        boolean isLogin = startLogin(primaryStage);

        if(!isLogin)
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("app.fxml");
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(url.openStream());
            // Get the AppController instance from the FXMLLoader
            AppController appController = fxmlLoader.getController();

            // Pass the primaryStage to the AppController
            appController.setPrimaryStage(primaryStage);

            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    private boolean startLogin(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/clientComponents/login/loginComponent.fxml");

        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        LoginController loginController = fxmlLoader.getController();
        loginController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        boolean isLogin = loginController.isLoginSuccessful();
        return isLogin;
    }
}

