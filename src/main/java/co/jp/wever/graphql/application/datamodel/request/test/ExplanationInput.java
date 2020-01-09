package co.jp.wever.graphql.application.datamodel.request.test;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExplanationInput {
    private String text;
    private List<String> referenceIds;
}
