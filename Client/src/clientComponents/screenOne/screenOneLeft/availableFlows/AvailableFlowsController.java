package clientComponents.screenOne.screenOneLeft.availableFlows;

import clientComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController;
import com.google.gson.Gson;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenOne.screenOneLeft.availableFlows.flowDefinitionDetails.FlowDefinitionDetailsController;
import util.Constants;
import util.http.HttpClientUtil;
import utilsDesktopApp.DTOFlowDetails;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.REFRESH_RATE;

public class AvailableFlowsController implements Closeable {

    private Timer timer;
    private TimerTask flowDefinitionRefresher;
    private final BooleanProperty autoUpdate;
    private clientComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController mainFlowDefinitionController;
    @FXML
    private Accordion listFlowsName;
    @FXML private HBox detailsComponent;
    @FXML private FlowDefinitionDetailsController detailsComponentController;
    @FXML private Label availableFlowsLabel;
    private SimpleBooleanProperty isAccordionClicked;
    public AvailableFlowsController()
    {
        autoUpdate = new SimpleBooleanProperty();
    }
    @FXML
    public void initialize() {
        if (detailsComponentController != null) {
            detailsComponentController.setMainController(this);
        }
        isAccordionClicked = new SimpleBooleanProperty(false);
    }
    public void initListener()
    {

        listFlowsName.expandedPaneProperty().addListener((observable, oldPane, newPane) -> {
            if (newPane != null) {
                isAccordionClicked.set(true);
            } else {
                isAccordionClicked.set(false);
            }
        });

    }
    private void animateAccordion(TitledPane pane) {
        FadeTransition fadeAnimation = new FadeTransition(Duration.seconds(0.5), pane.getContent());

        if (pane.isExpanded()) {
            fadeAnimation.setFromValue(0.0);
            fadeAnimation.setToValue(1.0);
        } else {
            fadeAnimation.setFromValue(1.0);
            fadeAnimation.setToValue(0.0);
        }

        fadeAnimation.play();
    }
    public void initListFlows() {

        String finalUrl = HttpUrl
                .parse(Constants.LIST_FLOWS_DETAILS)
                .newBuilder()
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
                        DTOListFlowsDetails listFlowsDetails = new Gson().fromJson(response.body().string(), DTOListFlowsDetails.class);
                        Platform.runLater(() -> {
                            initListFlowsFromDTOResponse(listFlowsDetails);
                        });
                    }}finally {
                    response.close();
                }
            }
        });


    }
    public void initListFlowsFromDTOResponse(DTOListFlowsDetails listFlowsDetails) {

        availableFlowsLabel.setVisible(true);

        if (listFlowsDetails != null)
        {
            List<String> paneNames = extractPaneNames(listFlowsName);
            List<String> flowsName =  extractListNamesFromDTO(listFlowsDetails);

            for (DTOFlowDetails flowDetails : listFlowsDetails.getDtoFlowDetailsList()) {

                if(!paneNames.contains(flowDetails.getName()))
                {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flowDefinitionDetails/flowDefinitionDetails.fxml"));
                        HBox contentPane = fxmlLoader.load();

                        FlowDefinitionDetailsController controller = fxmlLoader.getController();
                        controller.setMainController(this);
                        //controller.setSystemEngine(systemEngine);

                        TitledPane titledPane = new TitledPane(flowDetails.getName(), contentPane);
                        titledPane.getProperties().put("controller", controller);

                        controller.setFlowData(flowDetails);

                        listFlowsName.getPanes().add(titledPane);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            List<TitledPane> panesToRemove = new ArrayList<>();

            for (TitledPane pane : listFlowsName.getPanes()) {
                if (!flowsName.contains(pane.getText())) {
                    panesToRemove.add(pane);
                }
            }

            listFlowsName.getPanes().removeAll(panesToRemove);
        }
    }

    private List<String> extractPaneNames(Accordion accordion) {
        List<String> paneNames = new ArrayList<>();

        for (TitledPane pane : accordion.getPanes()) {
            paneNames.add(pane.getText());
        }

        return paneNames;
    }

    private List<String> extractListNamesFromDTO(DTOListFlowsDetails listFlowsDetails) {
        List<String> flowsNames = new ArrayList<>();

        for (DTOFlowDetails flowDetails : listFlowsDetails.getDtoFlowDetailsList())
        {
            flowsNames.add(flowDetails.getName());
        }
        return flowsNames;
    }
    public void setMainController(FlowDefinitionScreenController flowDefinitionController) {
        this.mainFlowDefinitionController = flowDefinitionController;
    }
    public void setFlowSelectedDetails(String flowName)
    {
        mainFlowDefinitionController.setFlowSelectedDetails(flowName);
    }
    public SimpleBooleanProperty isAccordionClickedProperty() {
        return isAccordionClicked;
    }
    private void updateFlowDefinitionListFromDTOResponse(DTOListFlowsDetails listFlowsDetails) {
        Platform.runLater(() -> {
            initListFlowsFromDTOResponse(listFlowsDetails);
        });
    }
    public void startListRefresher() {
        flowDefinitionRefresher = new FlowDefinitionRefresher(
                mainFlowDefinitionController.getMainBodyController().getMainController().currentUserNameProperty().getValue(),
                this::updateFlowDefinitionListFromDTOResponse);
        timer = new Timer();
        timer.schedule(flowDefinitionRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public BooleanProperty autoUpdatesProperty() {
        return autoUpdate;
    }

    @Override
    public void close() {
        listFlowsName.getPanes().clear();
        if (flowDefinitionRefresher != null && timer != null) {
            flowDefinitionRefresher.cancel();
            timer.cancel();
        }
    }
    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }


}
