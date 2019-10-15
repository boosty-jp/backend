package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;


import co.jp.wever.graphql.application.datamodel.request.PlanElementRequest;
import co.jp.wever.graphql.application.datamodel.response.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.ErrorResponse;
import co.jp.wever.graphql.application.datamodel.response.UpdateResponse;
import co.jp.wever.graphql.domain.service.PlanService;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;
import graphql.schema.DataFetcher;

@Component
public class PlanDataFetchers {

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////

    // TODO: あとでけす
    private final PlanRepositoryImpl planRepository;

    private final PlanService planService;

    PlanDataFetchers(PlanRepositoryImpl planRepository, PlanService planService) {
        this.planRepository = planRepository;
        this.planService = planService;
    }

    public DataFetcher planDataFetcher() {
        return dataFetchingEnvironment -> {
            String planId = dataFetchingEnvironment.getArgument("planId");
            return this.planRepository.findOne(planId);
        };
    }

    public DataFetcher allPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planRepository.findAll(userId);
        };
    }

    public DataFetcher allPublishedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planRepository.findAllPublished(userId);
        };
    }

    public DataFetcher allDraftedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planRepository.findAllDrafted(userId);
        };
    }

    public DataFetcher allLikedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planRepository.findAllLiked(userId);
        };
    }

    public DataFetcher allLearningPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planRepository.findAllLearning(userId);
        };
    }

    public DataFetcher allLearnedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planRepository.findAllLearned(userId);
        };
    }

    public DataFetcher famousPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planRepository.findFamous(userId);
        };
    }

    public DataFetcher relatedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return this.planRepository.findRelated(userId);
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
            OffersDto inputObject = objectMapper.convertValue(dataFetchingEnvironment.getArgument("element"), PlanElementRequest.class);
            System.out.println(element.get);
            String createId = planService.addPlanElement(planId, userId, element);
            return CreateResponse.builder().id("").build();
        };
    }

    public DataFetcher updatePlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            String title = dataFetchingEnvironment.getArgument("title");
            String planId = dataFetchingEnvironment.getArgument("planId");
            this.planRepository.updateOne(planId, title, userId);
            return UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
        };
    }

    public DataFetcher deletePlanDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher publishPlanDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher draftPlanDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher startPlanDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }
}
