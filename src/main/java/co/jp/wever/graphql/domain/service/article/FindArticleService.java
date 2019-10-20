package co.jp.wever.graphql.domain.service.article;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.converter.article.ArticleDetailConverter;
import co.jp.wever.graphql.domain.converter.article.ArticleOutlineConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleOutline;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleOutlineEntity;
import co.jp.wever.graphql.infrastructure.repository.article.FindArticleRepositoryImpl;

@Service
public class FindArticleService {

    private final FindArticleRepositoryImpl findArticleRepository;

    FindArticleService(FindArticleRepositoryImpl findArticleRepository) {
        this.findArticleRepository = findArticleRepository;
    }

    public ArticleDetail findArticleDetail(String articleId, String userId) throws IllegalAccessException {
        ArticleDetailEntity articleDetailEntity = findArticleRepository.findOne(articleId);

        ArticleDetail articleDetail = ArticleDetailConverter.toArticleDetail(articleDetailEntity);

        if (articleDetail.canRead(UserId.of(userId))) {
            throw new IllegalAccessException();
        }

        return articleDetail;
    }

    public List<ArticleOutline> findAllArticle(String userId) {
        List<ArticleOutlineEntity> articleOutlineEntities = findArticleRepository.findAll(userId);

        List<ArticleOutline> articleOutlines = articleOutlineEntities.stream()
                                                                     .map(e -> ArticleOutlineConverter.toArticleOutline(
                                                                         e))
                                                                     .collect(Collectors.toList());

        // 公開していない　かつ　そのユーザーじゃないものは含めない
        UserId readUserId = UserId.of(userId);
        return articleOutlines.stream().filter(e -> e.canRead(readUserId)).collect(Collectors.toList());
    }

    public List<ArticleOutline> findAllPublishedArticle(String userId) {
        List<ArticleOutlineEntity> articleOutlineEntities = findArticleRepository.findAllPublished(userId);

        return articleOutlineEntities.stream()
                                     .map(e -> ArticleOutlineConverter.toArticleOutline(e))
                                     .collect(Collectors.toList());
    }

    public List<ArticleOutline> findAllDraftedArticle(String userId) {
        List<ArticleOutlineEntity> articleOutlineEntities = findArticleRepository.findAllDrafted(userId);

        List<ArticleOutline> articleOutlines = articleOutlineEntities.stream()
                                                                     .map(e -> ArticleOutlineConverter.toArticleOutline(
                                                                         e))
                                                                     .collect(Collectors.toList());

        // 公開していない　かつ　そのユーザーじゃないものは含めない
        UserId readUserId = UserId.of(userId);
        return articleOutlines.stream().filter(e -> e.canRead(readUserId)).collect(Collectors.toList());
    }

    public List<ArticleOutline> findAllLikedArticle(String userId) {
        List<ArticleOutlineEntity> articleOutlineEntities = findArticleRepository.findAllLiked(userId);

        return articleOutlineEntities.stream()
                                     .map(e -> ArticleOutlineConverter.toArticleOutline(e))
                                     .collect(Collectors.toList());

    }

    public List<ArticleOutline> findAllLearnedArticle(String userId) {
        List<ArticleOutlineEntity> articleOutlineEntities = findArticleRepository.findAllLearned(userId);

        List<ArticleOutline> articleOutlines = articleOutlineEntities.stream()
                                                                     .map(e -> ArticleOutlineConverter.toArticleOutline(
                                                                         e))
                                                                     .collect(Collectors.toList());

        // 公開していない　かつ　そのユーザーじゃないものは含めない
        UserId readUserId = UserId.of(userId);
        return articleOutlines.stream().filter(e -> e.canRead(readUserId)).collect(Collectors.toList());
    }

    public List<ArticleOutline> findFamousArticle() {
        List<ArticleOutlineEntity> articleOutlineEntities = findArticleRepository.findFamous();

        return articleOutlineEntities.stream()
                                     .map(e -> ArticleOutlineConverter.toArticleOutline(e))
                                     .collect(Collectors.toList());

    }

    public List<ArticleOutline> findRelatedArticle(String userId) {
        List<ArticleOutlineEntity> articleOutlineEntities = findArticleRepository.findRelated(userId);

        return articleOutlineEntities.stream()
                                     .map(e -> ArticleOutlineConverter.toArticleOutline(e))
                                     .collect(Collectors.toList());
    }
}
