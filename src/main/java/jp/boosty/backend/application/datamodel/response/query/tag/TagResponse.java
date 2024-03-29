package jp.boosty.backend.application.datamodel.response.query.tag;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagResponse {
    private String id;
    private String name;
    private long relatedCount;
}
