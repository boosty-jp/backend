package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.plan.PlanBaseInputConverter;
import co.jp.wever.graphql.application.converter.plan.PlanElementInputsConverter;
import co.jp.wever.graphql.application.converter.plan.PlanDetailResponseConverter;
import co.jp.wever.graphql.application.converter.plan.PlanListItemResponseConverter;
import co.jp.wever.graphql.application.converter.requester.RequesterConverter;
import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateImageResponse;
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
    private final RequesterConverter requesterConverter;

    PlanDataFetchers(
        FindPlanService findPlanService,
        UpdatePlanService updatePlanService,
        DeletePlanService deletePlanService,
        CreatePlanService createPlanService,
        RequesterConverter requesterConverter) {
        this.findPlanService = findPlanService;
        this.updatePlanService = updatePlanService;
        this.deletePlanService = deletePlanService;
        this.createPlanService = createPlanService;
        this.requesterConverter = requesterConverter;
    }

    public DataFetcher planDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String planId = dataFetchingEnvironment.getArgument("planId");

            return PlanDetailResponseConverter.toPlanResponse(this.findPlanService.findPlan(planId, requester));
        };
    }

    public DataFetcher allPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            return this.findPlanService.findAllPlan(requester)
                                       .stream()
                                       .map(p -> PlanListItemResponseConverter.toPlanListItemResponse(p))
                                       .collect(Collectors.toList());
        };
    }

    public DataFetcher allPublishedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher allDraftedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher allLikedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher allLearningPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher allLearnedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher famousPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    public DataFetcher relatedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            return null;
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher initPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String planId = createPlanService.initPlan(requester);

            return CreateResponse.builder().id(planId).build();
        };
    }

    public DataFetcher updatePlanTitleDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String title = dataFetchingEnvironment.getArgument("title");
            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.updatePlanTitle(planId, title, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updatePlanTagsDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            String planId = dataFetchingEnvironment.getArgument("planId");
            List<String> tags = dataFetchingEnvironment.getArgument("tags");

            updatePlanService.updatePlanTags(planId, tags, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updatePlanImageUrlDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String planId = dataFetchingEnvironment.getArgument("planId");
            String imageUrl = dataFetchingEnvironment.getArgument("url");

            updatePlanService.updatePlanImageUrl(planId, imageUrl, requester);
            return UpdateImageResponse.builder().url(imageUrl).build();
        };
    }

    public DataFetcher updatePlanDescriptionDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String planId = dataFetchingEnvironment.getArgument("planId");
            String description = dataFetchingEnvironment.getArgument("description");

            updatePlanService.updatePlanDescription(planId, description, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deletePlanDataFetcher() {

        return dataFetchingEnvironment -> {
            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String planId = dataFetchingEnvironment.getArgument("planId");

            deletePlanService.deletePlan(planId, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher publishPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            PlanBaseInput baseInput = PlanBaseInputConverter.toPlanBaseInput(dataFetchingEnvironment);
            List<PlanElementInput> elementsInput =
                PlanElementInputsConverter.toPlanElementsInput(dataFetchingEnvironment);

            updatePlanService.publishPlan(baseInput, elementsInput, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher draftPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);
            PlanBaseInput baseInput = PlanBaseInputConverter.toPlanBaseInput(dataFetchingEnvironment);
            List<PlanElementInput> elementsInput =
                PlanElementInputsConverter.toPlanElementsInput(dataFetchingEnvironment);

            updatePlanService.draftPlan(baseInput, elementsInput, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher startPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.startPlan(planId, requester);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher stopPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            Requester requester = requesterConverter.toRequester(dataFetchingEnvironment);

            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.stopPlan(planId, requester);
            return UpdateResponse.builder().build();
        };
    }
}
