package co.jp.wever.graphql.application.datamodel.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionInput {
    private String title;
    private int number;
    private List<ContentInput> contents;
}