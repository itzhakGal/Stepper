package adminComponents.screenFour.statisticScreen;

import adminComponents.mainScreen.body.BodyController;
import adminComponents.screenFour.screenFourLeft.StatisticsFlowTableController;
import adminComponents.screenFour.screenFourRight.StatisticsStepTableController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import utils.DTOStatistics;

public class StatisticMainController {

    private BodyController mainBodyController;
    @FXML
    private ScrollPane statisticsStepTableComponent;
    @FXML
    private StatisticsStepTableController statisticsStepTableComponentController;
    @FXML
    private ScrollPane statisticsFlowTableComponent;
    @FXML
    private StatisticsFlowTableController statisticsFlowTableComponentController;

    @FXML
    private HBox statisticsFullScreenHbox;

    @FXML
    public void initialize() {
        if (statisticsStepTableComponentController != null && statisticsFlowTableComponentController != null) {
            statisticsFlowTableComponentController.setMainController(this);
            statisticsStepTableComponentController.setMainController(this);
        }
    }

    public void setMainController(BodyController mainBodyController) {
        this.mainBodyController = mainBodyController;
    }

    public void setTableView(DTOStatistics statistics)
    {
        statisticsFlowTableComponentController.setTableView(statistics);
        statisticsStepTableComponentController.setTableView(statistics);
    }

    public void updateVisible()
    {
        statisticsFullScreenHbox.setVisible(true);
    }

    public void initListener() {
    }

    public void clearData() {
        statisticsStepTableComponentController.clearDetails();
        statisticsFlowTableComponentController.clearDetails();
    }
}
