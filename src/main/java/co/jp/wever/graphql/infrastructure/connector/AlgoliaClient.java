package co.jp.wever.graphql.infrastructure.connector;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.ArticleSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.SectionSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.TagSearchEntity;

@Component
public class AlgoliaClient {
    private SearchClient client;
    private SearchIndex<TagSearchEntity> tagIndex;
    private SearchIndex<ArticleSearchEntity> articleIndex;
    private SearchIndex<SectionSearchEntity> sectionIndex;

    public AlgoliaClient(
        @Value("${algolia.applicationId}") String applicationId, @Value("${algolia.adminKey}") String adminApiKey) {
        this.client = DefaultSearchClient.create(applicationId, adminApiKey);
        this.articleIndex = client.initIndex(VertexLabel.ARTICLE.getString(), ArticleSearchEntity.class);
        this.sectionIndex = client.initIndex(VertexLabel.SECTION.getString(), SectionSearchEntity.class);
        this.tagIndex = client.initIndex(VertexLabel.TAG.getString(), TagSearchEntity.class);
    }

    public SearchIndex<TagSearchEntity> getTagIndex() {
        return tagIndex;
    }

    public SearchIndex<ArticleSearchEntity> getArticleIndex() {
        return articleIndex;
    }

    public SearchIndex<SectionSearchEntity> getSectionIndex() {
        return sectionIndex;
    }
}
