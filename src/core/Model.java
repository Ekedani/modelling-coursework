package core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private final ArrayList<Element> elements;
    private double tCurr = 0;

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
            double tNext = Double.MAX_VALUE;
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
        var jobsIn = new ArrayList<Job>();
        var jobsOut = new ArrayList<Job>();
        var averageWorkingProcesses = 0.0;

        for (Element element : elements) {
            if (element instanceof Create) {
                results.put("Created", (double) element.getQuantity());
            } else if (element instanceof ChannelProcess) {
                var workload = ((ChannelProcess) element).getAverageWorkload();
                averageWorkingProcesses += workload;
                results.put(element.getName() + " average workload", workload);
                jobsIn.addAll(((Process) element).getAllJobs());
            } else if (element instanceof DelayProcess) {
                jobsIn.addAll(((Process) element).getAllJobs());
            } else if (element instanceof Dispose) {
                jobsIn.addAll(((Dispose) element).getAllJobs());
                jobsOut.addAll(((Dispose) element).getAllJobs());
            }
        }
        results.put("Average working processes", averageWorkingProcesses);
        results.put("Mean job time in system (in)", jobsIn.stream().mapToDouble(this::getJobTimeInModel).average().orElse(0.0));
        results.put("Max job time in system (in)", jobsIn.stream().mapToDouble(this::getJobTimeInModel).max().orElse(0.0));
        results.put("Min job time in system (in)", jobsIn.stream().mapToDouble(this::getJobTimeInModel).min().orElse(0.0));
        results.put("Mean job time in system (out)", jobsOut.stream().mapToDouble(Job::getTimeInModel).average().orElse(0.0));
        results.put("Max job time in system (out)", jobsOut.stream().mapToDouble(Job::getTimeInModel).max().orElse(0.0));
        results.put("Min job time in system (out)", jobsOut.stream().mapToDouble(Job::getTimeInModel).min().orElse(0.0));
        results.put("Disposed", (double) jobsOut.size());
        return results;
    }

    public void printResults() {
        System.out.println("============MODELLING RESULTS============");
        var results = getResults();
        for (var entry : results.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void reset() {
        tCurr = 0;
        for (Element element : elements) {
            element.reset();
        }
    }

    private double getJobTimeInModel(Job job) {
        if (job.getTimeOut() == Double.MAX_VALUE) {
            return tCurr - job.getTimeIn();
        } else {
            return job.getTimeInModel();
        }
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public double getMeanJobTimeInSystem() {
        var jobsIn = new ArrayList<Job>();
        for (Element element : elements) {
            if (element instanceof Dispose) {
                jobsIn.addAll(((Dispose) element).getAllJobs());
            } else if (element instanceof ChannelProcess) {
                jobsIn.addAll(((Process) element).getAllJobs());
            } else if (element instanceof DelayProcess) {
                jobsIn.addAll(((Process) element).getAllJobs());
            }
        }
        return jobsIn.stream().mapToDouble(this::getJobTimeInModel).average().orElse(0.0);
    }

    public double getMeanJobTimeInSystemOut() {
        var jobsOut = new ArrayList<Job>();
        for (Element element : elements) {
            if (element instanceof Dispose) {
                jobsOut.addAll(((Dispose) element).getAllJobs());
            }
        }
        return jobsOut.stream().mapToDouble(Job::getTimeInModel).average().orElse(0.0);
    }
}
