package co.jp.wever.graphql.domain.domainmodel.article;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.article.base.DraftArticleBase;
import co.jp.wever.graphql.domain.domainmodel.section.DraftSections;
import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;

public class DraftArticle {
    private DraftArticleBase base;
    private DraftSections sections;

    public DraftArticle(DraftArticleBase base, DraftSections sections) {
        this.base = base;
        this.sections = sections;
    }

    public String getId() {
        return base.getId().getValue();
    }

    public String getTitle() {
        return base.getTitle().getValue();
    }

    public String getImageUrl() {
        return base.getImageUrl().getValue();
    }

    public List<String> getTagIds() {
        return base.getTagIds().getValue();
    }

    public List<UpdateSection> getUpdateSection() {
        return sections.getUpdateSections();
    }
}
