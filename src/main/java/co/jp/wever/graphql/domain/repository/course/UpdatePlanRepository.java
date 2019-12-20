package co.jp.wever.graphql.domain.repository.course;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.plan.DraftPlan;
import co.jp.wever.graphql.domain.domainmodel.plan.PublishPlan;

@Repository
public interface UpdatePlanRepository {

    void updateTitle(String planId, String title);

    void updateTags(String planId, List<String> tagIds);

    void updateImageUrl(String planId, String imageUrl);

    void updateDescription(String planId, String description);

    void publishOne(PublishPlan publishPlan, String userId);

    void draftOne(DraftPlan draftPlan, String userId);

    void startOne(String planId, String userId);

    void stopOne(String planId, String userId);

    void finishOne(String planId, String userId, List<String> elementIds);

    void likeOne(String planId, String userId);

    void deleteLikeOne(String planId, String userId);
}
