package jp.boosty.graphql.domain.repository.tag;

import org.springframework.stereotype.Repository;

import java.util.List;

import jp.boosty.graphql.infrastructure.datamodel.tag.TagEntity;

@Repository
public interface TagQueryRepository {
    List<TagEntity> famousTags();
}
