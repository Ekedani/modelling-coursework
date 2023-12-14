package core;


import java.util.List;

public abstract class Process extends Element {
    private double workTime = 0.0;
    private double workingChannelsSum = 0.0;
    private int failures = 0;

    public Process(String name, double delayMean) {
        super(name);
        setDelayMean(delayMean);
    }

    public abstract int getNumberOfWorkingChannels();

    public abstract int getState();

    public abstract List<Job> getAllJobs();

    @Override
    public void doStatistics(double delta) {
        workTime += delta * getState();
        workingChannelsSum += delta * getNumberOfWorkingChannels();
    }

    @Override
    public void reset() {
        super.reset();
        workTime = 0.0;
        workingChannelsSum = 0.0;
        failures = 0;
    }

    public int getFailures() {
        return failures;
    }

    public void changeFailures(int delta) {
        failures += delta;
    }

    public double getWorkTime() {
        return workTime;
    }

}
