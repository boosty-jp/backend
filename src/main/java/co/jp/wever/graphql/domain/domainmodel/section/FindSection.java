package co.jp.wever.graphql.domain.domainmodel.section;

import co.jp.wever.graphql.domain.domainmodel.section.statistic.SectionStatistic;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;

public class FindSection {

    private SectionId id;
    private SectionTitle title;
    private SectionText text;
    private SectionNumber number;

    private UserId authorId;
    private SectionStatistic statistic;
    private boolean liked; //TODO: あとでドメインに直す

    public FindSection(
        SectionId id,
        SectionTitle title,
        SectionText text,
        SectionNumber number,
        UserId authorId,
        SectionStatistic statistic,
        boolean liked) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.number = number;
        this.authorId = authorId;
        this.statistic = statistic;
        this.liked = liked;
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

    public String getAuthorId() {
        return authorId.getValue();
    }

    public long getNumber() {
        return number.getValue();
    }

    public boolean canUpdate(UserId userId, SectionNumber sectionNumber) {
        return authorId.same(userId);
        //        return number.isSame(sectionNumber);
    }

    public SectionStatistic getStatistic() {
        return statistic;
    }

    public boolean canDelete(UserId userId) {

        return authorId.same(userId);
    }

    public boolean isLiked() {
        return liked;
    }

}
