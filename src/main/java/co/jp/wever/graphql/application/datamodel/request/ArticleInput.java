package co.jp.wever.graphql.application.datamodel.request;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleInput {
    private String id;
    private String title;
    private String imageUrl;
    private List<ArticleBlockInput>blocks;
    private List<String> tagIds;
    private List<SkillInput> skills;
}
