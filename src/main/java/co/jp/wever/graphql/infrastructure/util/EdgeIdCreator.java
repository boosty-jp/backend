package co.jp.wever.graphql.infrastructure.util;

import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToArticleEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToPlanEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToTagEdge;

public class EdgeIdCreator {
    public static String userLikeArticle(String userId, String articleId) {
        return userId + "-" + UserToArticleEdge.LIKED.getString() + "-" + articleId;
    }

    public static String userLearnArticle(String userId, String articleId) {
        return userId + "-" + UserToArticleEdge.LEARNED.getString() + "-" + articleId;
    }

    public static String userDraftArticle(String userId, String articleId) {
        return userId + "-" + UserToArticleEdge.DRAFTED.getString() + "-" + articleId;
    }

    public static String userPublishArticle(String userId, String articleId) {
        return userId + "-" + UserToArticleEdge.PUBLISHED.getString() + "-" + articleId;
    }

    public static String userDeleteArticle(String userId, String articleId) {
        return userId + "-" + UserToArticleEdge.DELETED.getString() + "-" + articleId;
    }

    public static String userLikeSection(String userId, String sectionId) {
        return userId + "-" + UserToSectionEdge.LIKED.getString() + "-" + sectionId;
    }

    public static String userDeleteSection(String userId, String sectionId) {
        return userId + "-" + UserToSectionEdge.DELETED.getString() + "-" + sectionId;
    }

    public static String articleIncludeSection(String articleId, String sectionId) {
        return articleId + "-" + ArticleToSectionEdge.INCLUDE.getString() + "-" + sectionId;
    }

    public static String userCreateSection(String userId, String sectionId) {
        return userId + "-" + UserToSectionEdge.CREATED.getString() + "-" + sectionId;
    }

    public static String userRelatedTag(String userId, String tagId) {
        return userId + "-" + UserToTagEdge.RELATED.getString() + "-" + tagId;
    }

    public static String userDraftedPlan(String userId, String planId) {
        return userId + "-" + UserToPlanEdge.DRAFTED.getString() + "-" + planId;
    }

    public static String articleRelatedTag(String articleId, String tagId) {
        return articleId + "-" + ArticleToTagEdge.RELATED.getString() + "-" + tagId;
    }

    public static String planRelatedTag(String planId, String tagId) {
        return planId + "-" + PlanToTagEdge.RELATED.getString() + "-" + tagId;
    }

    public static String userDeletePlan(String userId, String planId) {
        return userId + "-" + UserToPlanEdge.DELETED.getString() + "-" + planId;
    }
}
