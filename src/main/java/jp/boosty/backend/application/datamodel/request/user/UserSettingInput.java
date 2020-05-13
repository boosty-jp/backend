package jp.boosty.backend.application.datamodel.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSettingInput {
    private Boolean skillPublic;
    private Boolean learnPublic;
    private Boolean likePublic;
}
