package jp.boosty.graphql.application.converter.page;

import jp.boosty.graphql.application.datamodel.response.query.page.LikedPageResponse;
import jp.boosty.graphql.infrastructure.datamodel.page.LikedPageEntity;

public class LikePageResponseConverter {
    public static LikedPageResponse toLikePageResponse(LikedPageEntity pageEntity) {
        return LikedPageResponse.builder()
                                .id(pageEntity.getId())
                                .title(pageEntity.getTitle())
                                .bookId(pageEntity.getBookId())
                                .bookTitle(pageEntity.getBookTitle())
                                .bookImage(pageEntity.getBookImage())
                                .build();
    }
}
