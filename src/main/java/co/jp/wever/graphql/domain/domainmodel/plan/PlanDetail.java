package co.jp.wever.graphql.domain.domainmodel.plan;

import java.util.List;

import co.jp.wever.graphql.application.datamodel.request.user.Requester;
import co.jp.wever.graphql.domain.domainmodel.plan.action.PlanUserAction;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanStatus;
import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.element.FindPlanElements;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanStatistics;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;

public class PlanDetail {
    private PlanBase base;
    private List<Tag> tags;
    private User author;
    private FindPlanElements elements;
    private PlanStatistics statistics;
    private PlanUserAction action;
    private final static int MAX_TAG_COUNT = 5;
    private final static int MAX_ELEMENT_COUNT = 30;

    private PlanDetail(
        PlanBase base,
        List<Tag> tags,
        User author,
        FindPlanElements elements,
        PlanStatistics statistics,
        PlanUserAction action) {
        this.base = base;
        this.tags = tags;
        this.author = author;
        this.elements = elements;
        this.statistics = statistics;
        this.action = action;
    }

    public static PlanDetail of(
        PlanBase base,
        List<Tag> tags,
        User author,
        FindPlanElements elements,
        PlanStatistics statistics,
        PlanUserAction action) {

        // タグ設定時と公開のときのみエラーにする
        //        if (tags.size() > MAX_TAG_COUNT) {
        //            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
        //                                             GraphQLErrorMessage.INVALID_TAG_COUNT.getString());
        //        }

        // タグ設定時と公開のときのみエラーにする
        //        if (elements.size() > MAX_ELEMENT_COUNT) {
        //            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
        //                                             GraphQLErrorMessage.PLAN_ELEMENT_OVER.getString());
        //        }

        return new PlanDetail(base, tags, author, elements, statistics, action);
    }

    public boolean canRead(Requester requester) {

        if (base.getStatus().getString().equals(PlanStatus.PUBLISHED.getString())) {
            return true;
        }

        if (requester.isGuest()) {
            return false;
        }

        UserId requesterId = UserId.of(requester.getUserId());
        return author.getUserId().same(requesterId);
    }

    public boolean canUpdate(UserId userId) {
        return author.getUserId().same(userId);
    }

    public PlanBase getBase() {
        return base;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public User getAuthor() {
        return author;
    }

    public List<FindPlanElement> getElements() {
        return elements.getElements();
    }

    public PlanStatistics getStatistics() {
        return statistics;
    }

    public PlanUserAction getAction() {
        return action;
    }
}
