package co.jp.wever.graphql.domain.repository.tag;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;

@Repository
public interface FindTagRepository {
    List<TagEntity> famousTags();
}
