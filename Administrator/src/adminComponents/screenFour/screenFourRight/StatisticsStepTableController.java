package adminComponents.screenFour.screenFourRight;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Duration;
import stepper.systemEngine.SystemEngineInterface;
import adminComponents.screenFour.StatisticsData;
import adminComponents.screenFour.statisticScreen.StatisticMainController;
import utils.DTOStatistics;


import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsStepTableController implements Initializable {

    private StatisticMainController mainStatisticController;
    @FXML
    private TableView<StatisticsData> tableStepStatistic;

    @FXML
    private TableColumn<StatisticsData, String> StepNameColumn;
    @FXML
    private TableColumn<StatisticsData, Integer> executionsColumn;
    @FXML
    private TableColumn<StatisticsData, Double> averageExecutionTimeColumn;

    @FXML
    private Label stepsStatisticsTableLabel;

    @FXML private BarChart<String, Integer> stepStatisticBarChart;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table
        StepNameColumn.setCellValueFactory(new PropertyValueFactory<StatisticsData, String>("name"));
        executionsColumn.setCellValueFactory(new PropertyValueFactory<StatisticsData, Integer>("executions"));
        averageExecutionTimeColumn.setCellValueFactory(new PropertyValueFactory<StatisticsData, Double>("averageExecutionTime"));
        // Set cell factories for each column to use right-to-left alignment
        setRightToLeftAlignment(StepNameColumn);
        setRightToLeftAlignment(executionsColumn);
        setRightToLeftAlignment(averageExecutionTimeColumn);

        addAnimation();
    }

    private void addAnimation() {
        // Add animation to the table rows
        tableStepStatistic.setRowFactory(tableView -> {
            TableRow<StatisticsData> row = new TableRow<>();
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), row);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), row);
            translateTransition.setFromY(100);
            translateTransition.setToY(0);
            row.setOnMouseClicked(event -> {
                fadeTransition.play();
                translateTransition.play();
            });
            return row;
        });
    }

    private <T> void setRightToLeftAlignment(TableColumn<StatisticsData, T> column) {
        column.setCellFactory(new Callback<TableColumn<StatisticsData, T>, TableCell<StatisticsData, T>>() {
            @Override
            public TableCell<StatisticsData, T> call(TableColumn<StatisticsData, T> param) {
                TableCell<StatisticsData, T> cell = new TableCell<StatisticsData, T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setAlignment(Pos.CENTER);
                        } else {
                            setText(item.toString());
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        });
    }
    public void setMainController(StatisticMainController mainStatisticController) {
        this.mainStatisticController = mainStatisticController;
    }

    public void setTableView(DTOStatistics statistics)
    {
        setDetailsStepStatistics(statistics);
    }

    public void setDetailsStepStatistics(DTOStatistics statistics)
    {
        ObservableList<StatisticsData> data = tableStepStatistic.getItems();

        // Clear the existing items in the table
        data.clear();
        stepStatisticBarChart.getData().clear();
        for (String step : statistics.getStatisticsSteps().getExecutions().keySet()) {
            int countStepExecutions = statistics.getStatisticsSteps().getExecutions().get(step);
            long stepTotalTime = statistics.getStatisticsSteps().getTotalTimes().getOrDefault(step, 0L);
            double stepAvgTime = countStepExecutions > 0 ? (double) stepTotalTime / countStepExecutions : 0;

            StatisticsData statisticsData = new StatisticsData(step, countStepExecutions, stepAvgTime);
            tableStepStatistic.getItems().add(statisticsData);
        }
        stepStatisticBarChart.getData().clear();
        stepStatisticBarChart.setAnimated(true);
        XYChart.Series series = new XYChart.Series();

        for (Map.Entry<String, Integer> stat : statistics.getStatisticsSteps().getExecutions().entrySet()) {
            series.getData().add(new XYChart.Data(stat.getKey(), stat.getValue()));
        }
        stepStatisticBarChart.getData().add(series);
        stepStatisticBarChart.setAnimated(false);
    }


    public void clearDetails() {
        ObservableList<StatisticsData> data = tableStepStatistic.getItems();
        data.clear(); // Clear the items in the tableFlowExecution
    }
}