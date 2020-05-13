package jp.boosty.graphql.application.datamodel.response.query.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSettingResponse {
    private boolean skillPublic;
    private boolean learnPublic;
    private boolean likePublic;
}
