package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

import java.util.List;

import co.jp.wever.graphql.infrastructure.datamodel.section.LikedSectionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;


@Repository
public interface FindSectionRepository {

    SectionEntity findOne(String sectionId);

    List<SectionEntity> findAllDetailOnArticle(String articleId, String userId);

    List<LikedSectionEntity> findAllLiked(String userId);

    List<SectionEntity> findAllBookmarked(String userId);

    List<SectionEntity> findFamous();

    List<SectionEntity> findRelated(String userId);

    String findAuthorId(String sectionId);
}
