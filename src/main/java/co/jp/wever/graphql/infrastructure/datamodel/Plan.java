package co.jp.wever.graphql.infrastructure.datamodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Plan {
    private String id;
    private int price;
    private String title;
    private String image;
    private String description;
    private Boolean publish;
    private Boolean deleted;
}
