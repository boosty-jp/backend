package co.jp.wever.graphql.domain.repository.plan;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.domainmodel.plan.DraftPlan;
import co.jp.wever.graphql.domain.domainmodel.plan.PublishPlan;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanElementEntity;

@Repository
public interface UpdatePlanRepository {

    void updateTitle(String planId, String title);

    void updateTags(String planId, List<String> tagIds);

    void updateImageUrl(String planId, String imageUrl);

    void updateDescription(String planId, String description);

    void updateElements(String planId, List<PlanElementEntity> planElementEntities);

    void publishOne(PublishPlan publishPlan, String userId);

    void draftOne(DraftPlan draftPlan, String userId);

    void startOne(String planId, String userId);

    void stopOne(String planId, String userId);

    void finishOne(String planId, String userId);
}
