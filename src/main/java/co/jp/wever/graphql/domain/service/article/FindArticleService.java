package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.article.ArticleConverter;
import co.jp.wever.graphql.domain.domainmodel.article.Article;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.repository.article.FindArticleRepositoryImpl;

@Service
public class FindArticleService {

    private final FindArticleRepositoryImpl findArticleRepository;

    FindArticleService(FindArticleRepositoryImpl findArticleRepository) {
        this.findArticleRepository = findArticleRepository;
    }

    public Article findArticle(String articleId, Requester requester) {
        ArticleEntity articleEntity;
        if (requester.isGuest()) {
            articleEntity = findArticleRepository.findOneForGuest(articleId);
        } else {
            articleEntity = findArticleRepository.findOne(articleId, requester.getUserId());
        }

        Article article = ArticleConverter.toArticle(articleEntity);

        if (!article.canRead(requester)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return article;
    }

    public List<ArticleDetail> findAllArticle(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        List<ArticleDetailEntity> results = findArticleRepository.findAll(requester.getUserId(), requester.getUserId());

        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findAllPublishedArticle(String userId, Requester requester) {
        List<ArticleDetailEntity> results;
        if (requester.isGuest()) {
            results = findArticleRepository.findAllPublishedForGuest(userId);
        } else {
            results = findArticleRepository.findAllPublished(userId, requester.getUserId());
        }

        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findAllDraftedArticle(Requester requester) {
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(), GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        List<ArticleDetailEntity> results =
            findArticleRepository.findAllDrafted(requester.getUserId(), requester.getUserId());
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findAllLikedArticle(String userId, Requester requester) {
        List<ArticleDetailEntity> results;
        if (requester.isGuest()) {
            results = findArticleRepository.findAllLikedForGuest(userId);
        } else {
            results = findArticleRepository.findAllLiked(userId, requester.getUserId());
        }
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findAllLearnedArticle(String userId, Requester requester) {
        List<ArticleDetailEntity> results;
        if (requester.isGuest()) {
            results = findArticleRepository.findAllLearnedForGuest(userId);
        } else {
            results = findArticleRepository.findAllLearned(userId, requester.getUserId());
        }
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findFamousArticle(Requester requester) {
        List<ArticleDetailEntity> results;
        if (requester.isGuest()) {
            results = findArticleRepository.findFamousForGuest();
        } else {
            results = findArticleRepository.findFamous(requester.getUserId());
        }

        List<ArticleDetailEntity> filtered = results.stream()
                                                    .filter(r -> r.getBase()
                                                                  .getStatus()
                                                                  .equals(UserToArticleEdge.PUBLISHED.getString()))
                                                    .sorted((r1, r2) -> Long.compare(
                                                        r2.getStatistics().getLearnedCount() + r2.getStatistics()
                                                                                                 .getLikeCount(),
                                                        r1.getStatistics().getLearnedCount() + r1.getStatistics()
                                                                                                 .getLikeCount()))
                                                    .limit(10)
                                                    .collect(Collectors.toList());
        return filtered.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findRelatedArticle(String userId) {

        List<ArticleDetailEntity> results = findArticleRepository.findRelated(userId);
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }
}
