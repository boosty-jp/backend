package jp.boosty.backend.infrastructure.converter.entity.search;

import jp.boosty.backend.domain.domainmodel.book.Book;
import jp.boosty.backend.domain.domainmodel.book.BookBase;
import jp.boosty.backend.domain.domainmodel.book.BookTargetLevel;
import jp.boosty.backend.domain.domainmodel.content.ContentImageUrl;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.datamodel.algolia.BookSearchEntity;

import java.util.List;
import java.util.stream.Collectors;

public class BookSearchEntityConverter {
    public static BookSearchEntity toBookSearchEntityConverter(String bookId, Book book) {
        BookBase base = book.getBase();
        ContentImageUrl imageUrl = book.getImageUrl();
        BookTargetLevel level = book.getTargets().getLevel();
        List<String> tagIds = book.getTagIds().getTags().stream().map(t -> t.getValue()).collect(Collectors.toList());

        return BookSearchEntity.builder()
                               .objectID(bookId)
                               .title(base.getTitle().getValue())
                               .price(base.getPrice().getValue())
                               .tagIds(tagIds)
                               .description(base.getDescription().getValue())
                               .imageUrl(imageUrl.getValue())
                               .levelStart(level.getStart())
                               .levelEnd(level.getEnd())
                               .status(EdgeLabel.PUBLISH.getString())
                               .build();
    }
}
