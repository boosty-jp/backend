package jp.boosty.backend.application.converter.user;

import jp.boosty.backend.application.datamodel.request.user.UserInput;

import org.springframework.http.HttpStatus;

import java.util.Map;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class UserInputConverter {
    public static UserInput toUserInput(Map<String, Object> input) {
        try {
            return UserInput.builder()
                            .displayName(input.get("displayName").toString())
                            .description(input.get("description").toString())
                            .imageUrl(input.get("imageUrl").toString())
                            .url(input.get("url").toString())
                            .twitterId(input.get("twitterId").toString())
                            .githubId(input.get("githubId").toString())
                            .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
