package jp.boosty.backend.application.converter.page;

import java.util.Date;

import jp.boosty.backend.application.datamodel.response.query.page.PageResponse;
import jp.boosty.backend.infrastructure.datamodel.page.PageEntity;
import jp.boosty.backend.infrastructure.util.DateToStringConverter;

public class PageResponseConverter {
    public static PageResponse toPageResponse(PageEntity page) {
        return PageResponse.builder()
                           .id(page.getId())
                           .title(page.getTitle())
                           .text(page.getText())
                           .liked(page.isLiked())
                           .likeCount(page.getLikeCount())
                           .canPreview(page.isCanPreview())
                           .createDate(DateToStringConverter.toDateString(new Date(page.getCreatedDate())))
                           .updateDate(DateToStringConverter.toDateString(new Date(page.getUpdatedDate())))
                           .build();
    }

    public static PageResponse toPageResponseForList(PageEntity page) {
        return PageResponse.builder().build();
    }
}
