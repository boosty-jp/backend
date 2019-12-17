package co.jp.wever.graphql.application.datamodel.response.query.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionCountResponse {
    private int likeCount;
    private int learnedCount;
}
