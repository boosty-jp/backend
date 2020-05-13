package jp.boosty.graphql.application.datamodel.response.query.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesLinkResponse {
    String url;
}
