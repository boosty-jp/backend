package co.jp.wever.graphql.domain.converter.section;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;
import co.jp.wever.graphql.domain.domainmodel.section.FindSection;
import co.jp.wever.graphql.domain.domainmodel.section.SectionId;
import co.jp.wever.graphql.domain.domainmodel.section.SectionNumber;
import co.jp.wever.graphql.domain.domainmodel.section.SectionText;
import co.jp.wever.graphql.domain.domainmodel.section.SectionTitle;
import co.jp.wever.graphql.domain.domainmodel.section.statistic.SectionStatistic;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionEntity;

public class FindSectionConverter {
    public static FindSection toSection(SectionInput sectionInput, String userId) {
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

    public static FindSection toSection(CourseSectionEntity courseSectionEntity) {
        return new FindSection(SectionId.of(courseSectionEntity.getId()),
                               SectionTitle.of(courseSectionEntity.getTitle()),
                               SectionText.of(courseSectionEntity.getText()),
                               SectionNumber.of(courseSectionEntity.getNumber()),
                               UserId.of(courseSectionEntity.getAuthorId()),
                               SectionStatistic.of(courseSectionEntity.getLikeCount()),
                               courseSectionEntity.isLiked());
    }
}
