package co.jp.wever.graphql.domain.domainmodel.section;

public class SectionId {
    private String value;

    private SectionId(String value) {
        this.value = value;
    }

    public static SectionId of(String value) {
        return new SectionId(value);
    }

    public String getValue() {
        return value;
    }
}
