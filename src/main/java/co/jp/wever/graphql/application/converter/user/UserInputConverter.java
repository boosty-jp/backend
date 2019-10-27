package co.jp.wever.graphql.application.converter.user;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.UserInput;

public class UserInputConverter {
    public static UserInput toUserInput(Map<String, Object> input) {
        try {
            return UserInput.builder()
                            .displayName(input.get("displayName").toString())
                            .description(input.get("description").toString())
                            .url(input.get("url").toString())
                            .tags((List<String>) input.get("tags"))
                            .build();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
