package co.jp.wever.graphql.domain.domainmodel.article;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.domainmodel.content.ContentBase;
import co.jp.wever.graphql.domain.domainmodel.skill.ArticleSkills;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Article {
    private ContentBase base;
    private ArticleBlocks blocks;
    private ArticleSkills skills;

    private Article(
        ContentBase base, ArticleBlocks blocks, ArticleSkills skills) {
        this.base = base;
        this.blocks = blocks;
        this.skills = skills;
    }

    public static Article of(ContentBase base, ArticleBlocks blocks, ArticleSkills skills) {
        if (Objects.isNull(base) || Objects.isNull(blocks) || Objects.isNull(skills)) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.NULL_DATA.getString());
        }

        return new Article(base, blocks, skills);
    }

    public boolean entry() {
        return base.isEntry();
    }

    public boolean canPublish() {
        return base.valid();
    }

    public ContentBase getBase() {
        return base;
    }

    public ArticleSkills getSkills() {
        return skills;
    }

    public ArticleBlocks getBlocks() {
        return blocks;
    }
}
