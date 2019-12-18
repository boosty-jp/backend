package co.jp.wever.graphql.domain.domainmodel.skill;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import io.netty.util.internal.StringUtil;

public class SkillId {

    private String value;

    private SkillId(String value) {
        this.value = value;
    }

    public static SkillId of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SKILL_ID_EMPTY.getString());
        }

        return new SkillId(value);
    }

    public String getValue() {
        return value;
    }
}
