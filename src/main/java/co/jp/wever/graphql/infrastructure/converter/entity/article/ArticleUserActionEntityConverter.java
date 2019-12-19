package co.jp.wever.graphql.infrastructure.converter.entity.article;

import java.util.List;

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
