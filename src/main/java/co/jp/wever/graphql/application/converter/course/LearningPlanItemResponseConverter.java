package co.jp.wever.graphql.application.converter.course;

import co.jp.wever.graphql.application.datamodel.response.query.plan.LearningPlanItemResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanActionResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanBaseResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanStatisticsResponse;
import co.jp.wever.graphql.infrastructure.datamodel.plan.LearningPlanItemEntity;

public class LearningPlanItemResponseConverter {
    //TODO: Entityからの直接変換はやめる
    public static LearningPlanItemResponse toLearningPlanItemResponse(LearningPlanItemEntity learningPlanItemEntity) {
        return LearningPlanItemResponse.builder()
                                       .base(PlanBaseResponse.builder()
                                                             .id(learningPlanItemEntity.getBase().getId())
                                                             .imageUrl(learningPlanItemEntity.getBase().getImageUrl())
                                                             .title(learningPlanItemEntity.getBase().getTitle())
                                                             .description(learningPlanItemEntity.getBase()
                                                                                                .getDescription())
                                                             .build())
                                       .action(PlanActionResponse.builder()
                                                                 .allElementCount(learningPlanItemEntity.getAction()
                                                                                                        .getAllElementCount())
                                                                 .learnedElementCount(learningPlanItemEntity.getAction()
                                                                                                            .getLearnedElementCount())
                                                                 .build())
                                       .statistics(PlanStatisticsResponse.builder()
                                                                         .learning(learningPlanItemEntity.getStatistics()
                                                                                                         .getLearningCount())
                                                                         .learned(learningPlanItemEntity.getStatistics()
                                                                                                        .getLearnedCount())
                                                                         .like(learningPlanItemEntity.getStatistics()
                                                                                                     .getLikeCount())
                                                                         .build())
                                       .build();
    }
}
