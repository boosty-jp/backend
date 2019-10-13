package co.jp.wever.graphql.infrastructure.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository{
    //Mutation
    void followOne(String id);
}
