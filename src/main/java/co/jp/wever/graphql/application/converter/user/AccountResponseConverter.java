package co.jp.wever.graphql.application.converter.user;

import co.jp.wever.graphql.application.datamodel.response.query.user.AccountResponse;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

public class AccountResponseConverter {
    public static AccountResponse toAccountResponse(UserEntity userEntity) {
        return AccountResponse.builder()
                              .user(UserResponseConverter.toUserResponse(userEntity))
                              .setting(UserSettingResponseConverter.toUserSettingResponse(userEntity))
                              .build();
    }
}
