package jp.boosty.backend.domain.factory;

import jp.boosty.backend.domain.domainmodel.book.Book;
import jp.boosty.backend.domain.domainmodel.book.section.BookSections;
import jp.boosty.backend.domain.domainmodel.content.ContentImageUrl;
import jp.boosty.backend.domain.domainmodel.content.ContentTags;
import jp.boosty.backend.domain.domainmodel.content.ContentTitle;
import jp.boosty.backend.infrastructure.datamodel.book.BookEntity;
import jp.boosty.backend.domain.domainmodel.book.BookBase;
import jp.boosty.backend.domain.domainmodel.book.BookDescription;
import jp.boosty.backend.domain.domainmodel.book.BookFeatures;
import jp.boosty.backend.domain.domainmodel.book.BookPrice;
import jp.boosty.backend.domain.domainmodel.book.BookTargets;

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
