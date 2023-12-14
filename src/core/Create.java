package core;

public abstract class Create extends Element {
    private int failures = 0;

    public Create(String name, double delayMean) {
        super(name);
        setDelayMean(delayMean);
    }

    @Override
    public void inAct(Job job) {
        throw new UnsupportedOperationException("InAct is not supported for Create");
    }

    @Override
    public void reset() {
        super.reset();
        failures = 0;
    }

    public int getFailures() {
        return failures;
    }

    public void changeFailures(int delta) {
        this.failures += delta;
    }
}
