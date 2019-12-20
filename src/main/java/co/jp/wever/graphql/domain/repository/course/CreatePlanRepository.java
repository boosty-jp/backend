package co.jp.wever.graphql.domain.repository.course;

import org.springframework.stereotype.Repository;

@Repository
public interface CreatePlanRepository {
    String initOne(String userId);
}
