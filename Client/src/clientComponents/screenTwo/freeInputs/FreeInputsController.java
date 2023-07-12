package clientComponents.screenTwo.freeInputs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import stepper.dataDefinition.impl.number.IntWrapper;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.DataNecessity;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenTwo.FlowsExecutionScreenController;
import clientComponents.screenTwo.freeInputs.freeInputDetails.collectionInputs.CollectionInputs;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.*;
import utils.*;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FreeInputsController {

    private FlowsExecutionScreenController mainFlowsExecutionScreenController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private GridPane freeInputsGridPane;
    @FXML
    private Button runButton;
    private final SimpleBooleanProperty startButtonClickProperty = new SimpleBooleanProperty(false);
    private UUID flowIdRerun;
    private UUID currFlowId;


    @FXML
    public void initialize() {

    }

    public void setMainController(FlowsExecutionScreenController mainFlowsExecutionScreenController) {
        this.mainFlowsExecutionScreenController = mainFlowsExecutionScreenController;
    }

    public void setSystemEngine(SystemEngineInterface engineManager) {
        //this.systemEngine = engineManager;
    }

    private IntWrapper createTitleFreeInputs(String text, String value, IntWrapper row) {
        Label freeInputsLabel = new Label(text);
        freeInputsGridPane.setMargin(freeInputsLabel, new javafx.geometry.Insets(10, 10, 10, 10));
        freeInputsLabel.setStyle("-fx-font-family: Georgia; -fx-text-fill: #5c88be;" + value);
        freeInputsGridPane.addRow(row.getValue(), freeInputsLabel);
        //row++;
        row.setValue(row.getValue() + 1);
        return row;
    }

    public void updateDetailsFreeInputs(DTOFlowExecution dtoFlowExecution, boolean isContinuation)
    {
        IntWrapper row = new IntWrapper(0);;
        IntWrapper amountOfMandatoryInputs = new IntWrapper(0);;
        IntWrapper amountOfOptionalInput = new IntWrapper(0);
        IntWrapper countAmountUpdateData = new IntWrapper(0);

        startButtonClickProperty.set(false);
        mainFlowsExecutionScreenController.getProgressBar().setVisible(false);
        freeInputsGridPane.getChildren().clear();
        freeInputsGridPane.getRowConstraints().clear();
        freeInputsGridPane.getColumnConstraints().clear();

        String finalUrl;
        String strContinuation;

        if(isContinuation)
             strContinuation = "true";
        else
            strContinuation = "false";

        finalUrl = HttpUrl
                .parse(Constants.DETAILS_FREE_INPUTS)
                .newBuilder()
                .addQueryParameter("flowName", dtoFlowExecution.getFlowName())
                .addQueryParameter("strContinuation", strContinuation)
                .build()
                .toString();

        String dtoFlowExecutionJson = new Gson().toJson(dtoFlowExecution);
        RequestBody body = RequestBody.create(dtoFlowExecutionJson.getBytes());
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback(){

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        DTOFlowExecution dtoFlowExecutionNEW = new Gson().fromJson(response.body().string(), DTOFlowExecution.class);
                        flowIdRerun = dtoFlowExecutionNEW.getFlowIdRerun();
                        Platform.runLater(() -> {
                            updateDetailsFreeInputsWab(dtoFlowExecutionNEW, row, amountOfMandatoryInputs,amountOfOptionalInput,countAmountUpdateData, flowIdRerun);
                        });
                    }} finally {
                    response.close();
                }
            }
        });

        //this.flowIdRerun = systemEngine.updateOptionalExecution(dtoFlowExecution.getFlowName(), isContinuation);
        //systemEngine.removeInitialFreeInputFromDTO(dtoFlowExecution);
    }
    private void updateDetailsFreeInputsWab(DTOFlowExecution dtoFlowExecution, IntWrapper row, IntWrapper amountOfMandatoryInputs, IntWrapper amountOfOptionalInput, IntWrapper countAmountUpdateData, UUID flowIdRerun){

        mainFlowsExecutionScreenController.setExecutedFlowID(flowIdRerun);
        row = createTitleFreeInputs("Free Inputs:", "-fx-font-weight: bold; -fx-font-size: 16", row);
        row = createTitleFreeInputs("Mandatory Inputs:", "-fx-font-weight: bold; -fx-font-size: 14", row);
        //systemEngine.removeInitialFreeInputFromDTO(dtoFlowExecution);

        for(DTOInputExecution inputExecution : dtoFlowExecution.getInputsExecution())
        {
            if(inputExecution.getNecessity() == DataNecessity.MANDATORY) {
                row = getRow(row, inputExecution, dtoFlowExecution.getFlowName(), countAmountUpdateData);

                //amountOfMandatoryInputs++;
                amountOfMandatoryInputs.setValue(amountOfMandatoryInputs.getValue() + 1);
            }
            else {
                //amountOfOptionalInput++;
                amountOfOptionalInput.setValue(amountOfOptionalInput.getValue() + 1);
            }
        }
        if(amountOfOptionalInput.getValue() > 0) {
            row = createTitleFreeInputs("Optional Inputs:", "-fx-font-weight: bold; -fx-font-size: 14", row);
            for (DTOInputExecution inputExecution : dtoFlowExecution.getInputsExecution()) {
                if (inputExecution.getNecessity() == DataNecessity.OPTIONAL) {
                    row = getRow(row, inputExecution, dtoFlowExecution.getFlowName(), countAmountUpdateData);
                }
            }
        }
        updateVisibleButtonRun(amountOfMandatoryInputs, dtoFlowExecution, countAmountUpdateData);
        this.flowIdRerun = flowIdRerun;
    }
    private IntWrapper getRow(IntWrapper row, DTOInputExecution inputExecution, String flowName, IntWrapper countAmountUpdateData) {

        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter())
                .create();


        try {
            FXMLLoader fxmlLoader;
            GridPane content;

            if (inputExecution.getType().equals("EnumeratorData"))
                fxmlLoader = new FXMLLoader(getClass().getResource("freeInputDetails/freeInputEnumerator/freeInputEnumerator.fxml"));
            else if (inputExecution.getType().equals("Integer"))
                fxmlLoader = new FXMLLoader(getClass().getResource("freeInputDetails/freeInputNumber/freeInputNumber.fxml"));
            else
            {
                if (inputExecution.isFile())
                    fxmlLoader = new FXMLLoader(getClass().getResource("freeInputDetails/freeInputLoadFile/freeInputFile.fxml"));
                else
                    fxmlLoader = new FXMLLoader(getClass().getResource("freeInputDetails/freeInputString/freeInputDetails.fxml"));
            }

            content = fxmlLoader.load();
            CollectionInputs controller = fxmlLoader.getController();
            controller.init(inputExecution.getFinalName(), inputExecution.getNecessity());
            content.getProperties().put("controller", controller);
            controller.updateDetails(inputExecution);
            controller.setMainController(this);

            //controller.setSystemEngine(systemEngine);

            /*DataInFlowExecution freeInputDataInFlow = systemEngine.findValueOfFreeInput(flowName,inputExecution.getFinalName());
            if(freeInputDataInFlow.getItem() != null)
            {
                controller.setFreeInput(freeInputDataInFlow.getItem());
                countAmountUpdateData.setValue(countAmountUpdateData.getValue() + 1);
            }*/


            String finalUrl = HttpUrl
                    .parse(Constants.FIND_VALUE_OF_FREE_INPUT)
                    .newBuilder()
                    .addQueryParameter("flowName", flowName)
                    .addQueryParameter("finalInputName", inputExecution.getFinalName())
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        createTitleFreeInputs("getRow", "-fx-font-weight: bold; -fx-font-size: 12", row);
                    });

                }
                // יום רביעי עם חריגות
                /*@Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string(); // Read the response body once and store it in a variable

                            Platform.runLater(() -> {
                                //DTODataInFlowExecution freeInputDataInFlow = new Gson().fromJson(responseBody, DTODataInFlowExecution.class);
                                DataInFlowExecution freeInputDataInFlow = gson.fromJson(responseBody, DataInFlowExecution.class);

                                if (freeInputDataInFlow.getItem() != null) {
                                    controller.setFreeInput(freeInputDataInFlow.getItem());
                                    countAmountUpdateData.setValue(countAmountUpdateData.getValue() + 1);
                                }

                                //freeInputsGridPane.addRow(row.getValue(), content);
                                //row.setValue(row.getValue() + 1);
                            });
                        }}finally {
                        response.close();
                    }
                }*/

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                    if (response.isSuccessful()) {
                        //DataInFlowExecution freeInputDataInFlow = new Gson().fromJson(response.body().string(), DataInFlowExecution.class);
                        String res = response.body().string();
                        DTODataInFlowExecution freeInputDataInFlow = gson.fromJson(res, DTODataInFlowExecution.class);

                        Platform.runLater(() -> {
                            if(freeInputDataInFlow.getItem() != null)
                            {
                                controller.setFreeInput(freeInputDataInFlow.getItem());
                                countAmountUpdateData.setValue(countAmountUpdateData.getValue() + 1);
                            }

                        });
                    }}finally {
                        response.close();
                    }
                }
            });
            freeInputsGridPane.addRow(row.getValue(), content);
            row.setValue(row.getValue() + 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return row;
    }

    private void updateVisibleButtonRun(IntWrapper amountOfMandatoryInputs, DTOFlowExecution dtoFlowExecution, IntWrapper countAmountUpdateData) {
        AtomicInteger mandatoryInputs = new AtomicInteger(amountOfMandatoryInputs.getValue() - countAmountUpdateData.getValue());
        runButton.setOnAction(event -> handleButtonClick(amountOfMandatoryInputs.getValue(), dtoFlowExecution));
        if (mandatoryInputs.get() > 0)
            runButton.setDisable(true);
       if (mandatoryInputs.get() == 0) {
            runButton.setDisable(false);
            mainFlowsExecutionScreenController.getRerunButton().setDisable(true);
        }

        for (Node node : freeInputsGridPane.getChildren()) {
            if (node instanceof GridPane) {
                GridPane childGridPane = (GridPane) node;
                Integer rowIndex = GridPane.getRowIndex(childGridPane);
                if (rowIndex != null && rowIndex >= 2 && rowIndex < (2 + amountOfMandatoryInputs.getValue())) {
                    CollectionInputs controller = (CollectionInputs) childGridPane.getProperties().get("controller");
                    if (controller.getNecessity() == DataNecessity.MANDATORY) {
                        SimpleBooleanProperty isInputFieldEmpty = controller.getIsInputFieldEmptyProperty();
                        isInputFieldEmpty.addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                mandatoryInputs.getAndDecrement();
                                if (mandatoryInputs.get() == 0) {
                                    runButton.setDisable(false);
                                    mainFlowsExecutionScreenController.getRerunButton().setDisable(true);
                                }
                            }
                            else {
                                mandatoryInputs.getAndIncrement();
                                runButton.setDisable(true);
                                mainFlowsExecutionScreenController.getRerunButton().setDisable(false);
                            }
                        });
                    }
                }
            }
        }
    }

    private void handleButtonClick1(int amountOfMandatoryInputs, DTOFlowExecution dtoFlowExecution) {
        CollectionInputs controller;

        for (IntWrapper row = new IntWrapper(2); row.getValue() < (dtoFlowExecution.getInputsExecution().size()+3); row.setValue(row.getValue() + 1)) {
            if(row.getValue() != (2+amountOfMandatoryInputs)) {
                GridPane childGridPane = (GridPane) freeInputsGridPane.getChildren().get(row.getValue());
                controller = (CollectionInputs) childGridPane.getProperties().get("controller");
                String labelValue = controller.getName();
                String value = controller.getInputData();
                String type= dtoFlowExecution.getInputByFinalName(labelValue).getType();
                if(!value.isEmpty()) {

                    String finalUrl = HttpUrl
                            .parse(Constants.UPDATE_MANDATORY_INPUT)
                            .newBuilder()
                            .addQueryParameter("type", type)
                            .addQueryParameter("value", value)
                            .addQueryParameter("labelValue", labelValue)
                            .build()
                            .toString();

                    HttpClientUtil.runAsync(finalUrl, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Platform.runLater(() -> {
                                    createTitleFreeInputs("The input value is not an int", "-fx-font-weight: bold; -fx-font-size: 12", row);
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            try {
                                if (response.isSuccessful()) {
                                    Platform.runLater(() -> {
                                        //executionFlow(dtoFlowExecution);
                                        mainFlowsExecutionScreenController.setVisibleDetails(true);
                                        mainFlowsExecutionScreenController.getRerunButtonProperty().set(false);
                                        mainFlowsExecutionScreenController.getProgressBar().setVisible(true);
                                    });
                                }} finally {
                                response.close();
                            }

                        }
                    });

                    /*if (type.equals("Integer")) {
                        try {
                            // Try to parse the value as an integer
                            int intValue = Integer.parseInt(value);
                            //systemEngine.updateMandatoryInput(labelValue, intValue);
                        } catch (NumberFormatException e) {
                            // Throw a NumberFormatException with an appropriate error message if the value is not an integer
                             createTitleFreeInputs("The input value is not an int", "-fx-font-weight: bold; -fx-font-size: 12", row);
                           // throw new NumberFormatException("The input value is not an int.");
                        }
                    } else  if (type.equals("EnumeratorData"))
                    {
                        //systemEngine.updateMandatoryInputEnumerator(labelValue, value);
                    }else {
                        //systemEngine.updateMandatoryInput(labelValue, value);
                    }*/
                }
            }
        }
        executionFlow(dtoFlowExecution);
    }

    private void handleButtonClick(int amountOfMandatoryInputs, DTOFlowExecution dtoFlowExecution) {
        CollectionInputs controller;
        Gson gson = new Gson();

        List<DTOMandatoryInputsWeb> listDtoMandatoryInput = new ArrayList<>();
        for (int row = 2; row < (dtoFlowExecution.getInputsExecution().size()+3); row++) {
            if(row != (2+amountOfMandatoryInputs)) {
                GridPane childGridPane = (GridPane) freeInputsGridPane.getChildren().get(row);
                controller = (CollectionInputs) childGridPane.getProperties().get("controller");
                String labelValue = controller.getName();
                String value = controller.getInputData();
                String type= dtoFlowExecution.getInputByFinalName(labelValue).getType();
                listDtoMandatoryInput.add(new DTOMandatoryInputsWeb(type, labelValue, value));
            }
        }
        ListDTOMandatoryInputsWeb listDTOMandatoryInputsWeb = new ListDTOMandatoryInputsWeb(listDtoMandatoryInput);


        String finalUrl = HttpUrl
                .parse(Constants.TEST)
                .newBuilder()
                .build()
                .toString();

        String listDTOMandatoryInputsWebJSON = gson.toJson(listDTOMandatoryInputsWeb);
        RequestBody body = RequestBody.create(listDTOMandatoryInputsWebJSON.getBytes());
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {


            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        Platform.runLater(() -> {
                            executionFlow(dtoFlowExecution);
                            mainFlowsExecutionScreenController.setVisibleDetails(true);
                            mainFlowsExecutionScreenController.getRerunButtonProperty().set(false);
                            mainFlowsExecutionScreenController.getProgressBar().setVisible(true);
                        });
                    }} finally {
                    response.close();
                }
            }
        });

    }
    private void executionFlow(DTOFlowExecution dtoFlowExecution) {

        String finalUrl = HttpUrl
                .parse(Constants.FLOW_EXECUTION)
                .newBuilder()
                .addQueryParameter("flowName", dtoFlowExecution.getFlowName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                if (response.isSuccessful()) {
                    DTOFullDetailsPastRunWeb fullDetails = new Gson().fromJson(response.body().string(), DTOFullDetailsPastRunWeb.class);
                    Platform.runLater(() -> {
                        currFlowId = fullDetails.getUniqueId();
                        runButton.setDisable(true);
                        mainFlowsExecutionScreenController.updateDetailsFlowRun(fullDetails);
                        startButtonClickProperty.set(true);
                    });
                }}finally {
                    response.close();
                }
            }

        });


        /*DTOFullDetailsPastRun fullDetails = systemEngine.flowActivationAndExecutionJavaFX(dtoFlowExecution.getFlowName());
        currFlowId = fullDetails.getUniqueId();
        runButton.setDisable(true);
        mainFlowsExecutionScreenController.updateDetailsFlowRun(fullDetails);
        startButtonClickProperty.set(true);*/
    }
    public SimpleBooleanProperty getStartButtonClickProperty() {
        return this.startButtonClickProperty;
    }

    public void clearDetails() {
        startButtonClickProperty.set(false);
        freeInputsGridPane.getChildren().clear();
        freeInputsGridPane.getRowConstraints().clear();
        freeInputsGridPane.getColumnConstraints().clear();
    }

    public void updateFreeInputForRerunExecution(List<DTOInputFlowPastWeb> inputs, String flowName) {

        String finalUrl = HttpUrl
                .parse(Constants.RERUN_EXECUTION)
                .newBuilder()
                .addQueryParameter("flowName", flowName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        DTOFlowExecution dtoFlowExecution = new Gson().fromJson(response.body().string(), DTOFlowExecution.class);
                        Platform.runLater(() -> {
                            int amountOfMandatoryInputs = dtoFlowExecution.getAmountOfMandatoryInputs();
                            for (Node node : freeInputsGridPane.getChildren()) {
                                if (node instanceof GridPane) {
                                    GridPane childGridPane = (GridPane) node;
                                    CollectionInputs controller = (CollectionInputs) childGridPane.getProperties().get("controller");
                                    DTOInputFlowPastWeb inputFlowPast = findDTOInputFlowPastByName(controller.getName(), inputs);
                                    if (inputFlowPast.getValue() != null)
                                        controller.setFreeInput(inputFlowPast.getValue());
                                }
                            }
                        });
                    }}finally {
                    response.close();
                }
            }
        });


        /*DTOFlowExecution dtoFlowExecution = systemEngine.readInputsJavaFX(flowName);
        int amountOfMandatoryInputs = dtoFlowExecution.getAmountOfMandatoryInputs();
        for (Node node : freeInputsGridPane.getChildren()) {
            if (node instanceof GridPane) {
                GridPane childGridPane = (GridPane) node;
                CollectionInputs controller = (CollectionInputs) childGridPane.getProperties().get("controller");
                DTOInputFlowPast inputFlowPast = findDTOInputFlowPastByName(controller.getName(), inputs);
                if (inputFlowPast.getValue() != null)
                    controller.setFreeInput(inputFlowPast.getValue());
            }
        }*/
    }

    private DTOInputFlowPastWeb findDTOInputFlowPastByName(String name, List<DTOInputFlowPastWeb> inputs) {
        for(DTOInputFlowPastWeb dtoInputFlowPast : inputs)
        {
            if(name.equals(dtoInputFlowPast.getFinalName()))
                return dtoInputFlowPast;
        }
        return null;
    }

    public UUID getFlowIdRerun() {
        return flowIdRerun;
    }

    public UUID getCurrFlowId() {
        return currFlowId;
    }

}
