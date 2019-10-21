package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeleteSectionRepository {
    void deleteOne(String sectionId, String userId, List<String> decrementIds);
}
