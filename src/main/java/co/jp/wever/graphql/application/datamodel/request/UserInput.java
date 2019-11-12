package co.jp.wever.graphql.application.datamodel.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInput {
    private String displayName;
    private String description;
    private String url;
    private String imageUrl;
    private List<String> tags;
}
