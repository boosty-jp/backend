package jp.boosty.graphql.infrastructure.datamodel.user;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderHistoriesEntity {
    private List<OrderHistoryEntity> orderHistoryEntityList;
    private long sumCount;
}
