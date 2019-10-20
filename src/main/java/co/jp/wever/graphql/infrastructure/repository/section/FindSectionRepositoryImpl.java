package co.jp.wever.graphql.infrastructure.repository.section;

import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.section.FindSectionRepository;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

@Component
public class FindSectionRepositoryImpl implements FindSectionRepository {

    @Override
    public SectionEntity findOne(String sectionId) {
        return null;
    }

    @Override
    public List<SectionEntity> findAllOnArticle(String articleId) {
        return null;
    }

    @Override
    public List<SectionEntity> findAllLiked(String userId) {
        return null;
    }

    @Override
    public List<SectionEntity> findAllBookmarked(String userId) {
        return null;
    }

    @Override
    public List<SectionEntity> findFamous() {
        return null;
    }

    @Override
    public List<SectionEntity> findRelated(String userId) {
        return null;
    }

}
