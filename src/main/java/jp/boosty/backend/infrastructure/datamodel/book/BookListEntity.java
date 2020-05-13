package jp.boosty.backend.infrastructure.datamodel.book;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookListEntity {
    private List<BookEntity> books;
    private long sumCount;
}
