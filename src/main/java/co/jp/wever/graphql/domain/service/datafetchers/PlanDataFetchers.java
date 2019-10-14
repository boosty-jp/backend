package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import co.jp.wever.graphql.application.datamodel.CreateResponse;
import co.jp.wever.graphql.application.datamodel.ErrorResponse;
import co.jp.wever.graphql.infrastructure.datamodel.Plan;
import co.jp.wever.graphql.application.datamodel.UpdateResponse;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;
import graphql.schema.DataFetcher;

@Component
public class PlanDataFetchers {

    ///////////////////////////////
    //////////// Query ////////////
    ///////////////////////////////
    private final PlanRepositoryImpl planRepository;

    PlanDataFetchers(PlanRepositoryImpl planRepository){
        this.planRepository = planRepository;
    }

    public DataFetcher planDataFetcher() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            this.planRepository.findOne(id);
            // TODO マッピング処理
            return Plan.builder().id(1).name("hoge").price(100).deleted(false).publish(false).description("des").image("iamge").build();
        };
    }

    public DataFetcher allPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Plan> plans = new ArrayList<>();
            return plans;
        };
    }

    public DataFetcher allPublishedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Plan> plans = new ArrayList<>();
            return plans;
        };
    }

    public DataFetcher allDraftedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Plan> plans = new ArrayList<>();
            return plans;
        };
    }

    public DataFetcher allLikedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Plan> plans = new ArrayList<>();
            return plans;
        };
    }

    public DataFetcher allLearningPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Plan> plans = new ArrayList<>();
            return plans;
        };
    }

    public DataFetcher allLearnedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Plan> plans = new ArrayList<>();
            return plans;
        };
    }

    public DataFetcher famousPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Plan> plans = new ArrayList<>();
            return plans;
        };
    }

    public DataFetcher relatedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            List<Plan> plans = new ArrayList<>();
            return plans;
        };
    }


    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////

    public DataFetcher initPlanDataFetcher() {
        return dataFetchingEnvironment -> CreateResponse.builder().id("1").error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher addPlansElementDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
    }

    public DataFetcher updatePlanDataFetcher() {
        return dataFetchingEnvironment -> UpdateResponse.builder().error(ErrorResponse.builder().errorCode("code").errorMessage("error").build());
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
