package co.jp.wever.graphql.domain.service;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.domain.converter.PlanElementConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.Plan;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.converter.PlanConverter;
import co.jp.wever.graphql.infrastructure.datamodel.PlanEntity;
import co.jp.wever.graphql.infrastructure.repository.PlanRepositoryImpl;

@Service
public class PlanService {

    private final PlanRepositoryImpl planRepositoryImpl;

    PlanService(PlanRepositoryImpl planRepositoryImpl) {
        this.planRepositoryImpl = planRepositoryImpl;
    }

    public String initPlan(String userId) {
        return this.planRepositoryImpl.initOne(userId);
    }

    public String addPlanElement(String planId, String userId, PlanElementInput elementInput) {

        // 対象のエレメント情報を取得
        // 公開されている(削除ずみ、下書き中ではない)エレメントデータかどうかチェック
        if(!this.planRepositoryImpl.isPublishedPlanElement(planId)){
            // TODO: Exception検討する
            throw new IllegalArgumentException();
        }

        // 対象のプランを取得
        PlanEntity targetPlanEntity = this.planRepositoryImpl.findOne(planId);

        //ドメインモデルへの変換
        Plan plan = PlanConverter.toPlan(targetPlanEntity);

        // エレメントへ変換
        PlanElement planElement = PlanElementConverter.toPlanElement(elementInput);

        // 追加できるかチェック
        // 順番の不整合がないか
        // 追加できる数を超えていないか
        if(!plan.canAddElement(planElement)){
            // TODO: Exception検討する
            throw new IllegalArgumentException();
        }

        // 追加する
        return this.planRepositoryImpl.addOne(planId, userId, null);
    }
}
