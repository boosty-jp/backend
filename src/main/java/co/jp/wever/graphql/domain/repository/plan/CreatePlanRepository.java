package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

@Repository
public interface CreatePlanRepository {
    String initOne(String userId);
}
