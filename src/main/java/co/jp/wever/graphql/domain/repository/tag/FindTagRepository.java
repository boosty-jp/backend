package co.jp.wever.graphql.domain.repository.tag;

import org.springframework.stereotype.Repository;

@Repository
public interface FindTagRepository {
    boolean exists(String name);
}
