package jp.boosty.graphql.application.datamodel.response.query.user;

import jp.boosty.graphql.application.datamodel.response.query.book.BookResponse;
import jp.boosty.graphql.application.datamodel.response.query.page.PageResponse;

import java.util.List;

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
    private String githubId;

    private List<PageResponse> articles;
    private List<BookResponse> courses;
}
