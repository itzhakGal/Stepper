package clientComponents.screenThree.topScreen;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOListFullDetailsPastRunWeb;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UserFlowsExecutionRefresher extends TimerTask {
    private final Consumer<List<DTOFullDetailsPastRunWeb>> dataUserFlowsExecutionConsumer;
    private final String userName;

    public UserFlowsExecutionRefresher(String userName, Consumer<List<DTOFullDetailsPastRunWeb>> dataUserFlowsExecutionConsumer) {
        this.dataUserFlowsExecutionConsumer = dataUserFlowsExecutionConsumer;
        this.userName = userName;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.LIST_FLOWS_EXECUTION_CLIENT)
                .newBuilder()
                .addQueryParameter("userName", userName)
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
                try{
                if (response.isSuccessful()) {
                    DTOListFullDetailsPastRunWeb dtoListFullDetailsPastRun = new Gson().fromJson(response.body().string(), DTOListFullDetailsPastRunWeb.class);
                    List<DTOFullDetailsPastRunWeb> flowsExecutedList = dtoListFullDetailsPastRun.getDtoListFullDetailsPastRun();

                    dataUserFlowsExecutionConsumer.accept(flowsExecutedList);

                }}finally {
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
