package co.jp.wever.graphql.application.converter.article;

import java.util.List;
import java.util.Map;

import co.jp.wever.graphql.application.datamodel.request.ArticleInput;

public class ArticleInputConverter {
    public static ArticleInput toArticleInput(Map<String, Object> input) {
        try {
            return ArticleInput.builder()
                               .title(input.get("title").toString())
                               .imageUrl(input.get("imageUrl").toString())
                               .tags((List<String>) input.get("tags"))
                               .build();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
