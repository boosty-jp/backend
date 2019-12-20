package co.jp.wever.graphql.application.datamodel.response.query.article;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.response.query.skill.SkillResponse;
import co.jp.wever.graphql.application.datamodel.response.query.tag.TagResponse;
import co.jp.wever.graphql.application.datamodel.response.query.user.AccountActionResponse;
import co.jp.wever.graphql.application.datamodel.response.query.user.ActionCountResponse;
import co.jp.wever.graphql.application.datamodel.response.query.user.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleResponse {
    private String id;
    private String title;
    private String imageUrl;
    private String status;
    private List<ArticleBlockResponse> blocks;
    private String createDate;
    private String updateDate;

    private List<TagResponse> tags;
    private UserResponse author;
    private List<SkillResponse> skills;
    private ActionCountResponse actionCount;
    private AccountActionResponse accountAction;
}
