package co.jp.wever.graphql.infrastructure.repository.section;

import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.section.FindSectionRepository;
import co.jp.wever.graphql.infrastructure.datamodel.SectionEntity;

@Component
public class FindSectionRepositoryImpl implements FindSectionRepository {

    @Override
    public SectionEntity findOne(String id) {
        return SectionEntity.builder().build();
    }

    @Override
    public List<SectionEntity> findAllLiked(String id) {
        return null;
    }

    @Override
    public List<SectionEntity> findAllBookmarked(String id) {
        return null;
    }

    @Override
    public List<SectionEntity> findFamous(String id) {
        return null;
    }

    @Override
    public List<SectionEntity> findRelated(String id) {
        return null;
    }

}
