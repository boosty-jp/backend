package co.jp.wever.graphql.domain.domainmodel.skill;

import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class ArticleSkills {
    private List<ArticleSkill> skills;
    private final static int MAX_SKILL_COUNT = 3;

    private ArticleSkills(List<ArticleSkill> skills) {
        this.skills = skills;
    }

    public static ArticleSkills of(List<ArticleSkill> skills) {
        if(skills.isEmpty()){
            return new ArticleSkills(skills);
        }

        if (skills.size() > MAX_SKILL_COUNT) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.ARTICLE_SKILL_COUNT_OVER.getString());
        }

        boolean duplicated = (skills.size() != new HashSet<>(skills.stream()
                                                                   .map(ArticleSkill::getId)
                                                                   .collect(Collectors.toList())).size());

        if (duplicated) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SKILL_ID_DUPLICATED.getString());
        }

        return new ArticleSkills(skills);
    }

    public List<ArticleSkill> getSkills() {
        return skills;
    }
}
