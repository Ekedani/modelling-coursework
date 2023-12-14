package core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private final ArrayList<Element> elements;
    private double tCurr = 0;
    private double tNext = Double.MAX_VALUE;

    public Model(List<Element> elements) {
        this.elements = new ArrayList<>(elements);
        Element.resetNextId();
    }

    /**
     * Simulates events up to a specified time using the nearest event algorithm.
     *
     * @param time the time up to which the simulation will run.
     */
    public void simulate(double time) {
        while (tCurr < time) {
            tNext = Double.MAX_VALUE;
            for (Element element : elements) {
                if (element.getTNext() < tNext) {
                    tNext = element.getTNext();
                }
            }
            for (Element element : elements) {
                element.doStatistics(tNext - tCurr);
            }
            tCurr = tNext;
            for (var element : elements) {
                element.setTCurr(tCurr);
            }

            for (Element element : elements) {
                if (element.getTNext() == tCurr) {
                    element.outAct();
                }
            }
        }
    }

    public Map<String, Double> getResults() {
        var results = new LinkedHashMap<String, Double>();
        var jobs = new ArrayList<Job>();
        var averageWorkingProcesses = 0.0;
        for (Element element : elements) {
            if (element instanceof Create) {
                results.put(element.getName() + " created", (double) element.getQuantity());
            /*} else if (element instanceof ChannelProcess) {
                results.put(element.getName() + " average buffer size", ((ChannelProcess) element).getAverageQueueSize());
                results.put(element.getName() + " average workload", ((ChannelProcess) element).getAverageWorkload());
                jobs.addAll(((Process) element).getAllJobs());
            } else if (element instanceof DelayProcess) {
                results.put(element.getName() + " average queue size", ((DelayProcess) element).getAverageQueueSize());
                results.put(element.getName() + " average workload", ((DelayProcess) element).getAverageWorkload());
                jobs.addAll(((Process) element).getAllJobs());*/
            } else if (element instanceof Dispose) {
                results.put(element.getName() + " disposed", (double) element.getQuantity());
                jobs.addAll(((Dispose) element).getAllJobs());
            }
        }
        return results;
    }

    public void printResults() {
        for (Element element : elements) {
            element.printResult();
        }
    }

    public void reset() {
        for (Element element : elements) {
            element.reset();
        }
    }

}
