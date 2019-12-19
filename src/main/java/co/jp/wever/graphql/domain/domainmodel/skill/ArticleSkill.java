package co.jp.wever.graphql.domain.domainmodel.skill;

public class ArticleSkill {
    private SkillId id;
    private SkillLevel level;

    private ArticleSkill(SkillId id, SkillLevel level) {
        this.id = id;
        this.level = level;
    }

    public static ArticleSkill of(String id, int level) {
        return new ArticleSkill(SkillId.of(id), SkillLevel.of(level));
    }

    public SkillId getId() {
        return id;
    }

}
