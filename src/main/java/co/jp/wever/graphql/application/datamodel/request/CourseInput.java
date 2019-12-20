package co.jp.wever.graphql.application.datamodel.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseInput {
    private String id;
    private String title;
    private String imageUrl;
    private String description;

    private List<String> tagIds;
    private List<SectionInput> sections;
}