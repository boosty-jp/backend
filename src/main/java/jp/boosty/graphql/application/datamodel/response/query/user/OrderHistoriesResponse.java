package jp.boosty.graphql.application.datamodel.response.query.user;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderHistoriesResponse {
    private List<OrderHistoryResponse> orderHistories;
    private long sumCount;
}
