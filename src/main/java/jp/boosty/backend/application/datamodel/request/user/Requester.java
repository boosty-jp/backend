package jp.boosty.backend.application.datamodel.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Requester {
    private final String userId;
    private final boolean guest;
}
