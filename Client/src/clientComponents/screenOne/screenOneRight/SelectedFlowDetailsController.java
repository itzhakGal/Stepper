package clientComponents.screenOne.screenOneRight;

import clientComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenOne.screenOneRight.freeInputDetails.FreeInputDetailsController;
import clientComponents.screenOne.screenOneRight.outputDetails.OutputDetailsController;
import clientComponents.screenOne.screenOneRight.stepsList.StepListController;
import util.Constants;
import util.http.HttpClientUtil;
import utils.DTOFlowDefinition;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;

public class SelectedFlowDetailsController {
    private clientComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController mainFlowDefinitionScreenController;
    @FXML
    private GridPane selectedFlowDetailsGridPane;
    @FXML
    private Label flowNameLabel;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label formalOutputLabel;
    @FXML
    private Label readOnlyLabel;
    @FXML
    private HBox listStepAccordionComponent;
    @FXML private StepListController listStepAccordionComponentController;
    @FXML
    private GridPane freeInputGridPaneComponent;
    @FXML
    private FreeInputDetailsController freeInputGridPaneComponentController;
    @FXML
    private GridPane outputGridPaneComponent;
    @FXML
    private OutputDetailsController outputGridPaneComponentController;
    @FXML
    private Button executeFlowButton;
    private SimpleBooleanProperty isExecuteFlowButtonClicked;
    @FXML
    public void executeFlowButtonAction(Event event)
    {
        mainFlowDefinitionScreenController.updateExecuteFlowButton(flowNameLabel.getText());
        mainFlowDefinitionScreenController.openFlowExecutionTab();
        isExecuteFlowButtonClicked.set(true);
        isExecuteFlowButtonClicked.set(false);

    }
    @FXML
    public void initialize() {
        if (listStepAccordionComponentController != null && freeInputGridPaneComponentController != null && outputGridPaneComponentController != null) {
            listStepAccordionComponentController.setMainController(this);
            freeInputGridPaneComponentController.setMainController(this);
            outputGridPaneComponentController.setMainController(this);
        }
    }

    public SelectedFlowDetailsController() {
        isExecuteFlowButtonClicked = new SimpleBooleanProperty(false);
    }
    public void setMainController(FlowDefinitionScreenController mainFlowDefinitionScreenController) {
        this.mainFlowDefinitionScreenController = mainFlowDefinitionScreenController;
    }
    public void setFlowSelectedDetails(String flowName)
    {
        String finalUrl = HttpUrl
                .parse(Constants.INTRODUCING_FLOW_DEFINITION)
                .newBuilder()
                .addQueryParameter("flowName", flowName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        DTOFlowDefinition dtoFlowDefinition = new Gson().fromJson(response.body().string(), DTOFlowDefinition.class);
                        Platform.runLater(() -> {
                            setFlowSelectedDetailsFromDTOResponse(dtoFlowDefinition);
                        });
                    }}finally {
                    response.close();
                }
            }
        });

    }

    public void setFlowSelectedDetailsFromDTOResponse(DTOFlowDefinition dtoFlowDefinition) {
        //DTOFlowDefinition dtoFlowDefinition = systemEngine.introducingFlowDefinitionJavaFX(flowName);
        flowNameLabel.setText(dtoFlowDefinition.getName());
        descriptionTextArea.setText(dtoFlowDefinition.getDescription());
        String stringFormalOutput = String.join(", ", dtoFlowDefinition.getFormalOutputList());
        formalOutputLabel.setText(stringFormalOutput);
        if(dtoFlowDefinition.isReadOnly())
             readOnlyLabel.setText("Yes");
        else
            readOnlyLabel.setText("No");

        listStepAccordionComponentController.setFlowSelectedDetails(dtoFlowDefinition);
        freeInputGridPaneComponentController.setFreeInputOfFlowSelected(dtoFlowDefinition);
        outputGridPaneComponentController.setOutputOfFlowSelected(dtoFlowDefinition);
    }

    public SimpleBooleanProperty getIsExecuteFlowButtonClicked() {
        return isExecuteFlowButtonClicked;
    }

    public void initListener() {

        mainFlowDefinitionScreenController.getAvailableFlowsComponentController().isAccordionClickedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                        // Delete every component here
                        selectedFlowDetailsGridPane.setVisible(false);
                    }
                });
    }

    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }
}