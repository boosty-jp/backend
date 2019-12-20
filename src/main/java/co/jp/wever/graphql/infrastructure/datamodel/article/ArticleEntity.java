package co.jp.wever.graphql.infrastructure.datamodel.article;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.skill.SkillEntity;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.AccountActionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.ActionCountEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleEntity {
    private ArticleBaseEntity base;
    private List<ArticleBlockEntity> blocks;
    private List<TagEntity> tags;
    private List<SkillEntity> skills;
    private ActionCountEntity actionCount;
    private AccountActionEntity actions;
    private UserEntity author;
}
