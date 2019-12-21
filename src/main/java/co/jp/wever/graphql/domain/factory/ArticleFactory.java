package co.jp.wever.graphql.domain.factory;

import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.article.ArticleInput;
import co.jp.wever.graphql.domain.domainmodel.article.Article;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleBlock;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleBlocks;
import co.jp.wever.graphql.domain.domainmodel.content.ContentBase;
import co.jp.wever.graphql.domain.domainmodel.skill.ArticleSkill;
import co.jp.wever.graphql.domain.domainmodel.skill.ArticleSkills;
import io.netty.util.internal.StringUtil;

public class ArticleFactory {
    public static Article make(ArticleInput articleInput) {

        boolean entry = false;
        if (StringUtil.isNullOrEmpty(articleInput.getId())) {
            entry = true;
        }

        ContentBase base =
            ContentBase.of(articleInput.getTitle(), articleInput.getImageUrl(), articleInput.getTagIds(), entry);
        ArticleBlocks blocks = ArticleBlocks.of(articleInput.getBlocks()
                                                            .stream()
                                                            .map(b -> ArticleBlock.of(b.getType(), b.getData()))
                                                            .collect(Collectors.toList()));
        ArticleSkills articleSkills = ArticleSkills.of(articleInput.getSkills()
                                                                   .stream()
                                                                   .map(s -> ArticleSkill.of(s.getId(), s.getLevel()))
                                                                   .collect(Collectors.toList()));

        return Article.of(base, blocks, articleSkills);
    }
}
