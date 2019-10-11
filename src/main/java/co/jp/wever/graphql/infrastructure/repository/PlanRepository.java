package co.jp.wever.graphql.infrastructure.repository;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.datamodel.Plan;

@Repository
public interface  PlanRepository {
    Plan findOne(String id);
}
