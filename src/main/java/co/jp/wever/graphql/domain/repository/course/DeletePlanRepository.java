package co.jp.wever.graphql.domain.repository.course;

import org.springframework.stereotype.Repository;

@Repository
public interface DeletePlanRepository {
    void deleteOne(String planId, String userId);
}
