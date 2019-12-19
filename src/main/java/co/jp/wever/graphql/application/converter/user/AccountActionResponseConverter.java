package co.jp.wever.graphql.application.converter.user;

import co.jp.wever.graphql.application.datamodel.response.query.user.AccountActionResponse;
import co.jp.wever.graphql.domain.domainmodel.action.AccountAction;
import co.jp.wever.graphql.infrastructure.datamodel.user.AccountActionEntity;

public class AccountActionResponseConverter {
    public static AccountActionResponse toAccountAction(AccountAction accountAction) {
        return AccountActionResponse.builder()
                                    .learned(accountAction.isLearned())
                                    .liked(accountAction.isLiked())
                                    .build();
    }

    public static AccountActionResponse toAccountAction(AccountActionEntity accountAction) {
        return AccountActionResponse.builder()
                                    .learned(accountAction.isLearned())
                                    .liked(accountAction.isLiked())
                                    .build();
    }
}
