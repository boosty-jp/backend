package jp.boosty.backend.domain.domainmodel.book.section;

import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

public class BookSections {
    private List<BookSection> sections;
    private final static int MAX_SIZE = 20;

    private BookSections(List<BookSection> sections) {
        this.sections = sections;
    }

    public static BookSections of(List<BookSection> sections) {
        if (Objects.isNull(sections)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_NULL.getString());
        }

        if (sections.size() > MAX_SIZE) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.SECTION_SIZE_OVER.getString());
        }

        return new BookSections(sections);
    }

    public List<BookSection> getSections() {
        return sections;
    }

    public boolean duplicatedContentId() {
        return (sections.size() != new HashSet<>(sections.stream()
                                                         .map(BookSection::getTitle)
                                                         .collect(Collectors.toList())).size());

    }

    public void publishValidation() {
        sections.forEach(BookSection::publishValidation);

        if (duplicatedContentId()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.DUPLICATED_SECTION.getString());
        }

        if (sections.size() == 0) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_SECTION.getString());
        }
    }
}
