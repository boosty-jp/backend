package co.jp.wever.graphql.domain.repository;

import org.springframework.stereotype.Repository;

import co.jp.wever.graphql.infrastructure.datamodel.Section;

@Repository
public interface SectionRepository {
    //Query
    Section findOne(String id);

    Section findAllLiked(String id);

    Section findAllBookmarked(String id);

    Section findFamous(String id);

    Section findRelated(String id);

    //Mutation
    Section addOne(String id);

    Section updateOne(String id);

    Section bookmarkOne(String id);

    Section likeOne(String id);
}
