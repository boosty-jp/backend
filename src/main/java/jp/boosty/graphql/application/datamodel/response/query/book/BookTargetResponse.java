package jp.boosty.graphql.application.datamodel.response.query.book;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookTargetResponse {
    private int levelStart;
    private int levelEnd;
    private List<String> targetDescriptions;
}
