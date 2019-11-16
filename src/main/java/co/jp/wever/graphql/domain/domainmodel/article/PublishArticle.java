package co.jp.wever.graphql.domain.domainmodel.article;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.article.base.PublishArticleBase;
import co.jp.wever.graphql.domain.domainmodel.section.PublishSections;
import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;

public class PublishArticle {
    private PublishArticleBase base;
    private PublishSections sections;

    public PublishArticle(PublishArticleBase base, PublishSections sections) {
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
