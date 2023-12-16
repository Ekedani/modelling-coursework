import core.ChannelProcess;
import core.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExperimentHelper {
    /**
     * Compare results of two models and return map of differences between them in format:
     * factor name -> [difference, percent difference]
     *
     * @param primary
     * @param secondary
     * @return map of differences between imitation results in format: factor name -> [difference, percent difference]
     */
    public static Map<String, Double[]> compareResults(Map<String, Double> primary, Map<String, Double> secondary) {
        var differences = new LinkedHashMap<String, Double[]>();
        for (var entry : primary.entrySet()) {
            var factor = entry.getKey();
            var baseValue = entry.getValue();
            var comparisonValue = secondary.get(factor);
            var difference = comparisonValue - baseValue;
            var percentDifference = difference / baseValue * 100;
            differences.put(factor, new Double[]{difference, percentDifference});
        }
        return differences;
    }

    /**
     * Observe output of the model within specified time intervals to acknowledge the required modelling time
     *
     * @param model         model to observe
     * @param modellingTime modelling time to observe
     * @param checkInterval interval to check model output
     * @return map of observed values
     */
    public static Map<String, List<Double>> observeOutput(Model model, double modellingTime, double checkInterval) {
        var observer = new Observer("Observer", checkInterval);
        var elements = model.getElements();

        for (var element : elements) {
            if (element instanceof ChannelProcess processor) {
                observer.addObservable(
                        element.getName() + " average workload",
                        processor::getAverageWorkload
                );
            }
        }
        observer.addObservable("Mean job time in system (in)", model::getMeanJobTimeInSystem);
        observer.addObservable("Mean job time in system (out)", model::getMeanJobTimeInSystemOut);

        model.addElement(observer);
        model.simulate(modellingTime);
        return observer.getResults();
    }

    /**
     * Run model n times and return average results to reduce random factor influence
     *
     * @param model         model to run
     * @param modellingTime modelling time
     * @param n             number of runs
     * @return map of average results
     */
    public static Map<String, Double> doNRuns(Model model, double modellingTime, int n) {
        var allResults = new LinkedHashMap<String, List<Double>>();
        for (int i = 0; i < n; i++) {
            model.simulate(modellingTime);
            var results = model.getResults();
            for (var entry : results.entrySet()) {
                var factor = entry.getKey();
                var value = entry.getValue();
                if (allResults.containsKey(factor)) {
                    allResults.get(factor).add(value);
                } else {
                    var values = new ArrayList<Double>();
                    values.add(value);
                    allResults.put(factor, values);
                }
            }
            model.reset();
        }
        var averageResults = new LinkedHashMap<String, Double>();
        for (var entry : allResults.entrySet()) {
            var factor = entry.getKey();
            var values = entry.getValue();
            var average = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            averageResults.put(factor, average);
        }
        return averageResults;
    }
}
