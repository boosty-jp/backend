package jp.boosty.backend.application.datamodel.request.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookBaseInput {
    private String title;
    private String description;
    private int price;
}
