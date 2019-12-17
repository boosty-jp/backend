package co.jp.wever.graphql.application.converter.user;

import org.springframework.http.HttpStatus;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.UserSettingInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class UserSettingInputConverter {
    public static UserSettingInput toUserSettingInput(Map<String, Object> input) {
        try {
            return UserSettingInput.builder()
                                   .learnPublic((Boolean) input.get("learnPublic"))
                                   .likePublic((Boolean) input.get("likePublic"))
                                   .skillPublic((Boolean) input.get("skillPublic"))
                                   .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
