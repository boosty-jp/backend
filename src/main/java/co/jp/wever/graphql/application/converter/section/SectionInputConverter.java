package co.jp.wever.graphql.application.converter.section;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;

public class SectionInputConverter {
    public static SectionInput toSectionInput(Map<String, Object> input) {
        try {
            return SectionInput.builder()
                               .title(input.get("title").toString())
                               .texts((List<String>) input.get("texts"))
                               .number((int) input.get("number"))
                               .build();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
