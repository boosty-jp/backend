package co.jp.wever.graphql.infrastructure.repository.section;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.section.UpdateSectionRepository;

@Component
public class UpdateSectionRepositoryImpl implements UpdateSectionRepository {

    @Override
    public void updateOne(String sectionId) {

    }

    @Override
    public void bookmarkOne(String sectionId, String userId) {

    }

    @Override
    public void likeOne(String sectionId, String userId) {

    }
}
