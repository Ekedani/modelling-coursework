import java.util.LinkedHashMap;
import java.util.Map;

public class ExperimentBuilder {
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
}
