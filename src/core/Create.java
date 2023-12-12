package core;

public abstract class Create extends Element {
    private int failures = 0;

    public Create(String name) {
        super(name);
    }

    public Create(String name, double delayMean) {
        super(name);
        setDelayMean(delayMean);
    }

    @Override
    public void inAct(Job job) {
        throw new UnsupportedOperationException("InAct is not supported for Create");
    }

    public int getFailures() {
        return failures;
    }

    public void changeFailures(int delta) {
        this.failures += delta;
    }
}
