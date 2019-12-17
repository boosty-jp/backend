package co.jp.wever.graphql.application.converter.user;

import org.springframework.http.HttpStatus;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.UserInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class UserInputConverter {
    public static UserInput toUserInput(Map<String, Object> input) {
        try {
            return UserInput.builder()
                            .displayName(input.get("displayName").toString())
                            .description(input.get("description").toString())
                            .imageUrl(input.get("imageUrl").toString())
                            .url(input.get("url").toString())
                            .twitterId(input.get("twitterId").toString())
                            .facebookId(input.get("facebookId").toString())
                            .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
