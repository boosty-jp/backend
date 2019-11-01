package co.jp.wever.graphql.domain.domainmodel.plan;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.plan.action.PlanUserAction;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanBase;
import co.jp.wever.graphql.domain.domainmodel.plan.base.PlanStatus;
import co.jp.wever.graphql.domain.domainmodel.plan.element.PlanElement;
import co.jp.wever.graphql.domain.domainmodel.plan.statistics.PlanStatistics;
import co.jp.wever.graphql.domain.domainmodel.tag.Tag;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;

public class PlanDetail {
    private PlanBase base;
    private List<Tag> tags;
    private User author;
    private List<PlanElement> elements;
    private PlanStatistics statistics;
    private PlanUserAction action;
    private final static int MAX_TAG_COUNT = 5;
    private final static int MAX_ELEMENT_COUNT = 30;

    private PlanDetail(
        PlanBase base,
        List<Tag> tags,
        User author,
        List<PlanElement> elements,
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
        List<PlanElement> elements,
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

    public boolean canRead(UserId userId) {
        if (base.getStatus().equals(PlanStatus.PUBLISHED.getString())) {
            return true;
        }

        return author.getUserId().same(userId);
    }

    public boolean canUpdate(UserId userId){
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

    public List<PlanElement> getElements() {
        return elements;
    }

    public PlanStatistics getStatistics() {
        return statistics;
    }

    public PlanUserAction getAction() {
        return action;
    }
}
