package co.jp.wever.graphql.domain.repository.section;

import org.springframework.stereotype.Repository;

@Repository
public interface UpdateSectionRepository {

    void updateOne(String id);

    void bookmarkOne(String id);

    void likeOne(String id);
}

