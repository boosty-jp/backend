package jp.boosty.graphql.application.converter.user;

import jp.boosty.graphql.domain.domainmodel.action.ActionCount;
import jp.boosty.graphql.application.datamodel.response.query.user.ActionCountResponse;
import jp.boosty.graphql.infrastructure.datamodel.user.ActionCountEntity;

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
