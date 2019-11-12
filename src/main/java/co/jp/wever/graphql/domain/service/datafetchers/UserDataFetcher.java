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
import co.jp.wever.graphql.domain.domainmodel.TokenVerifier;
import co.jp.wever.graphql.domain.service.user.CreateUserService;
import co.jp.wever.graphql.domain.service.user.DeleteUserService;
import co.jp.wever.graphql.domain.service.user.FindUserService;
import co.jp.wever.graphql.domain.service.user.UpdateUserService;
import graphql.schema.DataFetcher;

@Component
public class UserDataFetcher {

    private final TokenVerifier tokenVerifier;
    private final FindUserService findUserService;
    private final CreateUserService createUserService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;
    private final RequesterConverter requesterConverter;

    public UserDataFetcher(
        TokenVerifier tokenVerifier,
        FindUserService findUserService,
        CreateUserService createUserService,
        UpdateUserService updateUserService,
        DeleteUserService deleteUserService,
        RequesterConverter requesterConverter) {
        this.tokenVerifier = tokenVerifier;
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

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher createUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> userInputMap = (Map) dataFetchingEnvironment.getArgument("user");
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            createUserService.createUser(UserInputConverter.toUserInput(userInputMap), userId);
            return CreateResponse.builder().id(userId).build();
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
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String imageUrl = dataFetchingEnvironment.getArgument("url");

            updateUserService.updateUserImage(imageUrl, userId);
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

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            deleteUserService.deleteUser(userId);
            return UpdateResponse.builder().build();
        };
    }


    public DataFetcher followUserDataFetcher() {


        return dataFetchingEnvironment -> {
            String targetUserId = dataFetchingEnvironment.getArgument("userId");
            String token = (String) dataFetchingEnvironment.getContext();
            String followerUserId = tokenVerifier.getUserId(token);

            updateUserService.followUser(targetUserId, followerUserId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher unFollowUserDataFetcher() {
        return dataFetchingEnvironment -> {
            String targetUserId = dataFetchingEnvironment.getArgument("userId");
            String token = (String) dataFetchingEnvironment.getContext();
            String followerUserId = tokenVerifier.getUserId(token);

            updateUserService.unFollowUser(targetUserId, followerUserId);
            return UpdateResponse.builder().build();
        };
    }
}
