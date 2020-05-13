package jp.boosty.graphql.domain.repository.book;

import jp.boosty.graphql.infrastructure.datamodel.book.BookListEntity;

import org.springframework.stereotype.Repository;

import java.util.List;

import jp.boosty.graphql.domain.domainmodel.search.SearchCondition;
import jp.boosty.graphql.infrastructure.datamodel.book.BookEntity;

@Repository
public interface BookQueryRepository {
    BookEntity findOne(String bookId, String userId);

    BookEntity findOneForGuest(String bookId);

    String findAuthorId(String bookId);

    BookListEntity findCreated(String userId, SearchCondition searchCondition);

    BookListEntity findCreatedBySelf(String userId, SearchCondition searchCondition);

    BookListEntity findOwn(String userId, int page);

    BookListEntity findRecentlyViewed(String userId);

    BookListEntity findNew(int page);

    BookListEntity findFamous(int page);

    BookListEntity findFamousFree(int page);

    BookListEntity findSearched(String query, int page);

    List<BookEntity> findActioned(String userId, SearchCondition searchCondition);

    long findSectionCount(String bookId);

    long findPageCount(String sectionId);

    List<String> findSectionIds(String bookId);

    List<String> findPageIds(String sectionId);

    boolean isPurchased(String bookId, String userId);

    int findPrice(String bookId);

    String findStatus(String bookId);
}
