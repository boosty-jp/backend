package jp.boosty.graphql.infrastructure.datamodel.book;

import java.util.List;

import jp.boosty.graphql.infrastructure.datamodel.tag.TagEntity;
import jp.boosty.graphql.infrastructure.datamodel.user.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookEntity {
    private BookBaseEntity base;
    private List<TagEntity> tags;
    private List<BookSectionEntity> sections;
    private String lastViewedPageId;
    private UserEntity author;
    private long purchaseCount;
    private boolean purchased;
}