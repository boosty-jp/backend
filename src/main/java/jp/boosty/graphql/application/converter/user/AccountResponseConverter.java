package jp.boosty.graphql.application.converter.user;

import jp.boosty.graphql.application.datamodel.response.query.user.AccountResponse;
import jp.boosty.graphql.infrastructure.datamodel.user.UserEntity;

public class AccountResponseConverter {
    public static AccountResponse toAccountResponse(UserEntity userEntity) {
        return AccountResponse.builder()
                              .user(UserResponseConverter.toUserResponse(userEntity))
                              .build();
    }
}
