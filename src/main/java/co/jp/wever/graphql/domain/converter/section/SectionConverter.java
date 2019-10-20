package co.jp.wever.graphql.domain.converter.section;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;
import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.domain.domainmodel.section.SectionId;
import co.jp.wever.graphql.domain.domainmodel.section.SectionNumber;
import co.jp.wever.graphql.domain.domainmodel.section.SectionTexts;
import co.jp.wever.graphql.domain.domainmodel.section.SectionTitle;
import co.jp.wever.graphql.domain.domainmodel.section.statistic.SectionStatistic;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

public class SectionConverter {
    public static Section toSection(SectionInput sectionInput, String userId) {
        return new Section(SectionId.of(""),
                           //TODO: ドメインじゃないので分離したい
                           SectionTitle.of(sectionInput.getTitle()),
                           SectionTexts.of(sectionInput.getTexts()),
                           SectionNumber.of(sectionInput.getNumber()),
                           UserId.of(userId),
                           SectionStatistic.of(0, 0)); //TODO: ドメインじゃないので分離したい
    }

    public static Section toSection(SectionEntity sectionEntity) {
        return new Section(SectionId.of(sectionEntity.getId()),
                           SectionTitle.of(sectionEntity.getTitle()),
                           SectionTexts.of(sectionEntity.getTexts()),
                           SectionNumber.of(sectionEntity.getNumber()),
                           UserId.of(sectionEntity.getAuthorId()),
                           SectionStatistic.of(sectionEntity.getStatisticEntity().getLike(),
                                               sectionEntity.getStatisticEntity().getBookmark()));
    }
}
