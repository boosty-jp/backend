package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.Map;

import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.user.AccountResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserInputConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.converter.user.UserSettingInputConverter;
import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.domain.service.user.UserMutationService;
import co.jp.wever.graphql.domain.service.user.UserQueryService;
import graphql.schema.DataFetcher;

@Component
public class UserDataFetcher {

    private final UserQueryService userQueryService;
    private final UserMutationService userMutationService;
    private final RequesterConverter requesterConverter;

    public UserDataFetcher(
        UserQueryService userQueryService,
        UserMutationService userMutationService,
        RequesterConverter requesterConverter) {
        this.userQueryService = userQueryService;
        this.userMutationService = userMutationService;
        this.requesterConverter = requesterConverter;
    }

    ///////////////////////////////
    ///////////   Query  //////////
    ///////////////////////////////
    public DataFetcher userDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return UserResponseConverter.toUserResponse(userQueryService.findUser(userId));
        };
    }

    public DataFetcher accountDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            return AccountResponseConverter.toAccountResponse(userQueryService.findAccount(requester));
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher createUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> userInputMap = dataFetchingEnvironment.getArgument("user");
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String displayName = dataFetchingEnvironment.getArgument("displayName");
            String imageUrl= dataFetchingEnvironment.getArgument("imageUrl");

            userMutationService.createUser(displayName, imageUrl,requester);
            return CreateResponse.builder().id(requester.getUserId()).build();
        };
    }

    public DataFetcher updateUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> userInputMap = dataFetchingEnvironment.getArgument("user");
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            userMutationService.updateUser(UserInputConverter.toUserInput(userInputMap), requester);
            return true;
        };
    }

    public DataFetcher updateUserSettingDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> userSettingInputMap = dataFetchingEnvironment.getArgument("setting");
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            userMutationService.updateUserSetting(UserSettingInputConverter.toUserSettingInput(userSettingInputMap),
                                                requester);
            return true;
        };
    }

    public DataFetcher deleteUserDataFetcher() {

        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            userMutationService.deleteUser(requester);
            return true;
        };
    }
}
