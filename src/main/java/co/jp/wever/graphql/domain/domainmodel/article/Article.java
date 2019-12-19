package co.jp.wever.graphql.domain.domainmodel.article;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.content.ContentBase;
import co.jp.wever.graphql.domain.domainmodel.skill.ArticleSkills;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Article {
    private ContentBase base;
    private ArticleTextUrl textUrl;
    private ArticleSkills skills;

    public Article(ContentBase base, ArticleTextUrl textUrl, ArticleSkills skills) {
        this.base = base;
        this.textUrl = textUrl;
        this.skills = skills;
    }

    public static Article of(ContentBase base, ArticleTextUrl textUrl, ArticleSkills skills) {
        if (Objects.isNull(base) || Objects.isNull(textUrl) || Objects.isNull(skills)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        return new Article(base, textUrl, skills);
    }

    public boolean entry() {
        return base.isEntry();
    }

    public boolean canPublish() {
        if (!base.valid()) {
            return false;
        }

        return textUrl.valid();
    }

    public ContentBase getBase() {
        return base;
    }

    public ArticleTextUrl getTextUrl() {
        return textUrl;
    }

    public ArticleSkills getSkills() {
        return skills;
    }
}
