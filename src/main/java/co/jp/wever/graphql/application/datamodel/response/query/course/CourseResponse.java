package co.jp.wever.graphql.application.datamodel.response.query.course;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.query.tag.TagResponse;
import co.jp.wever.graphql.application.datamodel.response.query.user.AccountActionResponse;
import co.jp.wever.graphql.application.datamodel.response.query.user.ActionCountResponse;
import co.jp.wever.graphql.application.datamodel.response.query.user.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponse {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String status;
    private String createDate;
    private String updateDate;

    private List<TagResponse> tqgs;
    private List<SectionResponse> sections;
    private UserResponse author;

    private ActionCountResponse actionCount;
    private AccountActionResponse accountAction;
    private LearnStatusResponse learnStatus;
}
