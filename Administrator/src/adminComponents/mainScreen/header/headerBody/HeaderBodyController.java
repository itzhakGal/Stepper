package adminComponents.mainScreen.header.headerBody;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import adminComponents.mainScreen.header.HeaderController;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFileUpload;
import java.io.File;
import java.io.IOException;

public class HeaderBodyController {

    private HeaderController mainHeaderController;
    @FXML
    private Label statusFileXML;
    @FXML
    private Label filePath;
    @FXML
    private Button loadFileButton;
    private SimpleBooleanProperty loadFileButtonProperty;

    private SimpleBooleanProperty isFileCorrectProperty;

    private boolean isFirstFile = true;


    public HeaderBodyController() {
     loadFileButtonProperty = new SimpleBooleanProperty(false);
     isFileCorrectProperty = new SimpleBooleanProperty(false);
    }
    public void setMainController(HeaderController mainController) {
        this.mainHeaderController = mainController;
    }

    @FXML
    void loadFileButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            return;
        }

        String finalUrl;
        if (isFirstFile) {
            finalUrl = HttpUrl
                    .parse(Constants.LOAD_FILE)
                    .newBuilder()
                    .addQueryParameter("isFirstUpload", "true")
                    .build()
                    .toString();
            isFirstFile = false;
        }
        else {
            finalUrl = HttpUrl
                    .parse(Constants.LOAD_FILE)
                    .newBuilder()
                    .addQueryParameter("isFirstUpload", "false")
                    .build()
                    .toString();
        }

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("xmlFile", selectedFile.getName(), RequestBody.create(selectedFile, MediaType.parse("text/plain")))
                        .build();


        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    statusFileXML.setText(e.getMessage());
                    statusFileXML.setVisible(true);
                    filePath.setText("");
                    isFileCorrectProperty.set(false);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    if (response.isSuccessful()) {
                            String res = response.body().string();
                            DTOFileUpload dtoFileUpload = new Gson().fromJson(res, DTOFileUpload.class);
                            Platform.runLater(() -> {
                                String isValid = dtoFileUpload.getIsValid();
                                String errorMessage = dtoFileUpload.getErrorMessage();
                                if (isValid.equals("true")) {
                                    filePath.setText(selectedFile.getPath());
                                    filePath.setVisible(true);
                                    statusFileXML.setText("File is correct");
                                    statusFileXML.setVisible(true);
                                    isFileCorrectProperty.set(true);
                                    mainHeaderController.updatePushTabButtons();
                                    mainHeaderController.openTabUserManager();
                                    mainHeaderController.updateRolesScreenTwo();
                                    loadFileButtonProperty.set(false);
                                    mainHeaderController.getMainController().getBodyComponentController().updateButtons();
                                } else {
                                    filePath.setText(selectedFile.getPath());
                                    isFileCorrectProperty.set(false);
                                    statusFileXML.setText(errorMessage);
                                    statusFileXML.setVisible(true);
                                }

                            });
                        }}finally {
                    response.close();
                }
            }
        });

    }
    public SimpleBooleanProperty getLoadFileButtonProperty() {
        return this.loadFileButtonProperty;
    }

    public SimpleBooleanProperty getIsFileCorrectProperty() {
        return this.isFileCorrectProperty;
    }

    public Button getLoadFileButton() {
        return loadFileButton;
    }
}
