package jp.boosty.backend.domain.service.datafetchers;

import jp.boosty.backend.application.converter.requester.RequesterConverter;
import jp.boosty.backend.application.converter.user.AccountResponseConverter;
import jp.boosty.backend.application.converter.user.UserInputConverter;
import jp.boosty.backend.application.converter.user.UserResponseConverter;
import jp.boosty.backend.application.datamodel.request.user.Requester;
import jp.boosty.backend.application.datamodel.response.mutation.CreateResponse;
import jp.boosty.backend.application.datamodel.response.query.user.SalesLinkResponse;
import jp.boosty.backend.domain.service.user.UserMutationService;
import jp.boosty.backend.domain.service.user.UserQueryService;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.backend.application.converter.user.OrderHistoryResponseConverter;
import jp.boosty.backend.application.datamodel.response.query.user.OrderHistoriesResponse;
import jp.boosty.backend.infrastructure.datamodel.user.OrderHistoriesEntity;

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

    public DataFetcher canSaleDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            return userQueryService.canSale(requester);
        };
    }

    public DataFetcher salesLinkDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            return SalesLinkResponse.builder().url(userQueryService.findSalesLink(requester)).build();
        };
    }

    public DataFetcher orderHistoriesDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            int page = dataFetchingEnvironment.getArgument("page");

            OrderHistoriesEntity orderHistoriesEntity = userQueryService.findOrderHistories(page, requester);
            return OrderHistoriesResponse.builder()
                                         .orderHistories(orderHistoriesEntity.getOrderHistoryEntityList()
                                                                             .stream()
                                                                             .map(o -> OrderHistoryResponseConverter.toOrderHistoryResponse(
                                                                                 o))
                                                                             .collect(Collectors.toList()))
                                         .sumCount(orderHistoriesEntity.getSumCount())
                                         .build();
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
            String imageUrl = dataFetchingEnvironment.getArgument("imageUrl");

            userMutationService.createUser(displayName, imageUrl, requester);
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

    public DataFetcher deleteUserDataFetcher() {

        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            userMutationService.deleteUser(requester);
            return true;
        };
    }

    public DataFetcher registerStripeDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String userId = dataFetchingEnvironment.getArgument("userId");
            String code = dataFetchingEnvironment.getArgument("code");
            userMutationService.registerStripe(userId, code, requester);
            return true;
        };
    }
}
