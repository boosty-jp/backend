package jp.boosty.backend.application.datamodel.response.query.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionCountResponse {
    private long likeCount;
    private long learnedCount;
}
