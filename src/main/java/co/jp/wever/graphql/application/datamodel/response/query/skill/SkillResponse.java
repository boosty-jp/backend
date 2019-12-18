package co.jp.wever.graphql.application.datamodel.response.query.skill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResponse {
    private String id;
    private String name;
    private long relatedCount;
    private long level;
}