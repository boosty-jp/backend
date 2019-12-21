package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.application.datamodel.request.user.UserSettingInput;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

@Repository
public interface UserMutationRepository {
    String createOne(UserEntity userEntity);

    void deleteUser(String userId);

    void updateOne(UserEntity userEntity);

    void updateSetting(String userId, UserSettingInput userSettingInput);
}
