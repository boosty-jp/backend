package jp.boosty.backend.application.datamodel.request.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchConditionInput {
    private String filter;
    private String sortField;
    private String sortOrder;
    private int page;
    private int resultCount;
}
