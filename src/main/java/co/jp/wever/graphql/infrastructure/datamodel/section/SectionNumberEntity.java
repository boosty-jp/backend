package co.jp.wever.graphql.infrastructure.datamodel.section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionNumberEntity {
    private String id;
    private long number;
}
