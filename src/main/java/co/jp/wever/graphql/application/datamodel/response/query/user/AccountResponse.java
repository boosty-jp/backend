package co.jp.wever.graphql.application.datamodel.response.query.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {
    private UserResponse user;
    private UserSettingResponse setting;
}
