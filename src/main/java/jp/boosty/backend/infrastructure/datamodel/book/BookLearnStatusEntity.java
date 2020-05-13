package jp.boosty.backend.infrastructure.datamodel.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookLearnStatusEntity {
    private int progress;
    private String status;
}
