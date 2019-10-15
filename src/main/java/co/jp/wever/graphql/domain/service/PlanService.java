package co.jp.wever.graphql.domain.service;

import org.springframework.stereotype.Service;

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

    public String addPlanElement(String planId, String userId, Object element) {

        // 対象のエレメント情報を取得
        this.planRepositoryImpl.findOne(planId);

        //ドメインモデルへの変換


        // 公開されている(削除ずみ、下書き中ではない)エレメントデータかどうかチェック

        // 対象のプランを取得
        // 追加できるかチェック
        // 順番の不整合がないか
        // 追加できる数を超えていないか

        // 追加する
        return this.planRepositoryImpl.addOne(userId);
    }
}
