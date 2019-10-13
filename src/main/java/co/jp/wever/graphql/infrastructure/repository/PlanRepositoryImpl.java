package co.jp.wever.graphql.infrastructure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.PlanRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.datamodel.Plan;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PlanRepositoryImpl implements PlanRepository {

    @Autowired
    private final NeptuneClient neptuneClient;

    @Override
    public Plan findOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAll(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllPublished(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllDrafted(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllLiked(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllLearning(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findAllLearned(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findFamous(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan findRelated(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan initOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan addOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan updateOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan deleteOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan publishOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan draftOne(String id) {
        return Plan.builder().build();
    }

    @Override
    public Plan startOne(String id) {
        return Plan.builder().build();
    }
}
