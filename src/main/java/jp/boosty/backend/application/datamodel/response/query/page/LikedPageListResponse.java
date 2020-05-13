package jp.boosty.backend.application.datamodel.response.query.page;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikedPageListResponse {
    private List<LikedPageResponse> pages;
    private long sumCount;
}
