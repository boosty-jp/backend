package co.jp.wever.graphql.domain.domainmodel.section.statistic;

public class SectionStatistic {
    private SectionLike like;
    private SectionBookmark bookmark;

    private SectionStatistic(SectionLike like, SectionBookmark bookmark) {
        this.like = like;
        this.bookmark = bookmark;
    }

    public static SectionStatistic of(int like, int bookmark) {
        return new SectionStatistic(SectionLike.of(like), SectionBookmark.of(bookmark));
    }

    public SectionLike getLike() {
        return like;
    }

    public SectionBookmark getBookmark() {
        return bookmark;
    }
}
