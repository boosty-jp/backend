package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;

@Repository
public interface DeleteSectionRepository {
    void deleteOne(String articleId, String sectionId, String userId, List<SectionNumberEntity> decrementIds);
}
