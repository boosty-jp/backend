package co.jp.wever.graphql.domain.repository.user;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.application.datamodel.request.user.UserSettingInput;
import co.jp.wever.graphql.domain.domainmodel.user.User;

@Repository
public interface UserMutationRepository {
    String createOne(String displayName, String imageUrl, String uid);

    void deleteUser(String userId);

    void updateOne(User user);

    void updateSetting(String userId, UserSettingInput userSettingInput);
}
