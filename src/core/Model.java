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
       /* for (Element element : elements) {
            if (element instanceof Create) {
                results.put(element.getName() + " failures", (double) ((Create) element).getFailures());
            } else if (element instanceof ChannelProcess) {
                results.put(element.getName() + " average queue size", ((ChannelProcess) element).getAverageQueueSize());
                results.put(element.getName() + " average workload", ((ChannelProcess) element).getAverageWorkload());
            } else if (element instanceof DelayProcess) {
                results.put(element.getName() + " average queue size", ((DelayProcess) element).getAverageQueueSize());
                results.put(element.getName() + " average workload", ((DelayProcess) element).getAverageWorkload());
            } else if (element instanceof Dispose) {
                results.put(element.getName() + " average queue size", ((Dispose) element).getAverageQueueSize());
                results.put(element.getName() + " average workload", ((Dispose) element).getAverageWorkload());
            }
        }*/
        return null;
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
