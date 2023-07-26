package clientComponents.mainScreen.header;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.users.User;
import util.Constants;
import util.http.HttpClientUtil;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;


public class HeaderDataRefresher extends TimerTask {
    private final Consumer<User> usersDataConsumer;
    private final String userName;

    public HeaderDataRefresher(String userName, Consumer<User> userDataConsumer) {
        //this.shouldUpdate = shouldUpdate;
        this.usersDataConsumer = userDataConsumer;
        this.userName = userName;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.USER_DATA_REFRESHER)
                .newBuilder()
                .addQueryParameter("username", userName)
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

                try {
                    if (response.isSuccessful()) {
                        User userData = new Gson().fromJson(response.body().string(), User.class);
                        usersDataConsumer.accept(userData);
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
