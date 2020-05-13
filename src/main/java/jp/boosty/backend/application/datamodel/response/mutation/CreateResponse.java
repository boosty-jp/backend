package jp.boosty.backend.application.datamodel.response.mutation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateResponse {
    private String id;
}
