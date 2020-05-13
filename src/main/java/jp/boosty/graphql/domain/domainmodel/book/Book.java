package jp.boosty.graphql.domain.domainmodel.book;

import jp.boosty.graphql.domain.domainmodel.book.section.BookSections;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import jp.boosty.graphql.domain.GraphQLCustomException;
import jp.boosty.graphql.domain.domainmodel.content.ContentImageUrl;
import jp.boosty.graphql.domain.domainmodel.content.ContentTags;
import jp.boosty.graphql.infrastructure.constant.GraphQLErrorMessage;

public class Book {

    private ContentImageUrl imageUrl;
    private BookBase base;
    private BookSections sections;
    private BookFeatures features;
    private BookTargets targets;
    private ContentTags tagIds;

    private Book(
        ContentImageUrl imageUrl,
        BookBase base,
        BookSections sections,
        BookFeatures features,
        BookTargets targets,
        ContentTags tagIds) {
        this.imageUrl = imageUrl;
        this.base = base;
        this.sections = sections;
        this.features = features;
        this.targets = targets;
        this.tagIds = tagIds;
    }

    public static Book of(
        ContentImageUrl imageUrl,
        BookBase base,
        BookSections sections,
        BookFeatures features,
        BookTargets targets,
        ContentTags tagIds) {
        if (Objects.isNull(base) || Objects.isNull(base) || Objects.isNull(sections) || Objects.isNull(features)
            || Objects.isNull(targets) || Objects.isNull(tagIds)) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_BOOK_DATA.getString());
        }

        return new Book(imageUrl, base, sections, features, targets, tagIds);
    }

    public void publishValidation() {
        if (!imageUrl.hasValue()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EMPTY_IMAGE.getString());
        }

        base.publishValidation();
        sections.publishValidation();

        if (!tagIds.hasValue()) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(), GraphQLErrorMessage.EMPTY_TAG.getString());
        }
    }

    public ContentImageUrl getImageUrl() {
        return imageUrl;
    }

    public BookBase getBase() {
        return base;
    }

    public BookSections getSections() {
        return sections;
    }

    public BookFeatures getFeatures() {
        return features;
    }

    public BookTargets getTargets() {
        return targets;
    }

    public ContentTags getTagIds() {
        return tagIds;
    }
}
