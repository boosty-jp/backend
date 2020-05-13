package jp.boosty.backend.application.datamodel.request.book;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookTargetsInput {
    private int levelStart;
    private int levelEnd;
    private List<String> targetsDescription;
}
