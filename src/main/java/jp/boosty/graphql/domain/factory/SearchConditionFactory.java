package jp.boosty.graphql.domain.factory;

import jp.boosty.graphql.application.datamodel.request.search.SearchConditionInput;
import jp.boosty.graphql.domain.domainmodel.search.SearchCondition;
import jp.boosty.graphql.infrastructure.constant.edge.EdgeLabel;

public class SearchConditionFactory {
    public static SearchCondition make(SearchConditionInput input) {
        return SearchCondition.of(input.getFilter(),
                                  input.getSortField(),
                                  input.getSortOrder(),
                                  input.getPage(),
                                  input.getResultCount());
    }

    public static SearchCondition makeFilteredPublished(SearchConditionInput input) {
        return SearchCondition.of(EdgeLabel.PUBLISH.getString(),
                                  input.getSortField(),
                                  input.getSortOrder(),
                                  input.getPage(),
                                  input.getResultCount());
    }
}
