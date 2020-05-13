package jp.boosty.backend.infrastructure.converter.entity.book;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.converter.entity.tag.TagEntityConverter;
import jp.boosty.backend.infrastructure.converter.entity.user.UserEntityConverter;
import jp.boosty.backend.infrastructure.datamodel.book.BookEntity;

public class BookEntityConverter {
    public static BookEntity toBookEntity(Map<String, Object> allResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) allResult.get("base");
        List<Map<Object, Object>> featureResult = (List<Map<Object, Object>>) allResult.get("features");
        List<Map<Object, Object>> targetDescriptionResult =
            (List<Map<Object, Object>>) allResult.get("targetDescriptions");
        List<Map<String, Object>> sectionResult = (List<Map<String, Object>>) allResult.get("sections");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) allResult.get("tags");
        Map<Object, Object> authorResult = (Map<Object, Object>) allResult.get("author");
        String statusResult = allResult.get("status").toString();
        boolean purchased = (boolean) allResult.get("purchased");
        String lastViewedPageId = (String) allResult.get("lastViewedPageId");

        return BookEntity.builder()
                         .base(BookBaseEntityConverter.toBookBaseEntity(baseResult,
                                                                        statusResult,
                                                                        featureResult,
                                                                        targetDescriptionResult))
                         .sections(sectionResult.stream()
                                                .map(s -> BookSectionEntityConverter.toBookSectionEntity(s))
                                                .collect(Collectors.toList()))
                         .tags(tagResult.stream()
                                        .map(t -> TagEntityConverter.toTagEntity(t))
                                        .collect(Collectors.toList()))
                         .lastViewedPageId(lastViewedPageId)
                         .author(UserEntityConverter.toUserEntity(authorResult))
                         .purchased(purchased)
                         .build();
    }

    public static BookEntity toBookEntityForGuest(Map<String, Object> allResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) allResult.get("base");
        List<Map<Object, Object>> featureResult = (List<Map<Object, Object>>) allResult.get("features");
        List<Map<Object, Object>> targetDescriptionResult =
            (List<Map<Object, Object>>) allResult.get("targetDescriptions");
        List<Map<String, Object>> sectionResult = (List<Map<String, Object>>) allResult.get("sections");
        Map<Object, Object> authorResult = (Map<Object, Object>) allResult.get("author");
        List<Map<Object, Object>> tagResult = (List<Map<Object, Object>>) allResult.get("tags");
        String statusResult = allResult.get("status").toString();

        return BookEntity.builder()
                         .base(BookBaseEntityConverter.toBookBaseEntity(baseResult,
                                                                        statusResult,
                                                                        featureResult,
                                                                        targetDescriptionResult))
                         .sections(sectionResult.stream()
                                                .map(s -> BookSectionEntityConverter.toCourseSectionEntityForGuest(s))
                                                .collect(Collectors.toList()))
                         .author(UserEntityConverter.toUserEntity(authorResult))
                         .tags(tagResult.stream()
                                        .map(t -> TagEntityConverter.toTagEntity(t))
                                        .collect(Collectors.toList()))
                         .build();
    }

    public static BookEntity toBookEntityForListWithStatus(Map<String, Object> findResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) findResult.get("base");
        String status = (String) findResult.get("status");
        long purchaseCount = (long) findResult.get("purchaseCount");

        return BookEntity.builder()
                         .base(BookBaseEntityConverter.toBookBaseEntityForList(baseResult, status))
                         .purchaseCount(purchaseCount)
                         .build();
    }

    public static BookEntity toBookEntityForListHidePurchaseCount(Map<String, Object> findResult) {
        Map<Object, Object> baseResult = (Map<Object, Object>) findResult.get("base");
        String status = (String) findResult.get("status");
        long purchaseCount = 0;

        return BookEntity.builder()
                         .base(BookBaseEntityConverter.toBookBaseEntityForList(baseResult, status))
                         .purchaseCount(purchaseCount)
                         .build();
    }

    public static BookEntity toBookEntityForOwnList(Map<String, Object> results) {
        Map<Object, Object> baseResult = (Map<Object, Object>) results.get("base");
        Map<Object, Object> authorResult = (Map<Object, Object>) results.get("author");
        return BookEntity.builder()
                         .base(BookBaseEntityConverter.toBookBaseEntityForList(baseResult,
                                                                               EdgeLabel.PUBLISH.getString()))
                         .author(UserEntityConverter.toUserEntity(authorResult))
                         .build();
    }
}
