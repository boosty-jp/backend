package co.jp.wever.graphql.application.datamodel.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSectionInput {
    private String title;
    private String text;
    private int number;
}
