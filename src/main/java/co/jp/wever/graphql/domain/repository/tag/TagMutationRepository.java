package co.jp.wever.graphql.domain.repository.tag;

import org.springframework.stereotype.Repository;

@Repository
public interface TagMutationRepository {
    String createTag(String name);
}
