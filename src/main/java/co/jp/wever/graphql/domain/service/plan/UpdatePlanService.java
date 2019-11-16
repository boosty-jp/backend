package co.jp.wever.graphql.domain.service.plan;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.PlanBaseInput;
import co.jp.wever.graphql.application.datamodel.request.PlanElementInput;
import co.jp.wever.graphql.application.datamodel.request.Requester;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.plan.DraftPlanConverter;
import co.jp.wever.graphql.domain.converter.plan.PublishPlanConverter;
import co.jp.wever.graphql.domain.domainmodel.plan.DraftPlan;
import co.jp.wever.graphql.domain.domainmodel.plan.PublishPlan;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanDescription;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanImageUrl;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.repository.plan.FindPlanRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.plan.UpdatePlanRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UpdatePlanService {

    private final FindPlanRepositoryImpl findPlanRepository;
    private final UpdatePlanRepositoryImpl updatePlanRepository;

    UpdatePlanService(UpdatePlanRepositoryImpl updatePlanRepository, FindPlanRepositoryImpl findPlanRepository) {
        this.updatePlanRepository = updatePlanRepository;
        this.findPlanRepository = findPlanRepository;
    }

    public void updatePlanTitle(String planId, String title, Requester requester) {
        log.info("update planId: {}", planId);
        log.info("update plan title: {}", title);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findPlanRepository.findAuthorId(planId));
        UserId updaterId = UserId.of(requester.getUserId());

        if (!authorId.same(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        updatePlanRepository.updateTitle(planId, title);
    }

    public void updatePlanTags(String planId, List<String> tags, Requester requester) {
        log.info("update planId: {}", planId);
        log.info("update plan tags: {}", tags);

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findPlanRepository.findAuthorId(planId));
        UserId updaterId = UserId.of(requester.getUserId());

        if (!authorId.same(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        if (tags.size() > 5) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        }

        updatePlanRepository.updateTags(planId, tags);
    }

    public void updatePlanImageUrl(String planId, String imageUrl, Requester requester) {
        log.info("update planId: {}", planId);
        log.info("update plan imageUrl: {}", imageUrl);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findPlanRepository.findAuthorId(planId));
        UserId updaterId = UserId.of(requester.getUserId());

        if (!authorId.same(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        PlanImageUrl.of(imageUrl);

        updatePlanRepository.updateImageUrl(planId, imageUrl);
    }

    public void updatePlanDescription(String planId, String description, Requester requester) {
        log.info("update planId: {}", planId);
        log.info("update plan description size: {}", description.length());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findPlanRepository.findAuthorId(planId));
        UserId updaterId = UserId.of(requester.getUserId());

        if (!authorId.same(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        PlanDescription.of(description);

        updatePlanRepository.updateDescription(planId, description);
    }

    public void publishPlan(PlanBaseInput baseInput, List<PlanElementInput> elementInputs, Requester requester) {
        log.info("publish plan: {}", baseInput);
        log.info("publish element size: {}", elementInputs.size());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findPlanRepository.findAuthorId(baseInput.getId()));
        UserId updaterId = UserId.of(requester.getUserId());

        if (!authorId.same(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        PublishPlan publishPlan = PublishPlanConverter.toPublishPlan(baseInput, elementInputs);

        updatePlanRepository.publishOne(publishPlan, requester.getUserId());
    }

    public void draftPlan(PlanBaseInput baseInput, List<PlanElementInput> elementInputs, Requester requester) {
        log.info("draft plan: {}", baseInput);
        log.info("draft element size: {}", elementInputs.size());

        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        UserId authorId = UserId.of(findPlanRepository.findAuthorId(baseInput.getId()));
        UserId updaterId = UserId.of(requester.getUserId());


        if (!authorId.same(updaterId)) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        DraftPlan draftPlan = DraftPlanConverter.toDraftPlan(baseInput, elementInputs);

        updatePlanRepository.draftOne(draftPlan, requester.getUserId());
    }

    public void likePlan(String planId, Requester requester) {
        log.info("like planId: {}", planId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        this.updatePlanRepository.likeOne(planId, requester.getUserId());
    }

    public void deleteLikePlan(String planId, Requester requester) {
        log.info("delete like planId: {}", planId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }

        this.updatePlanRepository.deleteLikeOne(planId, requester.getUserId());
    }

    public void startPlan(String planId, Requester requester) {
        log.info("start planId: {}", planId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        this.updatePlanRepository.startOne(planId, requester.getUserId());
    }

    public void finishPlan(String planId, Requester requester) {
        log.info("finish planId: {}", planId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        List<String> elementIds = findPlanRepository.findAllPlanElementIds(planId);
        this.updatePlanRepository.finishOne(planId, requester.getUserId(), elementIds);
    }

    public void stopPlan(String planId, Requester requester) {
        log.info("stop planId: {}", planId);
        if (requester.isGuest()) {
            throw new GraphQLCustomException(HttpStatus.UNAUTHORIZED.value(),
                                             GraphQLErrorMessage.NEED_LOGIN.getString());
        }
        this.updatePlanRepository.stopOne(planId, requester.getUserId());
    }
}
