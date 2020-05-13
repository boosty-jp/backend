package jp.boosty.graphql.infrastructure.converter.entity.search;

import jp.boosty.graphql.domain.domainmodel.book.Book;
import jp.boosty.graphql.domain.domainmodel.book.BookBase;
import jp.boosty.graphql.domain.domainmodel.book.BookTargetLevel;
import jp.boosty.graphql.domain.domainmodel.content.ContentImageUrl;
import jp.boosty.graphql.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.graphql.infrastructure.datamodel.algolia.BookSearchEntity;

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
