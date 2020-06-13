package jp.boosty.backend.domain.repository.book;

import jp.boosty.backend.domain.domainmodel.book.Book;
import jp.boosty.backend.domain.domainmodel.content.ContentTags;
import jp.boosty.backend.infrastructure.datamodel.payment.PaymentEntity;

import org.springframework.stereotype.Repository;

import java.util.List;

import jp.boosty.backend.domain.domainmodel.book.BookBase;
import jp.boosty.backend.domain.domainmodel.book.BookFeatures;
import jp.boosty.backend.domain.domainmodel.book.BookTargets;
import jp.boosty.backend.domain.domainmodel.book.section.BookSectionTitle;

@Repository
public interface BookMutationRepository {
    String create(String userId);

    void updateBase(String bookId, BookBase base);

    void updateImageUrl(String bookId, String imageUrl);

    void updateFeatures(String bookId, BookFeatures features);

    void updateTargets(String bookId, BookTargets targets);

    void updateTags(String bookId, ContentTags tagIds);

    void updateMeaningful(String bookId, boolean meaningful);

    String addSection(String bookId, BookSectionTitle name, long sectionCount);

    void updateSectionTitle(String sectionId, BookSectionTitle title);

    void reorderSections(String bookId, List<String> sectionIds);

    void reorderPages(String sectionId, List<String> pageIds);

    String createPage(String sectionId, long pageCount, String userId);

    void deleteSection(String bookId, String sectionId);

    void publish(String bookId, Book book, String userId);

    void suspend(String bookId, String userId);

    void delete(String bookId, String userId);

    void purchase(PaymentEntity paymentEntity, String userId, String paymentIntentId);

    void addBookShelf(String bookId, String userId);
}
