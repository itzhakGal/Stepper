package subComponents.screenThree.lowerScreen.stepListDetails.outputDetailsList.outputBody;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import stepper.dataDefinition.impl.Enumerator.EnumeratorData;
import stepper.dataDefinition.impl.fileList.FileListData;
import stepper.dataDefinition.impl.list.ListData;
import stepper.dataDefinition.impl.mapping.MappingData;
import stepper.dataDefinition.impl.relation.RelationData;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.lowerScreen.stepListDetails.outputDetailsList.OutputDetailsListController;
import utils.DTOOutPutFlowPast;
import utilsDesktopApp.DTOOutputDetailsJAVAFX;

import java.util.List;
import java.util.Map;

public class OutputBodyController {

    private OutputDetailsListController mainOutputDetailsListController;
    private SystemEngineInterface systemEngine;
    @FXML
    private Label type;
    @FXML
    private AnchorPane loadFxmlOutput;

    private SimpleStringProperty typeProperty;

    public OutputBodyController() {
        typeProperty =  new SimpleStringProperty();
    }

    @FXML
    public void initialize() {
        type.textProperty().bind(typeProperty);
    }

    public void setMainController(OutputDetailsListController mainOutputDetailsListController) {
        this.mainOutputDetailsListController = mainOutputDetailsListController;
    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }

    public void setFlowData(DTOOutPutFlowPast output) {
        typeProperty.set(output.getType());
        //להשלים תוכן
    }

    public void setOutputData(DTOOutputDetailsJAVAFX output) {
        typeProperty.set(output.getType());
        updateInputContent(output);
    }

    private void updateInputContent(DTOOutputDetailsJAVAFX output) {
        if (output.getTypePresentation().equals("FileListData")) {
            FileListData listData = (FileListData) output.getValue();
            createListDataContent(listData.getItem().isEmpty(), listData.toString(), listData.getItem().size());
        }else if (output.getTypePresentation().equals("EnumeratorData")) {
            EnumeratorData enumeratorData = (EnumeratorData) output.getValue();
            createStringContent((String) ((String)((EnumeratorData) output.getValue()).getAllMembers()));
        }
        else if(output.getTypePresentation().equals("ListData"))
        {
            ListData<String> listData = (ListData<String>) output.getValue();
            createListDataContent(listData.getItem().isEmpty(), listData.toString(), listData.getItem().size());
        }
        else if (output.getTypePresentation().equals("MappingData")) {
            VBox vBox = createVBox((MappingData) output.getValue());
            loadFxmlOutput.getChildren().add(vBox);
        }
        else if (output.getTypePresentation().equals("RelationData")) {
            TableView tableView = createTableView((RelationData) output.getValue());
            loadFxmlOutput.getChildren().add(tableView);
        }
        else {
            createStringContent((String) (output.getValue() + ""));
        }
    }
    public VBox createVBox(MappingData<?, ?> mappingData) {
        VBox vbox = new VBox();

        for (Map.Entry<?, ?> entry : mappingData.getPairs().entrySet()) {
            String keyText = "The number of files deleted successfully: " + entry.getKey().toString();
            String valueText = "The amount of files that failed to delete: " + entry.getValue().toString();

            Label keyLabel = new Label(keyText);
            Label valueLabel = new Label(valueText);

            HBox keyHBox = new HBox(keyLabel);
            HBox valueHBox = new HBox(valueLabel);

            vbox.getChildren().addAll(keyHBox, valueHBox);
        }

        return vbox;
    }
    public TableView<List<String>> createTableView(RelationData relationData) {
        List<List<String>> table = relationData.getTable();
        List<String> columnNames = table.get(0);

        TableView<List<String>> tableView = new TableView<>();
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            final int index = columnIndex; // Need final variable for lambda expression
            TableColumn<List<String>, String> column = new TableColumn<>(columnNames.get(columnIndex));
            column.setCellValueFactory(cellData -> {
                List<String> rowData = cellData.getValue();
                if (rowData != null && index < rowData.size()) {
                    return new SimpleStringProperty(rowData.get(index));
                } else {
                    return new SimpleStringProperty("");
                }
            });
            column.setMinWidth(170); // Set the minimum width for each column
            tableView.getColumns().add(column);
        }

        for (int rowIndex = 1; rowIndex < table.size(); rowIndex++) {
            List<String> rowData = table.get(rowIndex);
            tableView.getItems().add(rowData);
        }

        return tableView;
    }

    private void createListDataContent(boolean listData, String listData1, int listData2) {
        if (!listData) {
            ObservableList<String> observableInputs = FXCollections.observableArrayList(listData1);
            ListView<String> listView = new ListView<>();
            listView.setItems(observableInputs);
            listView.setPrefHeight(Math.min(listData2 * 24, 300)); // Set the maximum height to adjust to the number of items
            listView.setMinHeight(100);
            listView.setMinWidth(400);
            loadFxmlOutput.getChildren().add(listView);
        } else {
            createStringContent("There are no members in the list");
        }
    }

    private void createStringContent(String content) {
        Label label = new Label(content);
        label.setPadding(new Insets(10, 0, 0, 0)); // Add 10 pixels of padding from the top

        AnchorPane.setTopAnchor(label, 0.0); // Anchor the label to the top of the AnchorPane
        AnchorPane.setLeftAnchor(label, 0.0); // Align the label to the left of the AnchorPane

        loadFxmlOutput.getChildren().add(label);
    }
}
