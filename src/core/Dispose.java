package core;

import java.util.ArrayList;

public class Dispose extends Element {
    ArrayList<Job> processedJobs = new ArrayList<>();

    public Dispose(String name) {
        super(name);
    }

    @Override
    public void inAct(Job job) {
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
    }

    @Override
    public void doStatistics(double delta) {
    }
}
