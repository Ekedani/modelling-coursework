package core;


public abstract class Process extends Element {
    private double workTime = 0.0;
    private int failures = 0;

    public Process(String name) {
        super(name);
    }

    @Override
    public void inAct(Job job) {
    }

    @Override
    public void outAct() {
        var nextRoute = getNextRoute();
    }

    @Override
    public void doStatistics(double delta) {
        workTime += delta;
    }

    public int getFailures() {
        return failures;
    }

    public void changeFailures(int delta) {
        failures += delta;
    }
}
