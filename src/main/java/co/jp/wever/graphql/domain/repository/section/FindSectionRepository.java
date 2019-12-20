package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.section.LikedSectionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.course.CourseSectionEntity;


@Repository
public interface FindSectionRepository {

    CourseSectionEntity findOne(String sectionId);

    List<CourseSectionEntity> findAllDetailOnArticle(String articleId, String userId);

    List<LikedSectionEntity> findAllLiked(String userId);

    List<CourseSectionEntity> findAllBookmarked(String userId);

    List<CourseSectionEntity> findFamous();

    List<CourseSectionEntity> findRelated(String userId);

    String findAuthorId(String sectionId);
}
