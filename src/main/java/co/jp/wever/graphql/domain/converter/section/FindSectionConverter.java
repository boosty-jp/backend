package co.jp.wever.graphql.domain.converter.section;

import co.jp.wever.graphql.application.datamodel.request.CreateSectionInput;
import co.jp.wever.graphql.domain.domainmodel.section.FindSection;
import co.jp.wever.graphql.domain.domainmodel.section.SectionId;
import co.jp.wever.graphql.domain.domainmodel.section.SectionNumber;
import co.jp.wever.graphql.domain.domainmodel.section.SectionText;
import co.jp.wever.graphql.domain.domainmodel.section.SectionTitle;
import co.jp.wever.graphql.domain.domainmodel.section.statistic.SectionStatistic;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

public class FindSectionConverter {
    public static FindSection toSection(CreateSectionInput sectionInput, String userId) {
        return new FindSection(SectionId.of(""),
                               //TODO: ドメインじゃないので分離したい
                               SectionTitle.of(sectionInput.getTitle()),
                               SectionText.of(sectionInput.getText()),
                               SectionNumber.of(sectionInput.getNumber()),
                               UserId.of(userId),
                               SectionStatistic.of(0),
                               //TODO: ドメインじゃないので分離したい
                               false); //TODO: ドメインじゃないので分離したい
    }

    public static FindSection toSection(SectionEntity sectionEntity) {
        return new FindSection(SectionId.of(sectionEntity.getId()),
                               SectionTitle.of(sectionEntity.getTitle()),
                               SectionText.of(sectionEntity.getText()),
                               SectionNumber.of(sectionEntity.getNumber()),
                               UserId.of(sectionEntity.getAuthorId()),
                               SectionStatistic.of(sectionEntity.getLikeCount()),
                               sectionEntity.isLiked());
    }
}
