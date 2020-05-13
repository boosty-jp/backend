package jp.boosty.graphql.domain.factory;

import jp.boosty.graphql.domain.domainmodel.book.Book;
import jp.boosty.graphql.domain.domainmodel.book.section.BookSections;
import jp.boosty.graphql.domain.domainmodel.content.ContentImageUrl;
import jp.boosty.graphql.domain.domainmodel.content.ContentTags;
import jp.boosty.graphql.domain.domainmodel.content.ContentTitle;
import jp.boosty.graphql.infrastructure.datamodel.book.BookEntity;
import jp.boosty.graphql.domain.domainmodel.book.BookBase;
import jp.boosty.graphql.domain.domainmodel.book.BookDescription;
import jp.boosty.graphql.domain.domainmodel.book.BookFeatures;
import jp.boosty.graphql.domain.domainmodel.book.BookPrice;
import jp.boosty.graphql.domain.domainmodel.book.BookTargets;

public class BookFactory {
    public static Book make(BookEntity entity) {
        ContentImageUrl imageUrl = ContentImageUrl.of(entity.getBase().getImageUrl());

        BookBase bookBase = BookBase.of(ContentTitle.of(entity.getBase().getTitle()),
                                        BookDescription.of(entity.getBase().getDescription()),
                                        BookPrice.of(entity.getBase().getPrice()));

        BookSections sections = BookSectionsFactory.make(entity.getSections());

        BookFeatures features = BookFeaturesFactory.make(entity.getBase().getFeatures());

        BookTargets targets = BookTargetsFactory.make(entity.getBase().getLevelStart(),
                                                      entity.getBase().getLevelEnd(),
                                                      entity.getBase().getTargetDescriptions());

        ContentTags tagIds = ContentTagIdsFactory.make(entity.getTags());

        return Book.of(imageUrl, bookBase, sections, features, targets, tagIds);
    }
}
