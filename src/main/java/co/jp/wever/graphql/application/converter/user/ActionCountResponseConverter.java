package co.jp.wever.graphql.application.converter.user;

import co.jp.wever.graphql.application.datamodel.response.query.user.ActionCountResponse;
import co.jp.wever.graphql.domain.domainmodel.action.ActionCount;
import co.jp.wever.graphql.infrastructure.datamodel.user.ActionCountEntity;

public class ActionCountResponseConverter {
    public static ActionCountResponse toActionCountResponse(ActionCount actionCount) {
        return ActionCountResponse.builder()
                                  .learnedCount(actionCount.getLearnedCount().getValue())
                                  .likeCount(actionCount.getLikeCount().getValue())
                                  .build();
    }

    public static ActionCountResponse toActionCountResponse(ActionCountEntity actionCount) {
        return ActionCountResponse.builder()
                                  .learnedCount(actionCount.getLearnedCount())
                                  .likeCount(actionCount.getLikeCount())
                                  .build();
    }
}
