package co.jp.wever.graphql.domain.converter.user;

import co.jp.wever.graphql.application.datamodel.request.UserInput;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserDescription;
import co.jp.wever.graphql.domain.domainmodel.user.UserDisplayName;
import co.jp.wever.graphql.domain.domainmodel.user.UserFacebookId;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.domain.domainmodel.user.UserImageUrl;
import co.jp.wever.graphql.domain.domainmodel.user.UserTwitterId;
import co.jp.wever.graphql.domain.domainmodel.user.UserUrl;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

public class UserConverter {
    public static User toUser(UserEntity user) {

        return new User(UserId.of(user.getUserId()),
                        UserDisplayName.of(user.getDisplayName()),
                        UserDescription.of(user.getDescription()),
                        UserImageUrl.of(user.getImageUrl()),
                        UserUrl.of(user.getUrl()), );
    }

    public static User toUser(UserInput userInput, String userId) {
        return new User(UserId.of(userId),
                        UserDisplayName.of(userInput.getDisplayName()),
                        UserDescription.of(userInput.getDescription()),
                        UserImageUrl.of(userInput.getImageUrl()),
                        UserUrl.of(userInput.getUrl()),
                        UserTwitterId.of(userInput.getTwitterId()),
                        UserFacebookId.of(userInput.getFacebookId()));
    }
}
