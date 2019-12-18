package co.jp.wever.graphql.domain.domainmodel.skill;

public class Skill {
    private SkillId id;
    private SkillName name;
    private SkillLevel level;

    private Skill(SkillId id, SkillName name, SkillLevel level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public static Skill of(String id, String name, int level) {
        return new Skill(SkillId.of(id), SkillName.of(name), SkillLevel.of(level));
    }

    public SkillId getId() {
        return id;
    }

    public SkillName getName() {
        return name;
    }

    public SkillLevel getLevel() {
        return level;
    }

}
