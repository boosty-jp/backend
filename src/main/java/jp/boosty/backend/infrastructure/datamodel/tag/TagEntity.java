package jp.boosty.backend.infrastructure.datamodel.tag;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagEntity {
    private String id;
    private String name;
    private long relatedCount;
}
