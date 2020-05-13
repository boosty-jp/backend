package jp.boosty.backend.application.datamodel.request.page;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageInput {
    private String title;
    private String text;
}
