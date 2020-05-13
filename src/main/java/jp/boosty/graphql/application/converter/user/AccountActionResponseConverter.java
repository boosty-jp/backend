package jp.boosty.graphql.application.converter.user;

import jp.boosty.graphql.domain.domainmodel.action.AccountAction;
import jp.boosty.graphql.application.datamodel.response.query.user.AccountActionResponse;
import jp.boosty.graphql.infrastructure.datamodel.user.AccountActionEntity;

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
