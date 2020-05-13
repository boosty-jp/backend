package jp.boosty.graphql.application.datamodel.response.query.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountActionResponse {
    private boolean liked;
    private boolean learned;
}
