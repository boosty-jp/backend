package co.jp.wever.graphql.domain.service.section;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.section.FindSectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.FindSection;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;
import co.jp.wever.graphql.infrastructure.repository.section.DeleteSectionRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.section.FindSectionRepositoryImpl;

@Service
public class DeleteSectionService {
    private final FindSectionRepositoryImpl findSectionRepository;
    private final DeleteSectionRepositoryImpl deleteSectionRepository;

    public DeleteSectionService(
        FindSectionRepositoryImpl findSectionRepository, DeleteSectionRepositoryImpl deleteSectionRepository) {
        this.findSectionRepository = findSectionRepository;
        this.deleteSectionRepository = deleteSectionRepository;
    }

    public void deleteSection(String articleId, String sectionId, String userId) {
        FindSection findSection = FindSectionConverter.toSection(findSectionRepository.findOne(sectionId));

        if (!findSection.canDelete(UserId.of(userId))) {
            throw new GraphQLCustomException(HttpStatus.FORBIDDEN.value(),
                                             GraphQLErrorMessage.FORBIDDEN_REQUEST.getString());
        }

        List<SectionNumberEntity> sectionNumbers = findSectionRepository.findAllNumberOnArticle(articleId);
        List<SectionNumberEntity> decrementNumbers = sectionNumbers.stream()
                                                                   .filter(s -> s.getNumber() > findSection.getNumber())
                                                                   .map(s -> SectionNumberEntity.builder()
                                                                                                .id(s.getId())
                                                                                                .number(
                                                                                                    s.getNumber() - 1)
                                                                                                .build())
                                                                   .collect(Collectors.toList());

        deleteSectionRepository.deleteOne(articleId, sectionId, userId, decrementNumbers);
    }
}
