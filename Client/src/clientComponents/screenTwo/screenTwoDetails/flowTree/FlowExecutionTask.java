package clientComponents.screenTwo.screenTwoDetails.flowTree;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.systemEngine.SystemEngineInterface;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOStepFlowPastWeb;
import utils.DTOFullDetailsPastRun;
import utils.DTOStepFlowPast;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class FlowExecutionTask extends Task<Boolean> {
    //private final SystemEngineInterface systemEngine;
    private final UIAdapter uiAdapter;
    private final UUID flowId;
    private final SimpleStringProperty currentFlowId;
    private final SimpleBooleanProperty onFinish;
    private SimpleDoubleProperty progress;

    public FlowExecutionTask(UUID flowId, UIAdapter uiAdapter, SimpleBooleanProperty onFinish, SimpleStringProperty currentFlowId) {
        this.flowId = flowId;
        this.uiAdapter = uiAdapter;
        this.onFinish = onFinish;
        this.currentFlowId = currentFlowId;
        //this.systemEngine = systemEngineInterface;
        this.progress = new SimpleDoubleProperty(0.0);
    }

    /*@Override
    protected Boolean call() {
        int SLEEP_TIME = 100;
        DTOFullDetailsPastRun executedData = systemEngine.getFlowExecutedDataDTO(this.flowId);
        uiAdapter.update(executedData);

        while (executedData.getFinalResult() == null) {
            executedData = systemEngine.getFlowExecutedDataDTO(this.flowId);
            if (currentFlowId.getValue().equals(flowId.toString())) {
                uiAdapter.update(executedData);
                // Calculate and update the progress based on your logic
                double progressValue = calculateProgress(executedData);  // Implement this method
                progress.set(progressValue);
            }

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ignored) {}
        }
        onFinish.setValue(true);
        return Boolean.TRUE;
    }*/

    @Override
    protected Boolean call() {
        int SLEEP_TIME = 100;
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

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            DTOFullDetailsPastRunWeb executedData = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                            Platform.runLater(() -> {
                                uiAdapter.update(executedData);
                                if (currentFlowId.getValue().equals(flowId.toString())) {
                                    uiAdapter.update(executedData);
                                    // Calculate and update the progress based on your logic
                                    double progressValue = calculateProgress(executedData);  // Implement this method
                                    progress.set(progressValue);
                                }

                                try {
                                    Thread.sleep(SLEEP_TIME);
                                } catch (InterruptedException ignored) {
                                }
                                onFinish.setValue(true);

                            });
                        }}finally {
                        response.close();
                    }
                }
            });
        }
        return Boolean.TRUE;
    }

    /*@Override
    protected Boolean call() {

        String finalUrl = HttpUrl
                .parse(Constants.FLOW_EXECUTION_TASK)
                .newBuilder()
                .addQueryParameter("flowId", this.flowId.toString())
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
                            String responseData = response.body().string();
                            DTOFullDetailsPastRunWeb executedData = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                            Platform.runLater(() -> {
                                uiAdapter.update(executedData);
                                updateMoreData(executedData);
                                onFinish.setValue(true);
                            });
                        }}finally {
                        response.close();
                    }
                }
            });

        return Boolean.TRUE;
    }


    public void updateMoreData(DTOFullDetailsPastRunWeb executedData) {
        int SLEEP_TIME = 100;

        while (executedData.getFinalResult() == null) {
            String finalUrl = HttpUrl
                    .parse(Constants.FLOW_EXECUTION_TASK)
                    .newBuilder()
                    .addQueryParameter("flowId", this.flowId.toString())
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    // Handle failure if needed
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            DTOFullDetailsPastRunWeb executed = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                            Platform.runLater(() -> {
                                if (currentFlowId.getValue().equals(flowId.toString())) {
                                    uiAdapter.update(executed);
                                    // Calculate and update the progress based on your logic
                                    double progressValue = calculateProgress(executed); // Implement this method
                                    progress.set(progressValue);

                                }

                                try {
                                    Thread.sleep(SLEEP_TIME);
                                } catch (InterruptedException ignored) {
                                }
                            });

                            executedData.setFinalResult(executed.getFinalResult());
                        }
                    } finally {
                        response.close();
                    }
                }
            });
        }
    }*/


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


        /*public void updateMoreData(DTOFullDetailsPastRunWeb executedData) {
        int SLEEP_TIME = 100;
        Boolean result = false;

        while (executedData.getFinalResult() == null) {
            String finalUrl = HttpUrl
                    .parse(Constants.COLLECT_MORE_DATA_EXECUTION_TASK)
                    .newBuilder()
                    .addQueryParameter("flowId", this.flowId.toString())
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
                            String responseData = response.body().string();
                            DTOFullDetailsPastRunWeb executed = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                            Platform.runLater(() -> {
                                if (currentFlowId.getValue().equals(flowId.toString())) {
                                    uiAdapter.update(executed);
                                    // Calculate and update the progress based on your logic
                                    double progressValue = calculateProgress(executed);  // Implement this method
                                    progress.set(progressValue);
                                }

                                try {
                                    Thread.sleep(SLEEP_TIME);
                                } catch (InterruptedException ignored) {
                                }
                            });
                            executedData = executed;
                        }
                    } finally {
                        response.close();
                    }

                }
            });
        }
    }*/
}



