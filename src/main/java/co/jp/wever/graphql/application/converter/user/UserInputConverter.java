package co.jp.wever.graphql.application.converter.user;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.tag.TagInputConverter;
import co.jp.wever.graphql.application.datamodel.request.UserInput;

public class UserInputConverter {
    public static UserInput toUserInput(Map<String, Object> input) {
        try {
            List<Map<String, Object>> tagsInputMap = (List<Map<String, Object>>) input.get("tags");

            return UserInput.builder()
                            .displayName(input.get("displayName").toString())
                            .description(input.get("description").toString())
                            .imageUrl(input.get("imageUrl").toString())
                            .url(input.get("url").toString())
                            .tags(tagsInputMap.stream()
                                              .map(t -> TagInputConverter.toTagInput(t))
                                              .collect(Collectors.toList()))
                            .build();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
