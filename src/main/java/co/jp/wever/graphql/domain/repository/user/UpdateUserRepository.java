package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.application.datamodel.request.UserSettingInput;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Repository
public interface UpdateUserRepository {
    void updateOne(UserEntity userEntity);

    void updateSetting(String userId, UserSettingInput userSettingInput);
}
