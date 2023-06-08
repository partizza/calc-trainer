package ua.dev.gal.calctrainer.component;


public enum TrainOperation {

    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*");

    private final String symbol;

    TrainOperation(String symbol) {
        this.symbol = symbol;
    }

    public long apply(long a, long b) {
        return switch (this) {
            case ADDITION -> a + b;
            case SUBTRACTION -> a - b;
            case MULTIPLICATION -> a * b;
        };
    }

    public String getSymbol() {
        return symbol;
    }
}
