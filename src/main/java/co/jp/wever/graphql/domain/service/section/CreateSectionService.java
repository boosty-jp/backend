package co.jp.wever.graphql.domain.service.section;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.application.datamodel.request.SectionInput;
import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.domain.converter.section.SectionConverter;
import co.jp.wever.graphql.domain.domainmodel.section.Section;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;
import co.jp.wever.graphql.infrastructure.converter.entity.section.SectionEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;
import co.jp.wever.graphql.infrastructure.repository.section.CreateSectionRepositoryImpl;
import co.jp.wever.graphql.infrastructure.repository.section.FindSectionRepositoryImpl;

@Service
public class CreateSectionService {

    private final FindSectionRepositoryImpl findSectionRepository;
    private final CreateSectionRepositoryImpl createSectionRepository;

    public CreateSectionService(
        FindSectionRepositoryImpl findSectionRepository, CreateSectionRepositoryImpl createSectionRepository) {
        this.findSectionRepository = findSectionRepository;
        this.createSectionRepository = createSectionRepository;
    }

    public String createSection(String userId, String articleId, SectionInput sectionInput) {
        Section section = SectionConverter.toSection(sectionInput, userId);
        List<SectionNumberEntity> sectionNumbers = findSectionRepository.findAllNumberOnArticle(articleId);
        List<SectionNumberEntity> incrementNumbers = sectionNumbers.stream()
                                                                   .filter(s -> s.getNumber()
                                                                       >= sectionInput.getNumber())
                                                                   .map(s -> SectionNumberEntity.builder()
                                                                                                .id(s.getId())
                                                                                                .number(
                                                                                                    s.getNumber() + 1)
                                                                                                .build())
                                                                   .collect(Collectors.toList());

        // TODO: Numberのチェックはドメインに閉じ込める
        if (sectionInput.getNumber() < 0) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_NUMBER_INVALID.getString());
        }

        if (!sectionNumbers.isEmpty()) {
            int maxNumber = sectionNumbers.stream()
                                          .map(s -> s.getNumber())
                                          .collect(Collectors.toList())
                                          .stream()
                                          .mapToInt(v -> v)
                                          .max()
                                          .orElseThrow(() -> new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR
                                                                                            .value(),
                                                                                        GraphQLErrorMessage.INTERNAL_SERVER_ERROR
                                                                                            .getString()));
            if (sectionInput.getNumber() > maxNumber + 1) {
                throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                                 GraphQLErrorMessage.SECTION_NUMBER_INVALID.getString());
            }
        }

        return createSectionRepository.addOne(userId,
                                              articleId,
                                              SectionEntityConverter.toSectionEntity(section),
                                              incrementNumbers);
    }
}
