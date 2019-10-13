package co.jp.wever.graphql.infrastructure.repository;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.datamodel.Plan;

@Repository
public interface  PlanRepository {
    //Query
    Plan findOne(String id);
    Plan findAll(String id);
    Plan findAllPublished(String id);
    Plan findAllDrafted(String id);
    Plan findAllLiked(String id);
    Plan findAllLearning(String id);
    Plan findAllLearned(String id);
    Plan findFamous(String id);
    Plan findRelated(String id);

    //Mutation
    Plan initOne(String id);
    Plan addOne(String id);
    Plan updateOne(String id);
    Plan deleteOne(String id);
    Plan publishOne(String id);
    Plan draftOne(String id);
    Plan startOne(String id);
}
