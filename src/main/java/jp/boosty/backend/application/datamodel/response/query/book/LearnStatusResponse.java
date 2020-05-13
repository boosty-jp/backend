package jp.boosty.backend.application.datamodel.response.query.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearnStatusResponse {
    private int progress;
    private String status;
}
