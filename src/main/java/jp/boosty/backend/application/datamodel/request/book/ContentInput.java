package jp.boosty.backend.application.datamodel.request.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentInput {
    private String articleId;
    private int number;
}
