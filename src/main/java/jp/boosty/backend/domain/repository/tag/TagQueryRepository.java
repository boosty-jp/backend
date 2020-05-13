package jp.boosty.backend.domain.repository.tag;

import org.springframework.stereotype.Repository;

import java.util.List;

import jp.boosty.backend.infrastructure.datamodel.tag.TagEntity;

@Repository
public interface TagQueryRepository {
    List<TagEntity> famousTags();
}
