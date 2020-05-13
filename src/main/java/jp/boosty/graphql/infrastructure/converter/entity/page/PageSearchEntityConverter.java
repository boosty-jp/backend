package jp.boosty.graphql.infrastructure.converter.entity.page;

import jp.boosty.graphql.infrastructure.datamodel.algolia.PageSearchEntity;
import jp.boosty.graphql.domain.domainmodel.page.Page;

public class PageSearchEntityConverter {
    public static PageSearchEntity toPageSearchEntity(String bookId, String pageId, Page page, long updateTime) {
        return PageSearchEntity.builder()
                               .objectID(pageId)
                               .bookId(bookId)
                               .title(page.getTitle().getValue())
                               .text(page.getText().getValue())
                               .updateDate(updateTime)
                               .build();
    }
}
