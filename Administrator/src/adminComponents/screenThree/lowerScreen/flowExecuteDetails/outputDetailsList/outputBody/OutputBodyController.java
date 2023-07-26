package adminComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList.outputBody;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import stepper.dataDefinition.impl.Enumerator.EnumeratorData;
import stepper.dataDefinition.impl.fileList.FileListData;
import stepper.dataDefinition.impl.list.ListData;
import stepper.dataDefinition.impl.mapping.MappingData;
import stepper.dataDefinition.impl.relation.RelationData;
import stepper.systemEngine.SystemEngineInterface;
import adminComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList.OutputDetailsListController;
import utilWebApp.DTOOutPutFlowPastWeb;
import utils.DTOOutPutFlowPast;
import utilsDesktopApp.DTOOutputDetailsJAVAFX;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OutputBodyController {

    private OutputDetailsListController mainOutputDetailsListController;
    @FXML
    private Label type;
    @FXML
    private AnchorPane outputContentAnchorPane;

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

    public void setFlowData(DTOOutPutFlowPastWeb output) {
        typeProperty.set(output.getType());
        updateOutputContent(output);
    }

    private void updateOutputContent(DTOOutPutFlowPastWeb output) {

        Integer num;

        if (output.getTypePresentation().equals("FileListData")) {

            FileListData listData = new FileListData(((LinkedHashMap<String, ArrayList>)output.getContent()).get("item"));

            String returnVal = getString(output);

            createListDataContent(listData.getItem().isEmpty(), returnVal, listData.getItem().size());
        }else if (output.getTypePresentation().equals("EnumeratorData")) {
            createStringContent(getStringEnumerator(output));
        }
        else if(output.getTypePresentation().equals("ListData"))
        {
            ListData<String> listData = new ListData<String>((ArrayList)(((LinkedHashMap)output.getContent()).get("item")));
            createListDataContent(listData.getItem().isEmpty(), listData.toString(), listData.getItem().size());
        }
        else if (output.getTypePresentation().equals("MappingData")) {
            VBox vBox = createVBox(output.getContent());
            outputContentAnchorPane.getChildren().add(vBox);
        }
        else if (output.getTypePresentation().equals("RelationData")) {
            TableView tableView = createTableView(output.getContent());
            outputContentAnchorPane.getChildren().add(tableView);
        }
        else if(output.getTypePresentation().equals("Integer"))
        {
            num =((Double)output.getContent()).intValue();
            createStringContent(num.toString());

        }else{
            createStringContent((String) (output.getContent() + ""));
        }
    }

    private static String getStringEnumerator(DTOOutPutFlowPastWeb output) {
        ArrayList arrayList =  ((ArrayList)((LinkedHashMap)output.getContent()).get("enumerator"));
        String rerurnVal = "";
        int counter = 1;
        for (int i = 0; i < arrayList.size(); i++) {
            String enumerator = (String) ((ArrayList)((LinkedHashMap)output.getContent()).get("enumerator")).get(i);
            rerurnVal = rerurnVal + enumerator + "\n";
            counter++;
        }
        return rerurnVal;
    }
    private static String getString(DTOOutPutFlowPastWeb input) {
        ArrayList arrayList =  (ArrayList)((LinkedHashMap) input.getContent()).get("item");
        String rerurnVal = "";
        int counter = 1;
        for (int i = 0; i < arrayList.size(); i++) {
            String filePath = (String) ((LinkedHashMap) ((ArrayList)((LinkedHashMap) input.getContent()).get("item")).get(i)).get("fileName");
            rerurnVal = rerurnVal + "Item number: " + counter + ". " + filePath + "\n";
            counter++;
        }
        return rerurnVal;
    }


    public VBox createVBox(Object output) {
        Integer num;
        String cdr;
        String car;
        VBox vbox = new VBox();
        StringBuilder res= new StringBuilder();
        for(Map.Entry<Object,Object> entry :((Map<Object,Object>)((LinkedHashMap)((LinkedHashMap)output).get("pairs"))).entrySet())
        {
            if(entry.getKey() instanceof Double)
            {
                num=((Double)entry.getKey()).intValue();
                car=num.toString();
            }
            else
                car=entry.getKey().toString();

            if(entry.getValue() instanceof Double)
            {
                num=((Double)entry.getValue()).intValue();
                cdr=num.toString();
            }
            else
                cdr=entry.getValue().toString();

            String keyText = "The number of files deleted successfully: " + car;
            String valueText = "The amount of files that failed to delete: " + cdr;

            Label keyLabel = new Label(keyText);
            Label valueLabel = new Label(valueText);

            HBox keyHBox = new HBox(keyLabel);
            HBox valueHBox = new HBox(valueLabel);

            vbox.getChildren().addAll(keyHBox, valueHBox);
        }

        return vbox;
    }
    public TableView<List<String>> createTableView(Object input) {

        List<List<String>> table = ((List<List<String>>)((ArrayList)((LinkedHashMap)input).get("table")));

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
            outputContentAnchorPane.getChildren().add(listView);
        } else {
            createStringContent("There are no members in the list");
        }
    }

    private void createStringContent(String content) {
        TextArea textArea = new TextArea(content);
        textArea.setWrapText(true);
        textArea.setEditable(false);

        AnchorPane.setTopAnchor(textArea, 0.0);
        AnchorPane.setLeftAnchor(textArea, 0.0);
        AnchorPane.setBottomAnchor(textArea, 0.0);
        AnchorPane.setRightAnchor(textArea, 0.0);

        outputContentAnchorPane.getChildren().add(textArea);
    }
}
