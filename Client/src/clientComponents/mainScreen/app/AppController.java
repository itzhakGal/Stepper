package clientComponents.mainScreen.app;

import clientComponents.mainScreen.body.BodyController;
import clientComponents.mainScreen.header.HeaderClientController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AppController {

    @FXML private ScrollPane headerComponent;
    @FXML private ScrollPane bodyComponent;
    @FXML private HeaderClientController headerComponentController;
    @FXML private BodyController bodyComponentController;
    @FXML private ScrollPane mainComponent;
    private StringProperty currentUserName;
    private Stage primaryStage;

    @FXML
    public void initialize() {

        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);

            initListener();
        }

        mainComponent.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= 600) {
                mainComponent.setFitToWidth(false);
            } else {
                mainComponent.setFitToWidth(true);
            }
        });

        mainComponent.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= 800) {
                mainComponent.setFitToHeight(false);
            } else {
                mainComponent.setFitToHeight(true);
            }
        });

    }
    public AppController() {
        currentUserName = new SimpleStringProperty("");
    }

    public StringProperty currentUserNameProperty() {
        return currentUserName;
    }

    public void updatePushTabButtons()
    {
        bodyComponentController.updatePushTabButtons();
    }
    public void openTabFlowDefinition() {
        bodyComponentController.openTabFlowDefinition();
    }

    public void initListener() {
        bodyComponentController.initListener();

    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public HeaderClientController getHeaderComponentController() {
        return headerComponentController;
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    public void switchToClientApp(AppController appController) {
        try {
            init(appController);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void init(AppController appController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/clientComponents/mainScreen/app/include/app.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        appController = fxmlLoader.getController();
        appController.setPrimaryStage(primaryStage);
        this.bodyComponentController = appController.bodyComponentController;
        this.headerComponentController = appController.headerComponentController;

        Scene scene  = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        bodyComponentController.initialize();
        headerComponentController.initialize();
    }

    public void refresherDataUser() {
        headerComponentController.refresherDataUser();
    }

}
