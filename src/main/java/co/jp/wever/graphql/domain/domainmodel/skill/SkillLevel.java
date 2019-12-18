package co.jp.wever.graphql.domain.domainmodel.skill;

import org.springframework.http.HttpStatus;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SkillLevel {

    private int value;

    private SkillLevel(int value) {
        this.value = value;
    }

    public static SkillLevel of(int value) {
        if (value < 1 || value > 3) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_SKILL_LEVEL.getString());
        }

        return new SkillLevel(value);
    }

    public int getValue() {
        return value;
    }
}
