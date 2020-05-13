package jp.boosty.backend.infrastructure.converter.entity.page;

import jp.boosty.backend.infrastructure.datamodel.algolia.PageSearchEntity;
import jp.boosty.backend.domain.domainmodel.page.Page;

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
