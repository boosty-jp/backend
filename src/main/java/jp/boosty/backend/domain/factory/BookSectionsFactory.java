package jp.boosty.backend.domain.factory;

import java.util.List;
import java.util.stream.Collectors;

import jp.boosty.backend.domain.domainmodel.book.section.BookSection;
import jp.boosty.backend.domain.domainmodel.book.section.BookSections;
import jp.boosty.backend.infrastructure.datamodel.book.BookSectionEntity;

public class BookSectionsFactory {
    public static BookSections make(List<BookSectionEntity> entities) {
        List<BookSection> sections =
            entities.stream().map(e -> BookSectionFactory.make(e)).collect(Collectors.toList());
        return BookSections.of(sections);
    }
}
