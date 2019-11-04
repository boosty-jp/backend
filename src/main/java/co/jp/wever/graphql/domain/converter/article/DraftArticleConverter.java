package co.jp.wever.graphql.domain.converter.article;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;
import co.jp.wever.graphql.application.datamodel.request.UpdateSectionInput;
import co.jp.wever.graphql.domain.converter.section.DraftSectionsConverter;
import co.jp.wever.graphql.domain.domainmodel.article.DraftArticle;
import co.jp.wever.graphql.domain.domainmodel.article.base.DraftArticleBase;
import co.jp.wever.graphql.domain.domainmodel.section.DraftSections;

public class DraftArticleConverter {
    public static DraftArticle toDraftArticle(ArticleInput articleInput, List<UpdateSectionInput> sectionInputs) {
        DraftArticleBase base = DraftArticleBaseConverter.toDraftArticleBase(articleInput);
        DraftSections sections = DraftSectionsConverter.toDraftSections(sectionInputs);
        return new DraftArticle(base, sections);
    }
}
