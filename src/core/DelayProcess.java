package core;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class DelayProcess extends Process {

    private final SortedMap<Double, List<Job>> delayedJobs = new TreeMap<>();

    public DelayProcess(String name, double delayMean) {
        super(name, delayMean);
        delayedJobs.put(Double.MAX_VALUE, new ArrayList<>());
    }

    @Override
    public void inAct(Job job) {
        double tNext = getTCurr() + getDelay();
        if (delayedJobs.containsKey(tNext)) {
            delayedJobs.get(tNext).add(job);
        } else {
            ArrayList<Job> jobs = new ArrayList<>();
            jobs.add(job);
            delayedJobs.put(tNext, jobs);
        }
    }

    @Override
    public void outAct() {
        var jobs = delayedJobs.get(getTNext());
        for (Job job : jobs) {
            var nextRoute = getNextRoute();
            if (nextRoute != null) {
                var destination = nextRoute.getDestination();
                destination.inAct(job);
            }
        }
        changeQuantity(jobs.size());
        delayedJobs.remove(delayedJobs.firstKey());
    }

    @Override
    public double getTNext() {
        return delayedJobs.firstKey();
    }

    @Override
    public void printInfo() {
    }

    @Override
    public void printResult() {
        System.out.println(
                getName() + ": " +
                        "Quantity = " + getQuantity() +
                        ", Average workload: " + getWorkTime() / getTCurr()
        );
    }

    @Override
    public int getNumberOfWorkingChannels() {
        return delayedJobs.values().stream().mapToInt(List::size).sum();
    }

    @Override
    public int getState() {
        return delayedJobs.size() > 1 ? 1 : 0;
    }

    @Override
    public void reset() {
        super.reset();
        delayedJobs.clear();
        delayedJobs.put(Double.MAX_VALUE, new ArrayList<>());
    }

    @Override
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        for (List<Job> list : delayedJobs.values()) {
            jobs.addAll(list);
        }
        return jobs;
    }

}
