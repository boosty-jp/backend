package jp.boosty.backend.application.datamodel.request.book;

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
