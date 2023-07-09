package adminComponents.screenOne;

import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.users.User;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOUserDataFullInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

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
}
