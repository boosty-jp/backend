package co.jp.wever.graphql.domain.service.article;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.article.ArticleDetailConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.repository.article.FindArticleRepositoryImpl;

@Service
public class FindArticleService {

    private final FindArticleRepositoryImpl findArticleRepository;

    FindArticleService(FindArticleRepositoryImpl findArticleRepository) {
        this.findArticleRepository = findArticleRepository;
    }

    public ArticleDetail findArticleDetail(String articleId, String userId) {
        ArticleDetailEntity articleDetailEntity = findArticleRepository.findOne(articleId);

        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(articleDetailEntity);

        if (!articleDetail.canRead(UserId.of(userId))) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        return articleDetail;
    }

    public List<ArticleDetail> findAllArticle(String userId) {
        List<ArticleDetailEntity> results = findArticleRepository.findAll(userId);

        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findAllPublishedArticle(String userId) {
        List<ArticleDetailEntity> results = findArticleRepository.findAllPublished(userId);

        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findAllDraftedArticle(String userId) {
        List<ArticleDetailEntity> results = findArticleRepository.findAllDrafted(userId);
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findAllLikedArticle(String userId) {
        List<ArticleDetailEntity> results = findArticleRepository.findAllLiked(userId);
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findAllLearnedArticle(String userId) {
        List<ArticleDetailEntity> results = findArticleRepository.findAllLearned(userId);
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findFamousArticle() {
        List<ArticleDetailEntity> results = findArticleRepository.findFamous();
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }

    public List<ArticleDetail> findRelatedArticle(String userId) {

        List<ArticleDetailEntity> results = findArticleRepository.findRelated(userId);
        return results.stream().map(e -> ArticleDetailConverter.toArticleDetail(e)).collect(Collectors.toList());
    }
}
