package co.jp.wever.graphql.application.datamodel.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSettingInput {
    private Boolean skillPublic;
    private Boolean learnPublic;
    private Boolean likePublic;
}
