package jp.boosty.backend.application.datamodel.response.query.book;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookListResponse {
    private List<BookResponse> books;
    private long sumCount;
}
