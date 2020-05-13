package jp.boosty.graphql.infrastructure.datamodel.book;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookBaseEntity {
    private String id;
    private String title;
    private String imageUrl;
    private String description;
    private List<String> features;
    private int price;
    private int levelStart;
    private int levelEnd;
    private List<String> targetDescriptions;
    private String status;
    private long createdDate;
    private long updatedDate;
}
