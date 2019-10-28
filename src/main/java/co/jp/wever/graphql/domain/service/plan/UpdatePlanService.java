package co.jp.wever.graphql.domain.service.plan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.plan.PlanConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.converter.entity.plan.PlanBaseEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanBaseEntity;
import co.jp.wever.graphql.infrastructure.datamodel.plan.PlanEntity;
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

    public void updatePlanBase(String planId, String userId, PlanBaseInput planBaseInput) {

        // 対象のユーザーのプランかチェック
        PlanEntity planEntity = findPlanRepository.findOne(planId);
        Plan plan = PlanConverter.toPlan(planEntity);


        UserId updaterId = UserId.of(userId);
        if (!updaterId.same(plan.getAuthorId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        PlanBase updatePlanBase = PlanBase.of(planBaseInput.getTitle(),
                                              planBaseInput.getDescription(),
                                              planBaseInput.getImageUrl(),
                                              planBaseInput.getTags(),
                                              userId,
                                              planEntity.getBaseEntity().getStatus());
        PlanBaseEntity planBaseEntity = PlanBaseEntityConverter.toPlanBaseEntity(updatePlanBase);

        updatePlanRepository.updateBase(planId, planBaseEntity);
    }

    public void updatePlanElements(String planId, String userId, List<PlanElementInput> elements) {

        // TODO: 基本的にcreateの方と同じなので、createができたのを使い回す
        // Repositoryの操作だけ違う
        // すでに作成したプランの連携を切らなければならない。
        updatePlanRepository.updateElements(planId, null);
    }

    public void publishPlan(String planId, String userId) {

        PlanEntity targetPlanEntity = findPlanRepository.findOne(planId);
        Plan plan = PlanConverter.toPlan(targetPlanEntity);

        UserId publisherId = UserId.of(userId);
        if (!publisherId.same(plan.getAuthorId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        // 公開できるかチェック
        // プランの基本チェック
        // エレメントのチェック
        updatePlanRepository.publishOne(planId, userId);
    }

    public void draftPlan(String planId, String userId) {
        PlanEntity targetPlanEntity = findPlanRepository.findOne(planId);

        Plan plan = PlanConverter.toPlan(targetPlanEntity);
        UserId draftUserId = UserId.of(userId);
        if (!draftUserId.same(plan.getAuthorId())) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        // TODO:下書き化できるかチェック
        // 対象のユーザーのプランかどうか
        this.updatePlanRepository.draftOne(planId, userId);
    }

    public void startPlan(String planId, String userId) {
        //TODO:すでに終わっていないかプランじゃないかチェックする
        this.updatePlanRepository.startOne(planId, userId);
    }

    public void stopPlan(String planId, String userId) {
        this.updatePlanRepository.stopOne(planId, userId);
    }
}
