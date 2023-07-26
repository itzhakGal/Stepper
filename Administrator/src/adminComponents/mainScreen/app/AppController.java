package adminComponents.mainScreen.app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import adminComponents.mainScreen.body.BodyController;
import adminComponents.mainScreen.header.HeaderController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;
import java.io.IOException;

public class AppController {
    @FXML
    private ScrollPane headerComponent;
    @FXML
    private ScrollPane bodyComponent;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private BodyController bodyComponentController;
    private Stage primaryStage;

    private boolean isFirstAdmin;

    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);

            bodyComponentController.init();
            initListener();
        }
    }

    public void updatePushTabButtons() {
        bodyComponentController.updatePushTabButtons();
    }

    public void openTabUserManager() {
        bodyComponentController.openTabUserManager();
    }

    public void initListener() {
        headerComponentController.getHeaderBodyComponentController().getLoadFileButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        bodyComponentController.initListener();
                    }
                });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public HeaderController getHeaderComponentController() {
        return headerComponentController;
    }

    public void updateRolesScreenTwo() {
        bodyComponentController.updateRolesScreenTwo();
    }

    public BodyController getBodyComponentController() {
        return bodyComponentController;
    }

    public void checkIfAdminExists() {

        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_LOGIN)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure((e.getMessage()));
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String message = response.body().string();
                    if (!response.isSuccessful()) {
                        Platform.runLater(() -> {
                            handleFailureAdminLogin(message);
                            primaryStage.close();
                        });
                    }
                    else{
                        Platform.runLater(() -> {
                                    if (!message.equals("1"))
                                        setFirstAdmin(false);
                                    else
                                        setFirstAdmin(true);
                                    if (!isFirstAdmin) { //זה לא הפעם הראשונה שיש אדמין אז יש נתונים במערכת
                                        headerComponentController.updatePushTabButtons();
                                        headerComponentController.openTabUserManager();
                                        headerComponentController.updateRolesScreenTwo();
                                        headerComponentController.getMainController().getBodyComponentController().updateButtons();
                                    }
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public boolean isFirstAdmin() {
        return isFirstAdmin;
    }
    public void setFirstAdmin(boolean firstAdmin) {
        isFirstAdmin = firstAdmin;
    }
    public void ServerUpdateAdminApplicationHasBeenClosed() throws IOException {

        String finalUrl = HttpUrl
                .parse(Constants.LOG_OUT_ADMIN)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runSync(finalUrl, new Callback() {  //בקשה סינכרונית לא יוצא עד שהיא מתבצעת כראוי
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure((e.getMessage()));
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        Platform.runLater(() -> {
                            primaryStage.close();
                        });
                    }
                    else{
                        String errorMessage = "Something went wrong during admin logout";
                        Platform.runLater(() -> {
                            handleFailureAdminLogin(errorMessage);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public void handleFailureAdminLogin(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }

    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }

}
