package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

@Repository
public interface CreateSectionRepository {
    String addOne(String userId);
}
