package jp.boosty.graphql.domain.converter.user;

import jp.boosty.graphql.application.datamodel.request.user.UserInput;
import jp.boosty.graphql.domain.domainmodel.user.User;
import jp.boosty.graphql.domain.domainmodel.user.UserDescription;
import jp.boosty.graphql.domain.domainmodel.user.UserDisplayName;
import jp.boosty.graphql.domain.domainmodel.user.UserGithubId;
import jp.boosty.graphql.domain.domainmodel.user.UserId;
import jp.boosty.graphql.domain.domainmodel.user.UserImageUrl;
import jp.boosty.graphql.domain.domainmodel.user.UserTwitterId;
import jp.boosty.graphql.domain.domainmodel.user.UserUrl;
import jp.boosty.graphql.infrastructure.datamodel.user.UserEntity;

public class UserConverter {
    public static User toUser(UserEntity user) {

        return User.of(user.getUserId(),
                       user.getDisplayName(),
                       user.getDescription(),
                       user.getImageUrl(),
                       user.getUrl(),
                       user.getTwitterId(),
                       user.getGithubId());
    }

    public static User toUser(UserInput userInput, String userId) {
        return new User(UserId.of(userId),
                        UserDisplayName.of(userInput.getDisplayName()),
                        UserDescription.of(userInput.getDescription()),
                        UserImageUrl.of(userInput.getImageUrl()),
                        UserUrl.of(userInput.getUrl()),
                        UserTwitterId.of(userInput.getTwitterId()),
                        UserGithubId.of(userInput.getGithubId()));
    }
}
