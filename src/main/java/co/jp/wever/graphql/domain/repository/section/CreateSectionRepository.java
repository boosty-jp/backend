package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;

@Repository
public interface CreateSectionRepository {
    String addOne(
        String authorId,
        String articleId,
        CourseSectionEntity courseSectionEntity,
        List<SectionNumberEntity> incrementSectionIds);
}
