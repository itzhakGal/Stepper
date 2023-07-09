package adminComponents.screenOne;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static util.Constants.GSON_INSTANCE;


public class UserListRefresher extends TimerTask {
    private final Consumer<List<String>> usersListConsumer;

    public UserListRefresher(Consumer<List<String>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;

    }

    @Override
    public void run() {

            HttpClientUtil.runAsync(Constants.USERS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try{
                    String jsonArrayOfUsersNames = response.body().string();
                    String[] usersNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                    usersListConsumer.accept(Arrays.asList(usersNames));

                } finally {
                    response.close();
                }
            }
        });
    }
}
