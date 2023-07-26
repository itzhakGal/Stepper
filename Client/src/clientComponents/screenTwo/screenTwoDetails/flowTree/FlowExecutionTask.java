package clientComponents.screenTwo.screenTwoDetails.flowTree;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOStepFlowPastWeb;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

public class FlowExecutionTask extends TimerTask {  //extends Task<Boolean>
    private final UIAdapter uiAdapter;
    private final UUID flowId;
    private final SimpleStringProperty currentFlowId;
    private final SimpleBooleanProperty onFinish;
    private SimpleDoubleProperty progress;

    public FlowExecutionTask(UUID flowId, UIAdapter uiAdapter, SimpleStringProperty currentFlowId, SimpleBooleanProperty onFinish) {
        this.flowId = flowId;
        this.uiAdapter = uiAdapter;
        this.onFinish = onFinish;
        this.currentFlowId = currentFlowId;
        this.progress = new SimpleDoubleProperty(0.0);
    }

    @Override
    public void run() {
        if (currentFlowId.getValue().equals(flowId.toString())) {
            String finalUrl = HttpUrl
                    .parse(Constants.FLOW_EXECUTION_TASK)
                    .newBuilder()
                    .addQueryParameter("flowId", this.flowId.toString())
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
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            DTOFullDetailsPastRunWeb executedData = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                            uiAdapter.update(executedData);
                            if (executedData.getFinalResult() != null) {
                                Platform.runLater(() -> {
                                    onFinish.set(true);
                                    double progressValue = calculateProgress(executedData);  // Implement this method
                                    progress.set(progressValue);
                                    cancel();
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    public SimpleDoubleProperty getProgressBar()
    {
        return progress;
    }

    private double calculateProgress(DTOFullDetailsPastRunWeb executedData) {
        List<DTOStepFlowPastWeb> members = executedData.getSteps();

        if (members.isEmpty()) {
            return 0.0; // No progress if there are no members
        }

        int completedCount = 0;

        for (DTOStepFlowPastWeb member : members) {
            if (member.getStepResult() != null) {
                completedCount++;
            }
        }
        double progressValue = (double) completedCount / members.size();
        return progressValue;
    }

    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }
}



