package clientComponents.screenOne.screenOneLeft.availableFlows;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;
import utilsDesktopApp.DTOListFlowsDetails;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlowDefinitionRefresher extends TimerTask {

    private final Consumer<DTOListFlowsDetails> listFlowsDetailsConsumer;
    public  String userName;


    public FlowDefinitionRefresher(String userName, Consumer<DTOListFlowsDetails> listFlowsDetails) {
        this.listFlowsDetailsConsumer = listFlowsDetails;
        this.userName = userName;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.FLOW_DEFINITION_REFRESHER)
                .newBuilder()
                .addQueryParameter("userName", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

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
                        listFlowsDetailsConsumer.accept(listFlowsDetails);
                    }
                } finally {
                    response.close();
                }
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


