package co.jp.wever.graphql.application.datamodel.response.query.course;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {
    private String id;
    private int number;
    private String title;
    private List<ContentResponse> contents;
    private String createDate;
    private String updateDate;
}
