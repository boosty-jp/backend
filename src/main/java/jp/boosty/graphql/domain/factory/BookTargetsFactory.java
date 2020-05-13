package jp.boosty.graphql.domain.factory;

import jp.boosty.graphql.application.datamodel.request.book.BookTargetsInput;
import jp.boosty.graphql.domain.domainmodel.book.BookTargets;

import java.util.List;
import java.util.stream.Collectors;

import jp.boosty.graphql.domain.domainmodel.book.BookTargetDescription;
import jp.boosty.graphql.domain.domainmodel.book.BookTargetDescriptions;
import jp.boosty.graphql.domain.domainmodel.book.BookTargetLevel;

public class BookTargetsFactory {
    public static BookTargets make(BookTargetsInput bookTargetsInput) {
        BookTargetLevel level = BookTargetLevel.of(bookTargetsInput.getLevelStart(), bookTargetsInput.getLevelEnd());
        List<BookTargetDescription> descriptions = bookTargetsInput.getTargetsDescription()
                                                                   .stream()
                                                                   .map(d -> BookTargetDescription.of(d))
                                                                   .collect(Collectors.toList());

        return BookTargets.of(level, BookTargetDescriptions.of(descriptions));
    }

    public static BookTargets make(int levelStart, int levelEnd, List<String> descriptions) {
        BookTargetLevel level = BookTargetLevel.of(levelStart, levelEnd);
        List<BookTargetDescription> descriptionList =
            descriptions.stream().map(d -> BookTargetDescription.of(d)).collect(Collectors.toList());

        return BookTargets.of(level, BookTargetDescriptions.of(descriptionList));
    }
}
