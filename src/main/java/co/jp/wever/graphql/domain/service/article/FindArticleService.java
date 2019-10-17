package co.jp.wever.graphql.domain.service.article;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.domain.converter.article.ArticleDetailConverter;
import co.jp.wever.graphql.domain.converter.plan.PlanConverter;
import co.jp.wever.graphql.domain.domainmodel.article.ArticleDetail;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleDetailEntity;
import co.jp.wever.graphql.infrastructure.datamodel.article.aggregation.ArticleEntity;
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

        // データ取得
        // 下書きかどうかチェック
        // 下書きだった場合は認証チェックする

        return null;
    }

}
