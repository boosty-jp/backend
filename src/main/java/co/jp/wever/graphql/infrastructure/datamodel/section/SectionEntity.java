package co.jp.wever.graphql.infrastructure.datamodel.section;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionEntity {
    private String id;
    private String title;
    private int number;
    private List<String> texts;
    private Boolean deleted;
    private String authorId;
    private SectionStatisticEntity statisticEntity;
}
