package co.jp.wever.graphql.application.converter.user;

import co.jp.wever.graphql.application.datamodel.response.query.user.UserSettingResponse;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

public class UserSettingResponseConverter {
    public static UserSettingResponse toUserSettingResponse(UserEntity userEntity) {
        return UserSettingResponse.builder()
                                  .learnPublic(userEntity.getLearnPublic())
                                  .likePublic(userEntity.getLikePublic())
                                  .skillPublic(userEntity.getSkillPublic())
                                  .build();
    }
}
