package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.List;

import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.datamodel.article.ArticleUserActionEntity;

public class ArticleUserActionEntityConverter {
    public static ArticleUserActionEntity toArticleUserActionEntity(List<String> actionResult) {
        boolean liked = false;
        boolean learned = false;
        for (String action : actionResult) {
            if (action.equals(UserToArticleEdge.LIKED.getString())) {
                liked = true;
            }
            if (action.equals(UserToArticleEdge.LEARNED.getString())) {
                learned = true;
            }
        }

        return ArticleUserActionEntity.builder().learned(learned).liked(liked).build();
    }
}
