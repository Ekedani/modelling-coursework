import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        // runBasicModel();
        // observeBasicModelOutputs();
        // runAndCompareBasicAndModifiedModel();
        // bruteForceOptimalBufferPositions();
    }

    public static void observeBasicModelOutputs() {
        var basicConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        ExperimentHelper.observeOutput(basicConveyorModel, 1500, 25).forEach((factor, values) -> {
            System.out.print(factor + ": ");
            values.forEach(value -> System.out.print(value + "; "));
            System.out.println();
        });
    }

    public static void runBasicModel() {
        var basicConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        ExperimentHelper.doNRuns(basicConveyorModel, 2000, 20).forEach((factor, value) -> System.out.println(factor + ": " + value));
    }

    public static void runAndCompareBasicAndModifiedModel() {
        var basicConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        // Modified model has one element buffers before each processor
        var modifiedConveyorModel = ConveyorModelFactory.createCustomConveyorModel(
                1,
                4,
                5,
                1,
                1,
                1,
                1,
                5
        );
        var basicResults = ExperimentHelper.doNRuns(basicConveyorModel, 2000, 20);
        var modifiedResults = ExperimentHelper.doNRuns(modifiedConveyorModel, 2000, 20);
        System.out.println("========== BASIC MODEL RESULTS ==========");
        basicResults.forEach((factor, value) -> System.out.println(factor + ": " + value));
        System.out.println("========= MODIFIED MODEL RESULTS ========");
        modifiedResults.forEach((factor, value) -> System.out.println(factor + ": " + value));
        System.out.println("====== DIFFERENCE BETWEEN RESULTS =======");
        var differences = ExperimentHelper.compareResults(basicResults, modifiedResults);
        differences.forEach((factor, value) -> {
                    var actualDifference = value[0].toString();
                    var percentDifference = Double.isNaN(value[1]) ? "undefined" : value[1].toString() + "%";
                    System.out.println(
                            factor + ": actual difference is " + actualDifference
                                    + ", percent difference is " + percentDifference
                    );
                }
        );
    }

    public static void bruteForceOptimalBufferPositions() {
        var allResults = getResultsForAllBufferPositions(5, 10);
        var sortedResults = allResults.entrySet().stream()
                .sorted((e1, e2) -> {
                    var e1Value = e1.getValue().get("Mean job time in system (in)");
                    var e2Value = e2.getValue().get("Mean job time in system (in)");
                    return Double.compare(e1Value, e2Value);
                })
                .toList();
        for (var entry : sortedResults) {
            var bufferPositions = entry.getKey();
            var bufferPositionsString = bufferPositions.stream().map(Object::toString).reduce((s1, s2) -> s1 + ", " + s2).orElse("");
            var resultsForBufferPositions = entry.getValue();
            System.out.println("Model with buffer positions: [" + bufferPositionsString + "]");
            for (var resultEntry : resultsForBufferPositions.entrySet()) {
                var factor = resultEntry.getKey();
                var value = resultEntry.getValue();
                System.out.println(factor + ": " + value);
            }
            System.out.println();
        }
        System.out.println("Results for each buffer position: ");
        for (var entry : sortedResults) {
            System.out.println(entry.getKey() + ": " + entry.getValue().get("Mean job time in system (in)"));
        }
        System.out.println("Optimal buffer positions: " + sortedResults.get(0).getKey());
    }

    public static Map<List<Integer>, Map<String, Double>> getResultsForAllBufferPositions(int processorsCount, int totalBuffersSize) {
        var allCombinations = generateBufferCombinations(processorsCount, totalBuffersSize);
        System.out.println("Generated " + allCombinations.size() + " buffer combinations");
        var allModels = allCombinations.stream()
                .map(bufferPositions -> ConveyorModelFactory.createCustomConveyorModel(
                        1,
                        4,
                        processorsCount,
                        1,
                        1,
                        bufferPositions,
                        1,
                        5
                ))
                .toList();
        var results = new ConcurrentHashMap<List<Integer>, Map<String, Double>>();
        var executor = Executors.newFixedThreadPool(8);
        var remainingCounter = new AtomicInteger(allModels.size());
        for (int i = 0; i < allModels.size(); i++) {
            var model = allModels.get(i);
            var bufferPositions = allCombinations.get(i);
            executor.submit(() -> {
                var modelResults = ExperimentHelper.doNRuns(model, 2000, 20);
                var remains = remainingCounter.decrementAndGet();
                System.out.println(
                        "Model with buffer positions " + bufferPositions + " finished, " + remains + " models remaining"
                );
                results.put(bufferPositions, modelResults);
            });
        }
        executor.close();
        return results;
    }

    private static List<List<Integer>> generateBufferCombinations(int processorsCount, int totalBuffersSize) {
        List<List<Integer>> allCombinations = new ArrayList<>();
        generateBufferCombinations(processorsCount, totalBuffersSize, new ArrayList<>(), allCombinations);
        return allCombinations;
    }

    private static void generateBufferCombinations(
            int processorsCount,
            int remainingBufferSize,
            List<Integer> currentCombination,
            List<List<Integer>> allCombinations
    ) {
        if (processorsCount == 0) {
            if (remainingBufferSize == 0) {
                allCombinations.add(new ArrayList<>(currentCombination));
            }
            return;
        }
        for (int i = 0; i <= remainingBufferSize; i++) {
            currentCombination.add(i);
            generateBufferCombinations(processorsCount - 1, remainingBufferSize - i, currentCombination, allCombinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}