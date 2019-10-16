package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.PlanBaseInputConverter;
import co.jp.wever.graphql.application.converter.PlanBaseResponseConverter;
import co.jp.wever.graphql.application.converter.PlanResponseConverter;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.ErrorResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import co.jp.wever.graphql.domain.service.plan.CreatePlanService;
import co.jp.wever.graphql.domain.service.plan.DeletePlanService;
import co.jp.wever.graphql.domain.service.plan.FindPlanService;
import co.jp.wever.graphql.domain.service.plan.UpdatePlanService;
import graphql.schema.DataFetcher;

@Component
public class PlanDataFetchers {

    private final FindPlanService findPlanService;
    private final UpdatePlanService updatePlanService;
    private final DeletePlanService deletePlanService;
    private final CreatePlanService createPlanService;

    PlanDataFetchers(
        FindPlanService findPlanService,
        UpdatePlanService updatePlanService,
        DeletePlanService deletePlanService,
        CreatePlanService createPlanService) {
        this.findPlanService = findPlanService;
        this.updatePlanService = updatePlanService;
        this.deletePlanService = deletePlanService;
        this.createPlanService = createPlanService;
    }

    public DataFetcher planDataFetcher() {
        return dataFetchingEnvironment -> {
            String planId = dataFetchingEnvironment.getArgument("planId");
            String userId = dataFetchingEnvironment.getArgument("userId");
            return PlanResponseConverter.toPlanResponse(this.findPlanService.findPlan(planId, userId));
        };
    }

    public DataFetcher allPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.findPlanService.findAllPlan(userId)
                                   .stream()
                                   .map(p -> PlanBaseResponseConverter.toPlanResponse(p))
                                   .collect(Collectors.toList());
        };
    }

    public DataFetcher allPublishedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.findPlanService.findAllPublishedPlan(userId)
                                   .stream()
                                   .map(p -> PlanBaseResponseConverter.toPlanResponse(p))
                                   .collect(Collectors.toList());
        };
    }

    public DataFetcher allDraftedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.findPlanService.findAllDraftedPlan(userId)
                                   .stream()
                                   .map(p -> PlanBaseResponseConverter.toPlanResponse(p))
                                   .collect(Collectors.toList());
        };
    }

    public DataFetcher allLikedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.findPlanService.findAllLikedPlan(userId)
                                   .stream()
                                   .map(p -> PlanBaseResponseConverter.toPlanResponse(p))
                                   .collect(Collectors.toList());
        };
    }

    public DataFetcher allLearningPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.findPlanService.findAllLearningPlan(userId)
                                   .stream()
                                   .map(p -> PlanBaseResponseConverter.toPlanResponse(p))
                                   .collect(Collectors.toList());
        };
    }

    public DataFetcher allLearnedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.findPlanService.findAllLearnedPlan(userId)
                                   .stream()
                                   .map(p -> PlanBaseResponseConverter.toPlanResponse(p))
                                   .collect(Collectors.toList());
        };
    }

    public DataFetcher famousPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //TODO: 未実装
            return this.findPlanService.findAllLearnedPlan(userId)
                                   .stream()
                                   .map(p -> PlanBaseResponseConverter.toPlanResponse(p))
                                   .collect(Collectors.toList());
        };
    }

    public DataFetcher relatedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //TODO: 未実装
            return this.findPlanService.findAllLearnedPlan(userId)
                                   .stream()
                                   .map(p -> PlanBaseResponseConverter.toPlanResponse(p))
                                   .collect(Collectors.toList());
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher createPlanBaseDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            Map<String, Object> planBaseMap = (Map) dataFetchingEnvironment.getArgument("planBase");

            String createId = createPlanService.createPlanBase(userId, PlanBaseInputConverter.toPlanBaseInput(planBaseMap));
            return CreateResponse.builder().id(createId).build();
        };
    }

    public DataFetcher updatePlanBaseDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");
            Map<String, Object> planBaseMap = (Map) dataFetchingEnvironment.getArgument("planBase");

            updatePlanService.updatePlanBase(planId, userId, PlanBaseInputConverter.toPlanBaseInput(planBaseMap));
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher createPlanElementsDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");
            Map<String, Object> elementMap = (Map) dataFetchingEnvironment.getArgument("element");

            // TODO: elementMapの型を確認して、Listに変換する
            createPlanService.createPlanElements(planId, userId, null);
            return CreateResponse.builder().id().build();
        };
    }

    // TODO: あとでやる
    public DataFetcher updatePlanElementsDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String title = dataFetchingEnvironment.getArgument("title");
            String planId = dataFetchingEnvironment.getArgument("planId");
            return UpdateResponse.builder()
                                 .error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
        };
    }

    public DataFetcher deletePlanDataFetcher() {

        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");

            deletePlanService.deletePlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher publishPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.publishPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher draftPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.draftPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher startPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.startPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }
}
