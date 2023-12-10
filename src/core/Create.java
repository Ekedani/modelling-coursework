package core;

public abstract class Create extends Process {
    private int failures = 0;

    public Create(String name) {
        super(name);
    }

    @Override
    public void inAct(Job job) {
        throw new UnsupportedOperationException("InAct is not supported for Create");
    }

    public abstract void outAct();

    public int getFailures() {
        return failures;
    }

    public void changeFailures(int delta) {
        this.failures += delta;
    }
}
