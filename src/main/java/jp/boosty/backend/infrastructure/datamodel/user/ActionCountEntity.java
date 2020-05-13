package jp.boosty.backend.infrastructure.datamodel.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionCountEntity {
    private long likeCount;
    private long learnedCount;
}
