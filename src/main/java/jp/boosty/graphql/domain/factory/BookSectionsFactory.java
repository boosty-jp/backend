package jp.boosty.graphql.domain.factory;

import java.util.List;
import java.util.stream.Collectors;

import jp.boosty.graphql.domain.domainmodel.book.section.BookSection;
import jp.boosty.graphql.domain.domainmodel.book.section.BookSections;
import jp.boosty.graphql.infrastructure.datamodel.book.BookSectionEntity;

public class BookSectionsFactory {
    public static BookSections make(List<BookSectionEntity> entities) {
        List<BookSection> sections =
            entities.stream().map(e -> BookSectionFactory.make(e)).collect(Collectors.toList());
        return BookSections.of(sections);
    }
}
