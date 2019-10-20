package co.jp.wever.graphql.infrastructure.datamodel.section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionStatisticEntity {
    private int like;
    private int bookmark;
}
