package subComponents.screenFour.statisticScreen;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.mainScreen.body.BodyController;
import subComponents.screenFour.screenFourLeft.StatisticsFlowTableController;
import subComponents.screenFour.screenFourRight.StatisticsStepTableController;
import utils.DTOStatistics;

public class StatisticMainController {

    private BodyController mainBodyController;
    private SystemEngineInterface systemEngine;
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

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
        statisticsStepTableComponentController.setSystemEngine(systemEngine);
        statisticsFlowTableComponentController.setSystemEngine(systemEngine);

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
