package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.PlanElementInputConverter;
import co.jp.wever.graphql.application.converter.PlanResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.ErrorResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import co.jp.wever.graphql.domain.service.PlanService;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;
import graphql.schema.DataFetcher;

@Component
public class PlanDataFetchers {

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////

    private final PlanService planService;

    PlanDataFetchers(PlanService planService) {
        this.planService = planService;
    }

    public DataFetcher planDataFetcher() {
        return dataFetchingEnvironment -> {
            String planId = dataFetchingEnvironment.getArgument("planId");
            String userId = dataFetchingEnvironment.getArgument("userId");
            return PlanResponseConverter.toPlanResponse(this.planService.findPlan(planId, userId));
        };
    }

    public DataFetcher allPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planService.findAllPlan(userId).stream().map(p -> PlanResponseConverter.toPlanResponse(p)).collect(Collectors.toList());
        };
    }

    public DataFetcher allPublishedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planService.findAllPublishedPlan(userId).stream().map(p -> PlanResponseConverter.toPlanResponse(p)).collect(Collectors.toList());
        };
    }

    public DataFetcher allDraftedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planService.findAllDraftedPlan(userId).stream().map(p -> PlanResponseConverter.toPlanResponse(p)).collect(Collectors.toList());
        };
    }

    public DataFetcher allLikedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planService.findAllLikedPlan(userId).stream().map(p -> PlanResponseConverter.toPlanResponse(p)).collect(Collectors.toList());
        };
    }

    public DataFetcher allLearningPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planService.findAllLearningPlan(userId).stream().map(p -> PlanResponseConverter.toPlanResponse(p)).collect(Collectors.toList());
        };
    }

    public DataFetcher allLearnedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planService.findAllLearnedPlan(userId).stream().map(p -> PlanResponseConverter.toPlanResponse(p)).collect(Collectors.toList());
        };
    }

    public DataFetcher famousPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //TODO: 未実装
            return this.planService.findAllLearnedPlan(userId).stream().map(p -> PlanResponseConverter.toPlanResponse(p)).collect(Collectors.toList());
        };
    }

    public DataFetcher relatedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //TODO: 未実装
            return this.planService.findAllLearnedPlan(userId).stream().map(p -> PlanResponseConverter.toPlanResponse(p)).collect(Collectors.toList());
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////

    public DataFetcher initPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return planService.initPlan(userId);
        };
    }

    public DataFetcher addPlanElementDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");
            Map<String, Object> elementMap = (Map) dataFetchingEnvironment.getArgument("element");

            String createId = planService.addPlanElement(planId, userId, PlanElementInputConverter.toPlanElement(elementMap));
            return CreateResponse.builder().id(createId).build();
        };
    }

    // TODO: あとでやる
    public DataFetcher updatePlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String title = dataFetchingEnvironment.getArgument("title");
            String planId = dataFetchingEnvironment.getArgument("planId");
            return UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
        };
    }

    public DataFetcher deletePlanDataFetcher() {

        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");

            planService.deletePlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher publishPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");

            planService.publishPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher draftPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");

            planService.draftPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher startPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");

            planService.startPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }
}
