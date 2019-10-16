package co.jp.wever.graphql.domain.domainmodel.user;

public class UserId {
    private String value;

    private UserId(String value) {
        this.value = value;
    }

    public static UserId of(String value) {
        if (value.isEmpty()) {
            // TODO: Exception検討する
            throw new IllegalArgumentException();
        }

        return new UserId(value);
    }

    public String getValue() {
        return value;
    }

    public boolean same(String targetId){
        return value.equals(targetId);
    }
}
