package jp.boosty.graphql.application.converter.book;

import jp.boosty.graphql.application.datamodel.response.query.book.BookTargetResponse;
import jp.boosty.graphql.infrastructure.datamodel.book.BookBaseEntity;

public class BookTargetResponseConverter {
    public static BookTargetResponse toBookTargetResponse(BookBaseEntity baseEntity) {
        return BookTargetResponse.builder()
                                 .levelStart(baseEntity.getLevelStart())
                                 .levelEnd(baseEntity.getLevelEnd())
                                 .targetDescriptions(baseEntity.getTargetDescriptions())
                                 .build();
    }
}
