package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;

@Repository
public interface UpdatePlanRepository {

    void updateTitle(String planId, String title);

    void updateTags(String planId, String userId, List<String> tagIds);

    void updateImageUrl(String planId, String imageUrl);

    void updateDescription(String planId, String description);

    void updateBase(String planId, PlanBaseEntity planBaseEntity);

    void updateElements(String planId, List<PlanElementEntity> planElementEntities);

    void publishOne(String planId, String userId);

    void draftOne(String planId, String userId);

    void saveOne(
        String planId,
        String userId,
        List<PlanElementInput> elementInputs,
        String description,
        List<String> tags);

    void startOne(String planId, String userId);

    void stopOne(String planId, String userId);

    void finishOne(String planId, String userId);
}
