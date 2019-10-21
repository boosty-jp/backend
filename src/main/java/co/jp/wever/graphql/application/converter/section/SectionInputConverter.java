package co.jp.wever.graphql.application.converter.section;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;

public class SectionInputConverter {
    public static SectionInput toSectionInput(Map<String, Object> input) {
        try {
            return SectionInput.builder()
                               .title(input.get("title").toString())
                               .text(input.get("text").toString())
                               .number((int) input.get("number"))
                               .build();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
