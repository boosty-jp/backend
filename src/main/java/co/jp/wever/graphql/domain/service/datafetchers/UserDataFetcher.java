package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.converter.user.UserInputConverter;
import co.jp.wever.graphql.application.converter.user.UserResponseConverter;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateImageResponse;
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

    public DataFetcher profileDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            return UserResponseConverter.toUserResponse(findUserService.findProfile(requester));
        };
    }
    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher createUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> userInputMap = (Map) dataFetchingEnvironment.getArgument("user");
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            createUserService.createUser(UserInputConverter.toUserInput(userInputMap), requester);
            return CreateResponse.builder().id(requester.getUserId()).build();
        };
    }

    public DataFetcher updateUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            Map<String, Object> userInputMap = (Map) dataFetchingEnvironment.getArgument("user");

            updateUserService.updateUser(UserInputConverter.toUserInput(userInputMap), requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updateUserImageDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String imageUrl = dataFetchingEnvironment.getArgument("url");

            updateUserService.updateUserImage(imageUrl, requester);
            return UpdateImageResponse.builder().url(imageUrl).build();
        };
    }

    public DataFetcher updateUserTagsDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            List<String> tags = dataFetchingEnvironment.getArgument("tags");

            updateUserService.updateUserTags(tags, requester);
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
