package jp.boosty.backend.infrastructure.datamodel.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountActionEntity {
    private boolean liked;
    private boolean learned;
}
