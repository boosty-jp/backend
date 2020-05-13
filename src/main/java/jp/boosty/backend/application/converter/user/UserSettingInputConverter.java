package jp.boosty.backend.application.converter.user;

import jp.boosty.backend.application.datamodel.request.user.UserSettingInput;

import org.springframework.http.HttpStatus;

import java.util.Map;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

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
