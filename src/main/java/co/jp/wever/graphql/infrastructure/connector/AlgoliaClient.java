package co.jp.wever.graphql.infrastructure.connector;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.ArticleSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.PlanSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.SkillSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.TagSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.UserSearchEntity;

@Component
public class AlgoliaClient {
    private SearchClient client;
    private SearchIndex<TagSearchEntity> tagIndex;
    private SearchIndex<ArticleSearchEntity> articleIndex;
    private SearchIndex<SkillSearchEntity> skillIndex;
    private SearchIndex<UserSearchEntity> userIndex;
    private SearchIndex<PlanSearchEntity> planIndex;

    public AlgoliaClient(
        @Value("${algolia.applicationId}") String applicationId, @Value("${algolia.adminKey}") String adminApiKey) {
        this.client = DefaultSearchClient.create(applicationId, adminApiKey);
        this.planIndex = client.initIndex(VertexLabel.COURSE.getString(), PlanSearchEntity.class);
        this.articleIndex = client.initIndex(VertexLabel.ARTICLE.getString(), ArticleSearchEntity.class);
        this.skillIndex = client.initIndex(VertexLabel.SKILL.getString(), SkillSearchEntity.class);
        this.tagIndex = client.initIndex(VertexLabel.TAG.getString(), TagSearchEntity.class);
        this.userIndex = client.initIndex(VertexLabel.USER.getString(), UserSearchEntity.class);
    }

    public SearchIndex<TagSearchEntity> getTagIndex() {
        return tagIndex;
    }

    public SearchIndex<SkillSearchEntity> getSkillIndex() {
        return skillIndex;
    }

    public SearchIndex<ArticleSearchEntity> getArticleIndex() {
        return articleIndex;
    }

    public SearchIndex<UserSearchEntity> getUserIndex() {
        return userIndex;
    }

    public SearchIndex<PlanSearchEntity> getPlanIndex() {
        return planIndex;
    }
}
