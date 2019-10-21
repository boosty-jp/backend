package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

@Repository
public interface UpdateSectionRepository {

    void updateOne(SectionEntity sectionEntity);

    void likeOne(String sectionId, String userId);
}

