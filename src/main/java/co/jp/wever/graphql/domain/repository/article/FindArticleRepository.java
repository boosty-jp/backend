package co.jp.wever.graphql.domain.repository.article;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.search.others.SearchCondition;
import co.jp.wever.graphql.domain.domainmodel.search.self.SelfSearchCondition;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;

@Repository
public interface FindArticleRepository {
    ArticleEntity findOne(String articleId, String userId);

    ArticleEntity findOneForGuest(String articleId);

    List<ArticleEntity> findCreated(String userId, SearchCondition searchCondition);

    List<ArticleEntity> findCreatedBySelf(String userId, SelfSearchCondition searchCondition);

//    List<ArticleDetailEntity> findAll(String targetUserId, String requesterId);
//
//    List<ArticleDetailEntity> findAllPublished(String publisherUserId, String requesterId);
//
//    List<ArticleDetailEntity> findAllPublishedForGuest(String publisherUserId);
//
//    List<ArticleDetailEntity> findAllDrafted(String draftedUserId, String requesterId);
//
//    List<ArticleDetailEntity> findAllLiked(String likedUserId, String requesterId);
//
//    List<ArticleDetailEntity> findAllLikedForGuest(String likedUserId);
//
//    List<ArticleDetailEntity> findAllLearned(String learnedUserId, String requesterId);
//
//    List<ArticleDetailEntity> findAllLearnedForGuest(String learnedUserId);

    List<ArticleDetailEntity> findFamous(String requesterId);

    List<ArticleDetailEntity> findFamousForGuest();

    List<ArticleDetailEntity> findRelated(String userId);

    String findAuthorId(String articleId);
}
