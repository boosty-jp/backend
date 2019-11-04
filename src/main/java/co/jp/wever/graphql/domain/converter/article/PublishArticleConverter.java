package co.jp.wever.graphql.domain.converter.article;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.domain.converter.section.PublishSectionsConverter;
import co.jp.wever.graphql.domain.domainmodel.article.PublishArticle;
import co.jp.wever.graphql.domain.domainmodel.article.base.PublishArticleBase;
import co.jp.wever.graphql.domain.domainmodel.section.PublishSections;

public class PublishArticleConverter {
    public static PublishArticle toPublishArticle(ArticleInput articleInput, List<UpdateSectionInput> sectionInputs) {
        PublishArticleBase base = PublishArticleBaseConverter.toPublishArticleBase(articleInput);
        PublishSections sections = PublishSectionsConverter.toPublishSections(sectionInputs);
        return new PublishArticle(base, sections);
    }
}
