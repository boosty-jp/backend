package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

@Repository
public interface UpdateSectionRepository {

    void updateOne(String sectionId);

    void bookmarkOne(String sectionId, String userId);

    void likeOne(String sectionId, String userId);
}

