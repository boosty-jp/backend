package jp.boosty.backend.application.converter.book;

import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

import jp.boosty.backend.application.converter.tag.TagResponseConverter;
import jp.boosty.backend.application.converter.user.UserResponseConverter;
import jp.boosty.backend.application.datamodel.response.query.book.BookResponse;
import jp.boosty.backend.infrastructure.datamodel.book.BookEntity;
import jp.boosty.backend.infrastructure.util.DateToStringConverter;

public class BookResponseConverter {
    public static BookResponse toBookResponse(BookEntity bookEntity, boolean canSale) {
        return BookResponse.builder()
                           .id(bookEntity.getBase().getId())
                           .title(bookEntity.getBase().getTitle())
                           .imageUrl(bookEntity.getBase().getImageUrl())
                           .description(bookEntity.getBase().getDescription())
                           .price(bookEntity.getBase().getPrice())
                           .canSale(canSale)
                           .features(bookEntity.getBase().getFeatures())
                           .targets(BookTargetResponseConverter.toBookTargetResponse(bookEntity.getBase()))
                           .status(bookEntity.getBase().getStatus())
                           .lastViewedPageId(bookEntity.getLastViewedPageId())
                           .purchased(bookEntity.isPurchased())
                           .createDate(DateToStringConverter.toDateString(new Date(bookEntity.getBase()
                                                                                             .getCreatedDate())))
                           .updateDate(DateToStringConverter.toDateString(new Date(bookEntity.getBase()
                                                                                             .getUpdatedDate())))
                           .tags(bookEntity.getTags()
                                           .stream()
                                           .map(t -> TagResponseConverter.toTagResponse(t))
                                           .collect(Collectors.toList()))
                           .sections(bookEntity.getSections()
                                               .stream()
                                               .map(s -> SectionResponseConverter.toSectionResponse(s))
                                               .collect(Collectors.toList())
                                               .stream()
                                               .sorted(Comparator.comparingLong(s -> s.getNumber()))
                                               .collect(Collectors.toList()))
                           .author(UserResponseConverter.toUserResponse(bookEntity.getAuthor()))
                           .build();
    }

    public static BookResponse toBookResponseForList(BookEntity book) {
        return BookResponse.builder()
                           .id(book.getBase().getId())
                           .title(book.getBase().getTitle())
                           .imageUrl(book.getBase().getImageUrl())
                           .status(book.getBase().getStatus())
                           .purchasedCount(book.getPurchaseCount())
                           .createDate(String.valueOf(book.getBase().getCreatedDate()))
                           .updateDate(String.valueOf(book.getBase().getUpdatedDate()))
                           .build();
    }

    public static BookResponse toBookResponseForOwnList(BookEntity book) {
        return BookResponse.builder()
                           .id(book.getBase().getId())
                           .title(book.getBase().getTitle())
                           .imageUrl(book.getBase().getImageUrl())
                           .status(book.getBase().getStatus())
                           .purchasedCount(book.getPurchaseCount())
                           .createDate(String.valueOf(book.getBase().getCreatedDate()))
                           .updateDate(String.valueOf(book.getBase().getUpdatedDate()))
                           .author(UserResponseConverter.toUserResponse(book.getAuthor()))
                           .build();
    }
}
