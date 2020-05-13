package jp.boosty.graphql.domain.repository.page;

import jp.boosty.graphql.domain.domainmodel.search.SearchCondition;
import jp.boosty.graphql.infrastructure.datamodel.page.LikedPageListEntity;
import jp.boosty.graphql.infrastructure.datamodel.page.PageEntity;

import org.springframework.stereotype.Repository;

import jp.boosty.graphql.infrastructure.datamodel.page.PageListEntity;

@Repository
public interface PageQueryRepository {

    PageEntity findOne(String bookId, String articleId, String userId);

    PageEntity findOneForGuest(String pageId);

    PageEntity findOneToEdit(String pageId, String userId);

    PageListEntity findCreated(String userId, SearchCondition searchCondition);

    PageListEntity findCreatedBySelf(String userId, SearchCondition searchCondition);

    String findAuthorId(String articleId);

    LikedPageListEntity findLiked(String userId, int page);

    boolean canPreview(String pageId);
}
