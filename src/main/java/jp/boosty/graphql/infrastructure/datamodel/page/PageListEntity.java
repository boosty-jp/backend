package jp.boosty.graphql.infrastructure.datamodel.page;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageListEntity {
    private List<PageEntity> pages;
    private long sumCount;
}
