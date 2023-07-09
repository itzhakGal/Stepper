package clientComponents.screenOne.screenOneLeft.availableFlows;

import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
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
    //private final BooleanProperty shouldUpdate;


    public FlowDefinitionRefresher(Consumer<DTOListFlowsDetails> listFlowsDetails) {
        //this.shouldUpdate = shouldUpdate;
        this.listFlowsDetailsConsumer = listFlowsDetails;
    }

    @Override
    public void run() {

        /*if (!shouldUpdate.get()) {
            return;
        }*/

        //Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

        HttpClientUtil.runAsync(Constants.FLOW_DEFINITION_REFRESHER, new Callback() {

            public void onFailure(@NotNull Call call, @NotNull IOException e) {

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
}


