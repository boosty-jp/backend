package co.jp.wever.graphql.infrastructure.repository.section;

import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.section.CreateSectionRepository;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

@Component
public class CreateSectionRepositoryImpl implements CreateSectionRepository {

    @Override
    public String addOne(String userId, SectionEntity sectionEntity){
        return null;
    }
}
