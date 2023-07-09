package subComponents.mainScreen.header.headerBody;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.mainScreen.app.AppController;
import subComponents.mainScreen.header.HeaderController;

import java.io.File;

public class HeaderBodyController {

    private SystemEngineInterface systemEngine;

    private HeaderController mainHeaderController;
    @FXML
    private Label statusFileXML;
    @FXML
    private Label filePath;
    @FXML
    private Button loadFileButton;
    private SimpleBooleanProperty loadFileButtonProperty;

    private SimpleBooleanProperty isFileCorrectProperty;

    public HeaderBodyController() {
     loadFileButtonProperty = new SimpleBooleanProperty(false);
     isFileCorrectProperty = new SimpleBooleanProperty(false);
    }


    public void setMainController(HeaderController mainController) {
        this.mainHeaderController = mainController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }
    @FXML
    void loadFileButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String absolutePath = selectedFile.getAbsolutePath();
            boolean isXMLFile = absolutePath.toLowerCase().endsWith(".xml");
            loadFileButtonProperty.set(true);
            if(!isXMLFile)
            {
            statusFileXML.setText("Invalid file type. The file must be an XML file.");
            statusFileXML.setVisible(true);
            }else {
                // Check the validity of the file
                boolean isValid = checkFileValidity(selectedFile);

                if (isValid) {
                    filePath.setText(selectedFile.getPath());
                    filePath.setVisible(true);
                    statusFileXML.setText("File is correct");
                    statusFileXML.setVisible(true);
                    isFileCorrectProperty.set(true);
                    mainHeaderController.updatePushTabButtons();
                    mainHeaderController.openTabFlowDefinition();
                    loadFileButtonProperty.set(false);
                }else {
                    filePath.setText(selectedFile.getPath());
                    isFileCorrectProperty.set(false);
                }
            }
        }
    }

    private boolean checkFileValidity(File selectedFile) {
        boolean flag;
        try {
            systemEngine.readingSystemInformationFromFileJavaFX(selectedFile);
            flag =  true;
        }catch (RuntimeException e) {
            statusFileXML.setText(e.getMessage());
            statusFileXML.setVisible(true);
            flag = false;
        }
        return flag;
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
