package co.jp.wever.graphql.application.converter.article;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.skill.SkillInputConverter;
import co.jp.wever.graphql.application.datamodel.request.article.ArticleInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import graphql.schema.DataFetchingEnvironment;

public class ArticleInputConverter {
    public static ArticleInput toArticleInput(DataFetchingEnvironment request) {
        try {
            Map<String, Object> article = request.getArgument("article");

            List<Map<String, Object>> skills = (List<Map<String, Object>>) article.get("skills");
            List<Map<String, Object>> blocks = (List<Map<String, Object>>) article.get("blocks");
            return ArticleInput.builder()
                               .id(article.get("id").toString())
                               .title(article.get("title").toString())
                               .imageUrl(article.get("imageUrl").toString())
                               .blocks(blocks.stream()
                                             .map(b -> ArticleBlockInputConverter.toArticleBlockInput(b))
                                             .collect(Collectors.toList()))
                               .tagIds((List<String>) article.get("tagIds"))
                               .skills(skills.stream()
                                             .map(s -> SkillInputConverter.toSkillInput(s))
                                             .collect(Collectors.toList()))
                               .build();
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
