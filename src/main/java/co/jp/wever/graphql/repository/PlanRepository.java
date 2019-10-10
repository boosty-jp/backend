package co.jp.wever.graphql.repository;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.model.Plan;

@Repository
public interface  PlanRepository {
    Plan findOne(String id);
}
