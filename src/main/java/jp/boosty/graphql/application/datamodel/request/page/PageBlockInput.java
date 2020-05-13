package jp.boosty.graphql.application.datamodel.request.page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageBlockInput {
    private String type;
    private String data;
}
