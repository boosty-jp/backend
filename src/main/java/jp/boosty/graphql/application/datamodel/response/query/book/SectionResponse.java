package jp.boosty.graphql.application.datamodel.response.query.book;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {
    private String id;
    private long number;
    private String title;
    private List<PageResponse> pages;
    private String createDate;
    private String updateDate;
}
