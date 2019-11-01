package co.jp.wever.graphql.domain.service.datafetchers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.converter.plan.PlanBaseInputConverter;
import co.jp.wever.graphql.application.converter.plan.PlanElementInputConverter;
import co.jp.wever.graphql.application.converter.plan.PlanDetailResponseConverter;
import co.jp.wever.graphql.application.converter.plan.PlanListItemResponseConverter;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.application.datamodel.response.mutation.CreateResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateImageResponse;
import co.jp.wever.graphql.application.datamodel.response.mutation.UpdateResponse;
import co.jp.wever.graphql.application.datamodel.response.query.plan.PlanDetailResponse;
import co.jp.wever.graphql.domain.domainmodel.TokenVerifier;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanListItem;
import co.jp.wever.graphql.domain.service.plan.CreatePlanService;
import co.jp.wever.graphql.domain.service.plan.DeletePlanService;
import co.jp.wever.graphql.domain.service.plan.FindPlanService;
import co.jp.wever.graphql.domain.service.plan.UpdatePlanService;
import graphql.schema.DataFetcher;

@Component
public class PlanDataFetchers {

    private final TokenVerifier tokenVerifier;
    private final FindPlanService findPlanService;
    private final UpdatePlanService updatePlanService;
    private final DeletePlanService deletePlanService;
    private final CreatePlanService createPlanService;

    PlanDataFetchers(
        TokenVerifier tokenVerifier,
        FindPlanService findPlanService,
        UpdatePlanService updatePlanService,
        DeletePlanService deletePlanService,
        CreatePlanService createPlanService) {
        this.tokenVerifier = tokenVerifier;
        this.findPlanService = findPlanService;
        this.updatePlanService = updatePlanService;
        this.deletePlanService = deletePlanService;
        this.createPlanService = createPlanService;
    }

