package co.jp.wever.graphql.domain.domainmodel.section.statistic;

public class SectionStatistic {
    private SectionLike like;

    private SectionStatistic(SectionLike like) {
        this.like = like;
    }

    public static SectionStatistic of(int like) {
        return new SectionStatistic(SectionLike.of(like));
    }

    public SectionLike getLike() {
        return like;
    }

}
