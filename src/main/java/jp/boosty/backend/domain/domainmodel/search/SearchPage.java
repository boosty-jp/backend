package jp.boosty.backend.domain.domainmodel.search;

public class SearchPage {
    private int value;

    private SearchPage(int value) {
        this.value = value;
    }

    public static SearchPage of(int value) {
        if (value < 1) {
            value = 1;
        }
        return new SearchPage(value);
    }

    public int getValue() {
        return value;
    }

}