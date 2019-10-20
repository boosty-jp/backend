package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

@Repository
public interface CreateSectionRepository {
    String addOne(String userId, SectionEntity sectionEntity);
}
