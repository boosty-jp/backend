package co.jp.wever.graphql.infrastructure.datamodel.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestBaseEntity {
    private String id;
    private String title;
    private String description;
    private String status;
    private long createdDate;
    private long updatedDate;
}
