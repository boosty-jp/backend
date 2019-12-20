package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.user.AccountResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserInputConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserSettingInputConverter;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import graphql.schema.DataFetcher;

@Component
public class UserDataFetcher {

    private final FindUserService findUserService;
    private final CreateUserService createUserService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;
    private final RequesterConverter requesterConverter;

    public UserDataFetcher(
        FindUserService findUserService,
        CreateUserService createUserService,
        UpdateUserService updateUserService,
        DeleteUserService deleteUserService,
        RequesterConverter requesterConverter) {
        this.findUserService = findUserService;
        this.createUserService = createUserService;
        this.updateUserService = updateUserService;
        this.deleteUserService = deleteUserService;
        this.requesterConverter = requesterConverter;
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

    public DataFetcher accountDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            return AccountResponseConverter.toAccountResponse(findUserService.findAccount(requester));
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher createUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> userInputMap = dataFetchingEnvironment.getArgument("user");
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            createUserService.createUser(UserInputConverter.toUserInput(userInputMap), requester);
            return CreateResponse.builder().id(requester.getUserId()).build();
        };
    }

    public DataFetcher updateUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> userInputMap = dataFetchingEnvironment.getArgument("user");
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            updateUserService.updateUser(UserInputConverter.toUserInput(userInputMap), requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updateUserSettingDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> userSettingInputMap = dataFetchingEnvironment.getArgument("setting");
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            updateUserService.updateUserSetting(UserSettingInputConverter.toUserSettingInput(userSettingInputMap),
                                                requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deleteUserDataFetcher() {

        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            deleteUserService.deleteUser(requester);
            return UpdateResponse.builder().build();
        };
    }
}
