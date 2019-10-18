package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.SectionEntity;


@Repository
public interface FindSectionRepository {
    SectionEntity findOne(String id);

    List<SectionEntity> findAllLiked(String id);

    List<SectionEntity> findAllBookmarked(String id);

    List<SectionEntity> findFamous(String id);

    List<SectionEntity> findRelated(String id);

}
