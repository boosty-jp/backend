package co.jp.wever.graphql.application.converter.skill;

import org.springframework.http.HttpStatus;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.skill.SkillInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class SkillInputConverter {
    public static SkillInput toSkillInput(Map<String, Object> skill) {
        try {
            return SkillInput.builder().id(skill.get("id").toString()).level((int) skill.get("level")).build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
