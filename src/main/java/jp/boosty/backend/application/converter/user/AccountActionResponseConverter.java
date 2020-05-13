package jp.boosty.backend.application.converter.user;

import jp.boosty.backend.domain.domainmodel.action.AccountAction;
import jp.boosty.backend.application.datamodel.response.query.user.AccountActionResponse;
import jp.boosty.backend.infrastructure.datamodel.user.AccountActionEntity;

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
