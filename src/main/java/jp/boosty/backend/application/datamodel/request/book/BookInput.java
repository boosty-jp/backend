package jp.boosty.backend.application.datamodel.request.book;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookInput {
    private String id;
    private String title;
    private String imageUrl;
    private String description;

    private List<String> tagIds;
    private List<SectionInput> sections;
}
