package co.jp.wever.graphql.domain.domainmodel.skill;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.util.AbuseWordDetector;
import io.netty.util.internal.StringUtil;

public class SkillName {

    private String value;

    private SkillName(String value) {
        this.value = value;
    }

    public static SkillName of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SKILL_NAME_EMPTY.getString());
        }

        if (AbuseWordDetector.isAbuseWord(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SKILL_NAME.getString());
        }

        return new SkillName(value);
    }

    public String getValue() {
        return value;
    }
}
