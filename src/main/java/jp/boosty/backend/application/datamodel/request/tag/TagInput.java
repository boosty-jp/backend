package jp.boosty.backend.application.datamodel.request.tag;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagInput {
    private String id;
    private String name;
}