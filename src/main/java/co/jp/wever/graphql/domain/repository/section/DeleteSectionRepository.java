package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

@Repository
public interface DeleteSectionRepository {
    void deleteOne(String sectionId, String userId);
}
