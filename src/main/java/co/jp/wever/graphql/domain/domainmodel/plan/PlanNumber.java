package co.jp.wever.graphql.domain.domainmodel.plan;

public class PlanNumber {
    private final static int MIN_NUMBER = 1;
    private final static int MAX_NUMBER = 100;

    private int value;

    private PlanNumber(int value) {
        this.value = value;
    }

    public static PlanNumber of(int value) throws IllegalArgumentException{
        if (value < MIN_NUMBER) {
            throw new IllegalArgumentException();
        }

        if (value > MAX_NUMBER) {
            throw new IllegalArgumentException();
        }

        return new PlanNumber(value);
    }

    public int getValue() {
        return value;
    }
}
