package co.jp.wever.graphql.infrastructure.repository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository {
    //Mutation
    void followOne(String id);

    void unFollowOne(String id);
}
