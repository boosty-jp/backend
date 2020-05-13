package jp.boosty.graphql.application.datamodel.response.query.page;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageListResponse {
    private List<PageResponse> pages;
    private long sumCount;
}
