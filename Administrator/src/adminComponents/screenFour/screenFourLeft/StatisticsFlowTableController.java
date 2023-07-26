package adminComponents.screenFour.screenFourLeft;

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
import adminComponents.screenFour.StatisticsData;
import adminComponents.screenFour.statisticScreen.StatisticMainController;
import utils.DTOStatistics;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsFlowTableController implements Initializable {
    private StatisticMainController mainStatisticController;
    @FXML
    private TableView<StatisticsData> tableFlowStatistic;
    @FXML
    private TableColumn<StatisticsData, String> flowNameColumn;
    @FXML
    private TableColumn<StatisticsData, Integer> executionsColumn;
    @FXML
    private TableColumn<StatisticsData, Double> averageExecutionTimeColumn;
    @FXML
    private Label flowStatisticsTableLabel;
    @FXML
    private BarChart <String, Integer>flowStatChartBar;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table
        flowNameColumn.setCellValueFactory(new PropertyValueFactory<StatisticsData, String>("name"));
        executionsColumn.setCellValueFactory(new PropertyValueFactory<StatisticsData, Integer>("executions"));
        averageExecutionTimeColumn.setCellValueFactory(new PropertyValueFactory<StatisticsData, Double>("averageExecutionTime"));
        setRightToLeftAlignment(flowNameColumn);
        setRightToLeftAlignment(executionsColumn);
        setRightToLeftAlignment(averageExecutionTimeColumn);

        addAnimation();
    }

    private void addAnimation() {
        // Add animation to the table rows
        tableFlowStatistic.setRowFactory(tableView -> {
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
        setDetailsFlowStatistics(statistics);
    }

    public void setDetailsFlowStatistics(DTOStatistics statistics)
    {
        ObservableList<StatisticsData> data = tableFlowStatistic.getItems();
        // Clear the existing items in the table
        data.clear();
        flowStatChartBar.getData().clear();
        for (String flow : statistics.getStatisticsFlows().getExecutions().keySet()) {
            int countFlowExecutions = statistics.getStatisticsFlows().getExecutions().get(flow);
            long flowTotalTime = statistics.getStatisticsFlows().getTotalTimes().getOrDefault(flow, 0L);
            double flowAvgTime = countFlowExecutions > 0 ? (double) flowTotalTime / countFlowExecutions : 0;

            StatisticsData statisticsData = new StatisticsData(flow, countFlowExecutions, flowAvgTime);
            tableFlowStatistic.getItems().add(statisticsData);
        }
        flowStatChartBar.getData().clear();
        flowStatChartBar.setAnimated(true);
        XYChart.Series series = new XYChart.Series();

        for (Map.Entry<String, Integer> stat : statistics.getStatisticsFlows().getExecutions().entrySet()) {
            series.getData().add(new XYChart.Data(stat.getKey(), stat.getValue()));
        }
        flowStatChartBar.getData().add(series);
        flowStatChartBar.setAnimated(false);
    }
    public void clearDetails() {
        ObservableList<StatisticsData> data = tableFlowStatistic.getItems();
        data.clear(); // Clear the items in the tableFlowExecution
    }
}