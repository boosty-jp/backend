package jp.boosty.graphql.domain.domainmodel.search;

public class SearchOrder {
    private String value;
    private final static String ASCEND = "ascend";
    private final static String DESCEND = "descend";
    private final static String UNSORTED = "unsorted";

    private SearchOrder(String value) {
       this.value = value;
    }

    public static SearchOrder of(String value) {
        if (value.equals(ASCEND) || value.equals(DESCEND)) {
            return new SearchOrder(value);
        }

        return new SearchOrder(UNSORTED);
    }

    public boolean isAscend(){
        return value.equals(ASCEND);
    }

    public boolean isDescend(){
        return value.equals(DESCEND);
    }
}
