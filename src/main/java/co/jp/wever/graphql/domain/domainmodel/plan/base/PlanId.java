package co.jp.wever.graphql.domain.domainmodel.plan.base;

public class PlanId {
    private String value;

    private PlanId(String value) {
        this.value = value;
    }

    public static PlanId of(String value) {
        //TODO: ドメインじゃないので分離する
//        if (StringUtil.isNullOrEmpty(value)) {
//            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
//                                             GraphQLErrorMessage.PLAN_ID_EMPTY.getString());
//        }

        return new PlanId(value);
    }

    public String getValue() {
        return value;
    }
}
