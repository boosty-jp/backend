package jp.boosty.backend.application.converter.user;

import jp.boosty.backend.domain.domainmodel.action.ActionCount;
import jp.boosty.backend.application.datamodel.response.query.user.ActionCountResponse;
import jp.boosty.backend.infrastructure.datamodel.user.ActionCountEntity;

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
