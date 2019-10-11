package co.jp.wever.graphql.datamodel;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section {
    private long id;
    private String title;
    private List<String> texts;
    private Boolean deleted;
}
