package co.jp.wever.graphql.application.datamodel.request.test;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestInput {
    private String id;
    private String title;
    private String description;
    private String referenceCourseId;
    private List<QuestionInput> questions;
}
