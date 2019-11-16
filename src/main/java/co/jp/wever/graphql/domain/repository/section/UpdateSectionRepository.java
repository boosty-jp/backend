package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

@Repository
public interface UpdateSectionRepository {

    void updateOne(UpdateSection updateSection);

    void likeOne(String sectionId, String userId);

    void deleteLikeOne(String sectionId, String userId);
}

