package jp.boosty.backend.infrastructure.datamodel.page;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikedPageListEntity {
    private List<LikedPageEntity> likedPages;
    private long sumCount;
}
