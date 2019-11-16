package co.jp.wever.graphql.domain.service.plan;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.infrastructure.repository.plan.CreatePlanRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;

@Service
public class CreatePlanService {
    private final CreatePlanRepositoryImpl createPlanRepository;
    private final FindPlanRepositoryImpl findPlanRepository;

    CreatePlanService(CreatePlanRepositoryImpl createPlanRepository, FindPlanRepositoryImpl findPlanRepository) {
        this.createPlanRepository = createPlanRepository;
        this.findPlanRepository = findPlanRepository;
    }

    public String initPlan(Requester requester) {
        return createPlanRepository.initOne(requester.getUserId());
    }

}
