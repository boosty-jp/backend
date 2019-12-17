package co.jp.wever.graphql.application.datamodel.response.query.user;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.query.article.ArticleResponse;
import co.jp.wever.graphql.application.datamodel.response.query.course.CourseResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String displayName;
    private String imageUrl;
    private String description;
    private String url;
    private String twitterId;
    private String facebookId;

    private List<ArticleResponse> articles;
    private List<CourseResponse> courses;
}
