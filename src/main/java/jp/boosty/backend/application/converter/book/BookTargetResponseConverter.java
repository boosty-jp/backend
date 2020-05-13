package jp.boosty.backend.application.converter.book;

import jp.boosty.backend.application.datamodel.response.query.book.BookTargetResponse;
import jp.boosty.backend.infrastructure.datamodel.book.BookBaseEntity;

public class BookTargetResponseConverter {
    public static BookTargetResponse toBookTargetResponse(BookBaseEntity baseEntity) {
        return BookTargetResponse.builder()
                                 .levelStart(baseEntity.getLevelStart())
                                 .levelEnd(baseEntity.getLevelEnd())
                                 .targetDescriptions(baseEntity.getTargetDescriptions())
                                 .build();
    }
}
