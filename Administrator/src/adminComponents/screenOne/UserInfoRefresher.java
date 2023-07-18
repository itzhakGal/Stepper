package adminComponents.screenOne;

import com.google.gson.Gson;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOUserDataFullInfo;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UserInfoRefresher extends TimerTask {
    private final Consumer<DTOUserDataFullInfo> userDataFullInfo;
    private final String userSelected;

    public UserInfoRefresher(String userSelected , Consumer<DTOUserDataFullInfo> userDataFullInfo) {
        this.userDataFullInfo = userDataFullInfo;
        this.userSelected = userSelected;
    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.USER_DATA_INFO_IN_ADMIN)
                .newBuilder()
                .addQueryParameter("username", userSelected)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try{
                    if (response.isSuccessful()) {
                        DTOUserDataFullInfo dtoUserDataFullInfo = new Gson().fromJson(response.body().string(), DTOUserDataFullInfo.class);
                        userDataFullInfo.accept(dtoUserDataFullInfo);
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
