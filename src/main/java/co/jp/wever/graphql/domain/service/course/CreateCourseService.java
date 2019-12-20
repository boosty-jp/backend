package co.jp.wever.graphql.domain.service.course;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.infrastructure.repository.course.CreatePlanRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.course.FindPlanRepositoryImpl;

@Service
public class CreateCourseService {
    private final CreatePlanRepositoryImpl createPlanRepository;
    private final FindPlanRepositoryImpl findPlanRepository;

    CreateCourseService(CreatePlanRepositoryImpl createPlanRepository, FindPlanRepositoryImpl findPlanRepository) {
        this.createPlanRepository = createPlanRepository;
        this.findPlanRepository = findPlanRepository;
    }

    public String initPlan(Requester requester) {
        return createPlanRepository.initOne(requester.getUserId());
    }

}
