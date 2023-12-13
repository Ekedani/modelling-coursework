package core;

import java.util.ArrayList;

public class Dispose extends Element {
    ArrayList<Job> processedJobs = new ArrayList<>();

    public Dispose(String name) {
        super(name);
    }

    public double getAverageTimeInModel() {
        return processedJobs.stream().mapToDouble(Job::getTimeInModel).average().orElse(0.0);
    }

    public double getMaxTimeInModel() {
        return processedJobs.stream().mapToDouble(Job::getTimeInModel).max().orElse(0.0);
    }

    public double getMinTimeInModel() {
        return processedJobs.stream().mapToDouble(Job::getTimeInModel).min().orElse(0.0);
    }

    @Override
    public void inAct(Job job) {
        job.setTimeOut(getTCurr());
        processedJobs.add(job);
    }

    @Override
    public void outAct() {
        throw new UnsupportedOperationException("OutAct is not supported for Dispose");
    }

    @Override
    public void printInfo() {
        System.out.println("Dispose: " + getName());
    }

    @Override
    public void printResult() {
        System.out.println(
                getName() + ": " +
                        "Quantity = " + getQuantity() +
                        ", Average time in system: " + getAverageTimeInModel() +
                        ", Max time in system: " + getMaxTimeInModel() +
                        ", Min time in system: " + getMinTimeInModel()
        );
    }

    @Override
    public void doStatistics(double delta) {
    }

    @Override
    public int getQuantity() {
        return processedJobs.size();
    }
}
