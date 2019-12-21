package co.jp.wever.graphql.domain.factory;

import co.jp.wever.graphql.application.datamodel.request.search.SearchConditionInput;
import co.jp.wever.graphql.domain.domainmodel.search.SearchCondition;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;

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
