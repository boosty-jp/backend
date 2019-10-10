package co.jp.wever.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
@AllArgsConstructor
public class Plan {
    private long id;
    private int price;
    private String name;
    private String image;
    private String description;
    private Boolean publish;
    private Boolean deleted;
}
