package jp.boosty.graphql.domain.repository.page;

import org.springframework.stereotype.Repository;

import jp.boosty.graphql.domain.domainmodel.page.Page;

@Repository
public interface PageMutationRepository {
    void save(String bookId, String pageId, Page page);

    void delete(String articleId, String userId);

    void like(String pageId, String userId);

    void unLike(String pageId, String userId);

    void updateTrialRead(String pageId, boolean checked, String userId);
}
