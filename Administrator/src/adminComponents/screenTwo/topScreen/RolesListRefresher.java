package adminComponents.screenTwo.topScreen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTORole;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;


public class RolesListRefresher extends TimerTask {
    private final Consumer<Map<String, DTORole>> rolesListConsumer;

    public RolesListRefresher(Consumer<Map<String, DTORole>> rolesConsumer) {
        this.rolesListConsumer = rolesConsumer;

    }

    @Override
    public void run() {

            HttpClientUtil.runAsync(Constants.ROLES_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure((e.getMessage()));
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try{

                    String responseData = response.body().string();
                    Map<String, DTORole> roles = new Gson().fromJson(responseData, new TypeToken<Map<String, DTORole>>(){}.getType());
                    rolesListConsumer.accept(roles);

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
