package co.jp.wever.graphql.domain.domainmodel.section;

import co.jp.wever.graphql.domain.domainmodel.section.statistic.SectionStatistic;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;

public class Section {

    private SectionId id;
    private SectionTitle title;
    private SectionText text;
    private SectionNumber number;
    private UserId authorId;
    private SectionStatistic statistic;

    public Section(
        SectionId id,
        SectionTitle title,
        SectionText text,
        SectionNumber number,
        UserId authorId,
        SectionStatistic statistic) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.number = number;
        this.authorId = authorId;
        this.statistic = statistic;
    }

    public SectionId getId() {
        return id;
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getText() {
        return text.getValue();
    }

    public SectionNumber getSectionNumber() {
        return number;
    }

    public int getNumber() {
        return number.getValue();
    }

    public boolean canUpdate(UserId userId, SectionNumber sectionNumber) {
        if (!authorId.same(userId)) {
            return false;
        }

        return number.isSame(sectionNumber);
    }

    public SectionStatistic getStatistic() {
        return statistic;
    }

    public boolean canDelete(UserId userId) {
        return authorId.same(userId);
    }
}
