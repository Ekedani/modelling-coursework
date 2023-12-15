import core.Element;
import core.Job;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Observer extends Element {

    Map<String, Callable<Double>> observables = new LinkedHashMap<>();
    Map<String, List<Double>> results = new LinkedHashMap<>();

    public Observer(String name, double checkInterval) {
        super(name);
        setTNext(checkInterval);
        setDelayMean(checkInterval);
        Element.resetNextId();
    }

    public void addObservable(String name, Callable<Double> observable) {
        observables.put(name, observable);
        results.put(name, new ArrayList<>());
    }

    @Override
    public void inAct(Job job) {
        throw new UnsupportedOperationException("InAct is not supported for Observer");
    }

    @Override
    public void outAct() {
        for (var observable : observables.entrySet()) {
            try {
                results.get(observable.getKey()).add(observable.getValue().call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Observer: " + getTCurr());
        setTNext(getTCurr() + getDelay());
    }

    @Override
    public void printInfo() {
    }

    @Override
    public void printResult() {
    }

    @Override
    public void doStatistics(double delta) {}

    public Map<String, List<Double>> getResults() {
        return results;
    }
}
