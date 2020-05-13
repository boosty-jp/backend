package jp.boosty.backend.application.converter.user;

import jp.boosty.backend.application.datamodel.response.query.user.AccountResponse;
import jp.boosty.backend.infrastructure.datamodel.user.UserEntity;

public class AccountResponseConverter {
    public static AccountResponse toAccountResponse(UserEntity userEntity) {
        return AccountResponse.builder()
                              .user(UserResponseConverter.toUserResponse(userEntity))
                              .build();
    }
}
