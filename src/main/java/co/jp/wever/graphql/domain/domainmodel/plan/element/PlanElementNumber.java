package co.jp.wever.graphql.domain.domainmodel.plan.element;

public class PlanElementNumber {
    private final static int MIN_NUMBER = 1;
    private final static int MAX_NUMBER = 100;

    private int value;

    private PlanElementNumber(int value) {
        this.value = value;
    }

    public static PlanElementNumber of(int value) throws IllegalArgumentException{
        if (value < MIN_NUMBER) {
            throw new IllegalArgumentException();
        }

        if (value > MAX_NUMBER) {
            throw new IllegalArgumentException();
        }

        return new PlanElementNumber(value);
    }

    public int getValue() {
        return value;
    }
}
