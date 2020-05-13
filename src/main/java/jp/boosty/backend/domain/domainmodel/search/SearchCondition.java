package jp.boosty.backend.domain.domainmodel.search;

public class SearchCondition {
    private SearchFilter filter;
    private SearchSortField field;
    private SearchOrder order;
    private SearchPage page;
    private SearchResultCount resultCount;

    public SearchCondition(
        SearchFilter filter, SearchSortField field, SearchOrder order, SearchPage page, SearchResultCount resultCount) {
        this.filter = filter;
        this.field = field;
        this.order = order;
        this.page = page;
        this.resultCount = resultCount;
    }

    public static SearchCondition of(String filter, String field, String order, int page, int resultCount) {
        return new SearchCondition(SearchFilter.of(filter),
                                   SearchSortField.of(field),
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

    public boolean vertexSort() {
        return field.vertexSort();
    }

    public String getField() {
        return field.getValue();
    }

    public int getRangeStart() {
        return resultCount.getValue() * (page.getValue() - 1);
    }

    public int getRangeEnd() {
        return resultCount.getValue() * page.getValue();
    }

    public boolean shouldFilter() {
        return filter.shouldFilter();
    }

    public String getFilter() {
        return filter.getValue();
    }

    public boolean createdFilter() {
        return filter.createdFilter();
    }
}
