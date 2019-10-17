package co.jp.wever.graphql.infrastructure.datamodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Article {
    private long id;
    private String title;
    private int price;
    private String image;
    private Boolean deleted;
    private Boolean publish;
}
