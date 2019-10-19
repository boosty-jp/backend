package co.jp.wever.graphql.application.datamodel.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagInput {
    private String id;
    private String name;
}
