import core.ChannelProcess;
import core.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExperimentHelper {
    /**
     * Compare results of two models and return map of differences between them in format:
     * factor name -> [difference, percent difference]
     *
     * @param a first model results
     * @param b second model results
     * @return map of differences between imitation results
     */
    public static Map<String, Double[]> compareResults(Map<String, Double> a, Map<String, Double> b) {
        var differences = new LinkedHashMap<String, Double[]>();
        for (var entry : a.entrySet()) {
            var factor = entry.getKey();
            var valueA = entry.getValue();
            var valueB = b.get(factor);
            var difference = valueA - valueB;
            var percentDifference = difference / valueA * 100;
            differences.put(factor, new Double[]{difference, percentDifference});
        }
        return differences;
    }

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
}
