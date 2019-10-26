package co.jp.wever.graphql.domain.service.section;

import org.springframework.stereotype.Service;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;
import co.jp.wever.graphql.domain.converter.section.SectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.converter.entity.section.SectionEntityConverter;
import co.jp.wever.graphql.infrastructure.repository.section.FindSectionRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.section.UpdateSectionRepositoryImpl;

@Service
public class UpdateSectionService {

    private final FindSectionRepositoryImpl findSectionRepository;
    private final UpdateSectionRepositoryImpl updateSectionRepository;

    public UpdateSectionService(
        FindSectionRepositoryImpl findSectionRepository, UpdateSectionRepositoryImpl updateSectionRepository) {
        this.findSectionRepository = findSectionRepository;
        this.updateSectionRepository = updateSectionRepository;
    }

    public void updateSection(String sectionId, SectionInput sectionInput, String userId)
        throws IllegalAccessException {
        Section target = SectionConverter.toSection(findSectionRepository.findOne(sectionId));
        Section updateSection = SectionConverter.toSection(sectionInput, userId);

        if (!target.canUpdate(UserId.of(userId), updateSection.getSectionNumber())) {
            throw new IllegalAccessException();
        }

        updateSectionRepository.updateOne(SectionEntityConverter.toSectionEntity(updateSection, sectionId));
    }

    public void likeSection(String sectionId, String userId) {
        updateSectionRepository.likeOne(sectionId, userId);
    }

    public void deleteLikeSection(String sectionId, String userId) {
        updateSectionRepository.deleteLikeOne(sectionId,userId);
    }
}

