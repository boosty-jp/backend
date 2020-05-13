package jp.boosty.backend.infrastructure.datamodel.algolia;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookSearchEntity {
    private String objectID;
    private String title;
    private int price;
    private List<String> tagIds;
    private String description;
    private String imageUrl;
    private String status;
    private int purchaseCount;
    private int levelStart;
    private int levelEnd;
    private long updateDate;
}
