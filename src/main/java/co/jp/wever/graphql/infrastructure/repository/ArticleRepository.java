package co.jp.wever.graphql.infrastructure.repository;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.datamodel.Article;

@Repository
public interface ArticleRepository {
    //Query
    Article findOne(String id);
    Article findAll(String id);
    Article findAllPublished(String id);
    Article findAllDrafted(String id);
    Article findAllLiked(String id);
    Article findAllLearned(String id);
    Article findAllBookmarked(String id);
    Article findFamous(String id);
    Article findRelated(String id);

    //Mutation
    Article initOne(String id);
    Article updateOne(String id);
    Article deleteOne(String id);
    Article publishOne(String id);
    Article draftOne(String id);
    Article bookmarkOne(String id);
    Article likeOne(String id);
}
