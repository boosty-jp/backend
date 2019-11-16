package co.jp.wever.graphql.domain.repository.tag;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.tag.TagStatisticEntity;

@Repository
public interface FindTagRepository {
    boolean exists(String name);

    List<TagStatisticEntity> famousTags();
}
