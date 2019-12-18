package co.jp.wever.graphql.domain.domainmodel.skill;

import org.springframework.http.HttpStatus;

import java.util.List;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ArticleSkills {


    private List<Skill> skills;
    private final static int MAX_SKILL_COUNT = 3;

    private ArticleSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public static ArticleSkills of(List<Skill> skills) {
        if (skills.size() > MAX_SKILL_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.ARTICLE_SKILL_COUNT_OVER.getString());
        }

        return new ArticleSkills(skills);
    }

    public List<Skill> getSkills() {
        return skills;
    }
}