    public DataFetcher planDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");
            PlanDetailResponse p =
                PlanDetailResponseConverter.toPlanResponse(this.findPlanService.findPlan(planId, userId));
            return PlanDetailResponseConverter.toPlanResponse(this.findPlanService.findPlan(planId, userId));
        };
    }

    public DataFetcher allPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            return this.findPlanService.findAllPlan(userId)
                                       .stream()
                                       .map(p -> PlanListItemResponseConverter.toPlanListItemResponse(p))
                                       .collect(Collectors.toList());
        };
    }

    public DataFetcher allPublishedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //            return this.findPlanService.findAllPublishedPlan(userId)
            //                                       .stream()
            //                                       .map(p -> PlanDetailResponseConverter.toPlanResponse(p))
            //                                       .collect(Collectors.toList());
            return null;
        };
    }

    public DataFetcher allDraftedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            //            return this.findPlanService.findAllDraftedPlan(userId)
            //                                       .stream()
            //                                       .map(p -> PlanDetailResponseConverter.toPlanResponse(p))
            //                                       .collect(Collectors.toList());
            return null;
        };
    }

    public DataFetcher allLikedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //            return this.findPlanService.findAllLikedPlan(userId)
            //                                       .stream()
            //                                       .map(p -> PlanDetailResponseConverter.toPlanResponse(p))
            //                                       .collect(Collectors.toList());
            //
            return null;
        };
    }

    public DataFetcher allLearningPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //            return this.findPlanService.findAllLearningPlan(userId)
            //                                       .stream()
            //                                       .map(p -> PlanDetailResponseConverter.toPlanResponse(p))
            //                                       .collect(Collectors.toList());
            return null;
        };
    }

    public DataFetcher allLearnedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //            return this.findPlanService.findAllLearnedPlan(userId)
            //                                       .stream()
            //                                       .map(p -> PlanDetailResponseConverter.toPlanResponse(p))
            //                                       .collect(Collectors.toList());
            return null;
        };
    }

    public DataFetcher famousPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //TODO: 未実装
            //            return this.findPlanService.findAllLearnedPlan(userId)
            //                                       .stream()
            //                                       .map(p -> PlanDetailResponseConverter.toPlanResponse(p))
            //                                       .collect(Collectors.toList());
            return null;
        };
    }

    public DataFetcher relatedPlansDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            //TODO: 未実装
            //            return this.findPlanService.findAllLearnedPlan(userId)
            //                                       .stream()
            //                                       .map(p -> PlanDetailResponseConverter.toPlanResponse(p))
            //                                       .collect(Collectors.toList());

            return null;
        };
    }

    ///////////////////////////////
    ///////// Mutations   /////////
    ///////////////////////////////
    public DataFetcher initPlanDataFetcher() {
        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String createId = createPlanService.initPlan(userId);
            return CreateResponse.builder().id(createId).build();
        };
    }

    public DataFetcher createPlanBaseDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            Map<String, Object> planBaseMap = (Map) dataFetchingEnvironment.getArgument("planBase");

            String createId =
                createPlanService.createPlanBase(userId, PlanBaseInputConverter.toPlanBaseInput(planBaseMap));
            return CreateResponse.builder().id(createId).build();
        };
    }

    public DataFetcher updatePlanTitleDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String title = dataFetchingEnvironment.getArgument("title");
            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.updatePlanTitle(planId, userId, title);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updatePlanTagsDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String planId = dataFetchingEnvironment.getArgument("planId");
            List<String> tags = (List<String>) dataFetchingEnvironment.getArgument("tags");

            updatePlanService.updatePlanTags(planId, userId, tags);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updatePlanImageUrlDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String planId = dataFetchingEnvironment.getArgument("planId");
            String imageUrl = dataFetchingEnvironment.getArgument("url");

            updatePlanService.updatePlanImageUrl(planId, userId, imageUrl);
            return UpdateImageResponse.builder().url(imageUrl).build();
        };
    }

    public DataFetcher updatePlanDescriptionDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);
            String planId = dataFetchingEnvironment.getArgument("planId");
            String description = dataFetchingEnvironment.getArgument("description");

            updatePlanService.updatePlanDescription(planId, userId, description);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher updatePlanBaseDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");
            Map<String, Object> planBaseMap = (Map) dataFetchingEnvironment.getArgument("planBase");

            updatePlanService.updatePlanBase(planId, userId, PlanBaseInputConverter.toPlanBaseInput(planBaseMap));
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher savePlanDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");
            String description = dataFetchingEnvironment.getArgument("description");
            List<String> tags = (List<String>) dataFetchingEnvironment.getArgument("tags");
            List<Map<String, Object>> elementMaps =
                (List<Map<String, Object>>) dataFetchingEnvironment.getArgument("elements");
            List<PlanElementInput> elementInputs =
                elementMaps.stream().map(e -> PlanElementInputConverter.toPlanElement(e)).collect(Collectors.toList());

            updatePlanService.savePlan(planId, userId, elementInputs, description, tags);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher createPlanElementsDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");
            List<Map<String, Object>> elementMaps =
                (List<Map<String, Object>>) dataFetchingEnvironment.getArgument("elements");

            List<PlanElementInput> elementInputs =
                elementMaps.stream().map(e -> PlanElementInputConverter.toPlanElement(e)).collect(Collectors.toList());

            createPlanService.createPlanElements(planId, userId, elementInputs);
            return UpdateResponse.builder().build();
        };
    }

    // TODO: あとでやる
    public DataFetcher updatePlanElementsDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");

            List<Map<String, Object>> elementMaps =
                (List<Map<String, Object>>) dataFetchingEnvironment.getArgument("elements");

            List<PlanElementInput> elementInputs =
                elementMaps.stream().map(e -> PlanElementInputConverter.toPlanElement(e)).collect(Collectors.toList());

            updatePlanService.updatePlanElements(userId, planId, elementInputs);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher deletePlanDataFetcher() {

        return dataFetchingEnvironment -> {
            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");

            deletePlanService.deletePlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher publishPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.publishPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher draftPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.draftPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher startPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.startPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }

    public DataFetcher stopPlanDataFetcher() {
        return dataFetchingEnvironment -> {

            String token = (String) dataFetchingEnvironment.getContext();
            String userId = tokenVerifier.getUserId(token);

            String planId = dataFetchingEnvironment.getArgument("planId");

            updatePlanService.stopPlan(planId, userId);
            return UpdateResponse.builder().build();
        };
    }
}
