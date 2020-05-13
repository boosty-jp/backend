package jp.boosty.graphql.application.converter.book;

import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

import jp.boosty.graphql.application.datamodel.response.query.book.SectionResponse;
import jp.boosty.graphql.infrastructure.datamodel.book.BookSectionEntity;
import jp.boosty.graphql.infrastructure.util.DateToStringConverter;

public class SectionResponseConverter {
    public static SectionResponse toSectionResponse(BookSectionEntity section) {
        return SectionResponse.builder()
                              .id(section.getId())
                              .title(section.getTitle())
                              .number(section.getNumber())
                              .pages(section.getPages()
                                            .stream()
                                            .map(c -> PageResponseConverter.toPageResponse(c))
                                            .collect(Collectors.toList())
                                            .stream()
                                            .sorted(Comparator.comparingLong(p -> p.getNumber()))
                                            .collect(Collectors.toList()))
                              .createDate(DateToStringConverter.toDateString(new Date(section.getCreatedDate())))
                              .build();
    }
}
