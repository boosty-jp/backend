package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.application.converter.user.UserInputConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import co.jp.wever.graphql.domain.service.user.CreateUserService;
import co.jp.wever.graphql.domain.service.user.DeleteUserService;
import co.jp.wever.graphql.domain.service.user.FindUserService;
import co.jp.wever.graphql.domain.service.user.UpdateUserService;
import graphql.schema.DataFetcher;

@Component
public class UserDataFetcher {

    private final FindUserService findUserService;
    private final CreateUserService createUserService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;

    public UserDataFetcher(
        FindUserService findUserService,
        CreateUserService createUserService,
        UpdateUserService updateUserService,
        DeleteUserService deleteUserService) {
        this.findUserService = findUserService;
        this.createUserService = createUserService;
        this.updateUserService = updateUserService;
        this.deleteUserService = deleteUserService;
    }


    ///////////////////////////////
    ///////////   Query  //////////
    ///////////////////////////////
    public DataFetcher userDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return UserResponseConverter.toUserResponse(findUserService.findUser(userId));
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher createUserDataFetcher() {
        //TODO: 操作者と作成対象のユーザーが同じかチェック
        return dataFetchingEnvironment -> {
            Map<String, Object> userInputMap = (Map) dataFetchingEnvironment.getArgument("userInput");
            String userId = createUserService.createUser(UserInputConverter.toUserInput(userInputMap));
            return CreateResponse.builder().id(userId).build();
        };
    }

    public DataFetcher updateUserDataFetcher() {
        //TODO: 操作者と更新対象のユーザーが同じかチェック
        return dataFetchingEnvironment -> {
            Map<String, Object> userInputMap = (Map) dataFetchingEnvironment.getArgument("userInput");
            updateUserService.updateUser(UserInputConverter.toUserInput(userInputMap));
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteUserDataFetcher() {

        return dataFetchingEnvironment -> {

            //TODO: リクエストヘッダから取得する
            String targetUserId = "";
            deleteUserService.deleteUser(targetUserId);
            return UpdateResponse.builder().build();
        };
    }


    public DataFetcher followUserDataFetcher() {


        return dataFetchingEnvironment -> {
            String targetUserId = dataFetchingEnvironment.getArgument("targetUserId");

            //TODO: リクエストヘッダから取得する
            String followerUserId = "";
            updateUserService.followUser(targetUserId, followerUserId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher unFollowUserDataFetcher() {
        return dataFetchingEnvironment -> {
            String targetUserId = dataFetchingEnvironment.getArgument("targetUserId");

            //TODO: リクエストヘッダから取得する
            String followerUserId = "";
            updateUserService.unFollowUser(targetUserId, followerUserId);
            return UpdateResponse.builder().build();
        };
    }
}
