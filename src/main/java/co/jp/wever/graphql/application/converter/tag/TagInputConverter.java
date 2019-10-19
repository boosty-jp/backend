package co.jp.wever.graphql.application.converter.tag;

import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.TagInput;

public class TagInputConverter {
    public static TagInput toTagInput(Map<String, Object> input) {
        try {
            return TagInput.builder().id(input.get("id").toString()).name(input.get("name").toString()).build();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
