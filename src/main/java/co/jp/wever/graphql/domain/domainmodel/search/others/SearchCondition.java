package co.jp.wever.graphql.domain.domainmodel.search.others;

import co.jp.wever.graphql.domain.domainmodel.search.SearchOrder;
import co.jp.wever.graphql.domain.domainmodel.search.SearchPage;
import co.jp.wever.graphql.domain.domainmodel.search.SearchResultCount;
import co.jp.wever.graphql.domain.domainmodel.search.SearchSortField;

public class SearchCondition {
    private SearchSortField field;
    private SearchOrder order;
    private SearchPage page;
    private SearchResultCount resultCount;

    private SearchCondition(
        SearchSortField field, SearchOrder order, SearchPage page, SearchResultCount resultCount) {
        this.field = field;
        this.order = order;
        this.page = page;
        this.resultCount = resultCount;
    }

    public static SearchCondition of(String field, String order, int page, int resultCount) {
        return new SearchCondition(SearchSortField.of(field),
                                   SearchOrder.of(order),
                                   SearchPage.of(page),
                                   SearchResultCount.of(resultCount));
    }

    public boolean shouldSort() {
        if (!field.hasField()) {
            return false;
        }

        return order.isAscend() || order.isDescend();
    }

    public boolean isAscend() {
        return order.isAscend() && field.hasField();
    }

    public boolean isDescend() {
        return order.isDescend() && field.hasField();
    }

    public boolean edgeSort() {
        return field.edgeSort();
    }

    public boolean vertexSort() {
        return field.vertexSort();
    }

    public String getField(){
        return field.getValue();
    }

    public int getRangeStart(){
       return resultCount.getValue() * (page.getValue() - 1);
    }

    public int getRangeEnd(){
        return resultCount.getValue() * page.getValue();
    }
}
