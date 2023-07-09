package clientComponents.screenTwo.freeInputs.freeInputDetails.freeInputLoadFile;

import clientComponents.screenTwo.freeInputs.FreeInputsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import stepper.step.api.DataNecessity;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenTwo.freeInputs.freeInputDetails.collectionInputs.CollectionInputs;
import utils.DTOInputExecution;

import javax.swing.*;
import java.io.File;

public class FreeInputFileController implements CollectionInputs {

    private clientComponents.screenTwo.freeInputs.FreeInputsController mainFreeInputsController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private Button loadFileButton;

    @FXML
    private Label statusFileXML;

    @FXML
    private Label filePathLabel;
    @FXML
    private Label inputNameLabel;
    private String name;
    private DataNecessity necessity;
    private final SimpleBooleanProperty isInputFieldEmptyProperty;
    public FreeInputFileController() {
        isInputFieldEmptyProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    void loadFileButtonAction(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int ret = fileChooser.showOpenDialog(null);

        if (ret == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();

            if (selectedFiles != null && selectedFiles.length > 0) {
                StringBuilder pathBuilder = new StringBuilder();
                for (File selectedFile : selectedFiles) {
                    pathBuilder.append(selectedFile.getPath()).append(", ");
                }
                pathBuilder.delete(pathBuilder.length() - 2, pathBuilder.length());  // Remove the trailing comma and space

                filePathLabel.setText(pathBuilder.toString());
                filePathLabel.setVisible(true);
                isInputFieldEmptyProperty.set(true);
            }
        }
    }


    @Override
    public void init(String name, DataNecessity necessity) {
        this.necessity = necessity;
        this.name = name;
    }

    public Label getFieldLabel() {
        return this.inputNameLabel;
    }

    public String getName() {
        return this.name;
    }

    public DataNecessity getNecessity() {
        return this.necessity;
    }

    public SimpleBooleanProperty getIsInputFieldEmptyProperty() {
        return this.isInputFieldEmptyProperty;
    }

    @Override
    public String getInputData() {
        return filePathLabel.getText();
    }

    public void updateDetails(DTOInputExecution inputExecution) {
        inputNameLabel.setText(inputExecution.getFinalName());
    }

    @Override
    public void setFreeInput(Object item) {
        String path = item.toString();
        loadFileButtonActionWithFilePath(path);
    }

    private void loadFileButtonActionWithFilePath(String filePath) {
        filePathLabel.setText(filePath);
        filePathLabel.setVisible(true);
        isInputFieldEmptyProperty.set(true);
    }
    public void setMainController(FreeInputsController mainFreeInputsController) {
        this.mainFreeInputsController = mainFreeInputsController;
    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
    }

}
