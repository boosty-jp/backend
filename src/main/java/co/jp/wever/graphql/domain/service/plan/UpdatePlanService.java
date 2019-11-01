package co.jp.wever.graphql.domain.service.plan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.plan.PlanDetailConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.PlanDetail;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanDescription;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanImageUrl;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.plan.aggregation.PlanDetailEntity;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.plan.UpdatePlanRepositoryImpl;

@Service
public class UpdatePlanService {

    private final FindPlanRepositoryImpl findPlanRepository;
    private final UpdatePlanRepositoryImpl updatePlanRepository;

    UpdatePlanService(UpdatePlanRepositoryImpl updatePlanRepository, FindPlanRepositoryImpl findPlanRepository) {
        this.updatePlanRepository = updatePlanRepository;
        this.findPlanRepository = findPlanRepository;
    }

    public void updatePlanTitle(String planId, String userId, String title) {

        // TODO: クエリが重いのでなおす
        PlanDetailEntity planDetailEntity = findPlanRepository.findOne(planId);
        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);

        UserId updaterId = UserId.of(userId);
        if (!planDetail.canUpdate(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        updatePlanRepository.updateTitle(planId, title);
    }

    public void updatePlanTags(String planId, String userId, List<String> tags) {

        // TODO: クエリが重いのでなおす
        PlanDetailEntity planDetailEntity = findPlanRepository.findOne(planId);
        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);

        UserId updaterId = UserId.of(userId);
        if (!planDetail.canUpdate(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        // TODO: ドメインに移動させる
        if (tags.size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        updatePlanRepository.updateTags(planId, userId, tags);
    }

    public void updatePlanImageUrl(String planId, String userId, String imageUrl) {

        // TODO: クエリが重いのでなおす
        PlanDetailEntity planDetailEntity = findPlanRepository.findOne(planId);
        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);

        UserId updaterId = UserId.of(userId);
        if (!planDetail.canUpdate(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        //TODO: ドメインに閉じ込めたい
        PlanImageUrl.of(imageUrl);

        updatePlanRepository.updateImageUrl(planId, imageUrl);
    }

    public void updatePlanDescription(String planId, String userId, String description) {

        // TODO: クエリが重いのでなおす
        PlanDetailEntity planDetailEntity = findPlanRepository.findOne(planId);
        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);

        UserId updaterId = UserId.of(userId);
        if (!planDetail.canUpdate(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        //TODO: ドメインに閉じ込めたい
        PlanDescription.of(description);

        updatePlanRepository.updateDescription(planId, description);
    }

    public void savePlan(
        String planId, String userId, List<PlanElementInput> elementInputs, String description, List<String> tags) {

        // TODO: ドメイン移行
        if (tags.size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        if (elementInputs.size() > 30) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        // TODO: チェックするようにする
//        List<String> targetIds = elementInputs.stream().map(e -> e.getTargetId()).collect(Collectors.toList());
//        // 追加するエレメントが公開されているかチェック
//        List<String> publishedPlanElementIds = findPlanRepository.findPublishedPlanElementIds(targetIds);
//
//        // TODO: ドメインサービスに移す
//        if (!publishedPlanElementIds.stream().allMatch(id -> targetIds.contains(id))) {
//            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
//                                             GraphQLErrorMessage.INVALID_PLAN_ELEMENT_ID.getString());
//        }
//
//        if (!targetIds.stream().allMatch(id -> publishedPlanElementIds.contains(id))) {
//            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
//                                             GraphQLErrorMessage.INVALID_PLAN_ELEMENT_ID.getString());
//        }

//        // TODO: クエリが重いのでなおす
//        PlanDetailEntity planDetailEntity = findPlanRepository.findOne(planId);
//        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);
//
//        UserId updaterId = UserId.of(userId);
//
//        if (!planDetail.canUpdate(updaterId)) {
//            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
//                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
//        }

        updatePlanRepository.saveOne(planId, userId, elementInputs, description, tags);
    }

    public void updatePlanBase(String planId, String userId, PlanBaseInput planBaseInput) {

        // TODO: クエリが重いのでなおす
        PlanDetailEntity planDetailEntity = findPlanRepository.findOne(planId);
        PlanDetail planDetail = PlanDetailConverter.toPlanDetail(planDetailEntity);

        UserId updaterId = UserId.of(userId);
        if (planDetail.canUpdate(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        //        PlanBase updatePlanBase = PlanBase.of(planBaseInput.getTitle(),
        //                                              planBaseInput.getDescription(),
        //                                              planBaseInput.getImageUrl(),
        //                                              planBaseInput.getTags(),
        //                                              userId,
        //                                              planDetailEntity.getBase().getStatus());
        //        PlanBaseEntity planBaseEntity = PlanBaseEntityConverter.toPlanBaseEntity(updatePlanBase);
        //
        //        updatePlanRepository.updateBase(planId, planBaseEntity);
    }

    public void updatePlanElements(String planId, String userId, List<PlanElementInput> elements) {

        // TODO: 基本的にcreateの方と同じなので、createができたのを使い回す
        // Repositoryの操作だけ違う
        // すでに作成したプランの連携を切らなければならない。
        updatePlanRepository.updateElements(planId, null);
    }

    public void publishPlan(String planId, String userId) {
        //
        //        PlanEntity targetPlanEntity = findPlanRepository.findOne(planId);
        //        Plan plan = PlanConverter.toPlan(targetPlanEntity);
        //
        //        UserId publisherId = UserId.of(userId);
        //        if (!publisherId.same(plan.getAuthorId())) {
        //            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
        //                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        //        }

        // 公開できるかチェック
        // プランの基本チェック
        // エレメントのチェック
        //        updatePlanRepository.publishOne(planId, userId);
    }

    public void draftPlan(String planId, String userId) {
        //        PlanEntity targetPlanEntity = findPlanRepository.findOne(planId);
        //
        //        Plan plan = PlanConverter.toPlan(targetPlanEntity);
        //        UserId draftUserId = UserId.of(userId);
        //        if (!draftUserId.same(plan.getAuthorId())) {
        //            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
        //                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        //        }

        // TODO:下書き化できるかチェック
        // 対象のユーザーのプランかどうか
        //        this.updatePlanRepository.draftOne(planId, userId);
    }

    public void startPlan(String planId, String userId) {
        //TODO:すでに終わっていないかプランじゃないかチェックする
        //        this.updatePlanRepository.startOne(planId, userId);
    }

    public void stopPlan(String planId, String userId) {
        //        this.updatePlanRepository.stopOne(planId, userId);
    }
}
