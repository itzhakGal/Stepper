package adminComponents.screenTwo.topScreen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import java.util.List;

public class AvailableAndSelectedFlowsController {

    private TopManagementController mainController;
    @FXML
    private ListView<String> sourceListView;
    @FXML
    private ListView<String> targetListView;
    @FXML
    private Button availableOneMoveSelectedButton;
    @FXML
    private Button availableMoveAllSelectedButton;
    @FXML
    private Button selectedOneMoveAvailableButton;
    @FXML
    private Button selectedMoveAllAvailableButton;
    @FXML
    void availableMoveAllSelectedButtonAction(ActionEvent event) {
        // Transfer all items from sourceListView to targetListView
        targetListView.getItems().addAll(sourceListView.getItems());

        mainController.getListFlowsToAddToTheRole().addAll(sourceListView.getItems());
        sourceListView.getItems().clear();

        if(mainController.getListFlowsToRemoveFromTheRole().contains(sourceListView.getItems()))
            mainController.getListFlowsToRemoveFromTheRole().remove(sourceListView.getItems());
    }

    @FXML
    void availableOneMoveSelectedButtonAction(ActionEvent event) {
        // Move the selected item from sourceListView to targetListView
        String selectedItem = sourceListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            targetListView.getItems().add(selectedItem);
            sourceListView.getItems().remove(selectedItem);
        }
        sourceListView.getSelectionModel().clearSelection();
        mainController.getListFlowsToAddToTheRole().add(selectedItem);


        //אם הוא נמצא בליסט של המחיקה תוריד משם
        if(mainController.getListFlowsToRemoveFromTheRole().contains(selectedItem))
            mainController.getListFlowsToRemoveFromTheRole().remove(selectedItem);
    }
    @FXML
    void selectedMoveAllAvailableButtonAction(ActionEvent event) {
        // Transfer all items from targetListView to sourceListView
        sourceListView.getItems().addAll(targetListView.getItems());

        mainController.getListFlowsToRemoveFromTheRole().addAll(targetListView.getItems());
        targetListView.getItems().clear();

        if(mainController.getListFlowsToAddToTheRole().contains(targetListView.getItems()))
            mainController.getListFlowsToAddToTheRole().remove(targetListView.getItems());
    }
    @FXML
    void selectedOneMoveAvailableButtonAction(ActionEvent event) {
        // Move the selected item from targetListView to sourceListView
        String selectedItem = targetListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            sourceListView.getItems().add(selectedItem);
            targetListView.getItems().remove(selectedItem);
        }
        targetListView.getSelectionModel().clearSelection();
        mainController.getListFlowsToRemoveFromTheRole().add(selectedItem);

        //אם הוא כבר קיים בלהוסיף צריך להסיר אותו משם
        if(mainController.getListFlowsToAddToTheRole().contains(selectedItem))
            mainController.getListFlowsToAddToTheRole().remove(selectedItem);
    }

    public void initListener() {
        // Disable availableOneMoveSelectedButton when no item is selected in sourceListView.
        MultipleSelectionModel<String> sourceSelectionModel = sourceListView.getSelectionModel();
        sourceSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                availableOneMoveSelectedButton.setDisable(newValue == null);
            }
        });

        // Disable selectedOneMoveAvailableButton when no item is selected in targetListView.
        MultipleSelectionModel<String> targetSelectionModel = targetListView.getSelectionModel();
        targetSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedOneMoveAvailableButton.setDisable(newValue == null);
            }
        });

    }

    public void insertItemsIntoSourceListView(List<String> selectedAssignedRoles, List<String> listOfRoles) {

        sourceListView.getItems().clear();
        targetListView.getItems().clear();

        mainController.getListFlowsToAddToTheRole().clear();
        mainController.getListFlowsToRemoveFromTheRole().clear();

        sourceListView.getItems().addAll(selectedAssignedRoles);
        targetListView.getItems().addAll(listOfRoles);
    }
    public void setMainController(TopManagementController topManagementController) {
        this.mainController = topManagementController;
    }

    public ListView<String> getSourceListView() {
        return sourceListView;
    }

    public ListView<String> getTargetListView() {
        return targetListView;
    }
}