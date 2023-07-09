package clientComponents.login;

import clientComponents.mainScreen.app.AppController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

public class LoginController {

    private AppController mainAppController;

    private Stage primaryStage;

    private boolean loginSuccessful;
    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorMessageLabel;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        /*HttpClientUtil.setCookieManagerLoggingFacility(line ->
                Platform.runLater(() ->
                        updateHttpStatusLine(line)));*/
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                        errorMessageProperty.set("Something went wrongMM: " + e.getMessage());
                        loginSuccessful = false;
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() -> {
                                errorMessageProperty.set("Something went wrongSS: " + responseBody);
                                loginSuccessful = false;
                        });
                    } else {
                        Platform.runLater(() -> {
                            loginSuccessful = true;

                            //mainAppController.switchToClientApp(mainAppController);
                            //mainAppController.updateUserName(userName);
                            //mainAppController.updatePushTabButtons();
                            //mainAppController.openTabFlowDefinition();

                            changeSceneToMainApp(userName);
                            mainAppController.updateUserName(userName);
                            mainAppController.updatePushTabButtons();
                            mainAppController.openTabFlowDefinition();
                        });
                    }}finally {
                    response.close();
                }
            }
        });
    }

    public void changeSceneToMainApp(String userName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/clientComponents/mainScreen/app/include/app.fxml"));
            ScrollPane scrollPane = fxmlLoader.load();
            AppController appController = fxmlLoader.getController();
            appController.setPrimaryStage(primaryStage);

            mainAppController = appController;

            primaryStage.getScene().setRoot(scrollPane);
        } catch (IOException ignore) {}
    }

    @FXML
    void quitButtonClicked(ActionEvent event) {

    }

    @FXML
    void userNameKeyTyped(KeyEvent event) {

    }

    public void setMainAppController(AppController mainAppController) {
        this.mainAppController = mainAppController;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public AppController getMainAppController() {
        return mainAppController;
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
