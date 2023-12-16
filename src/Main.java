import java.util.List;

public class Main {
    public static void main(String[] args) {
        // runBasicModel();
        // observeBasicModelOutputs();
        runAndCompareBasicAndModifiedModel();
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

    public static List<Integer> getOptimalBufferPositions() {
        return null;
    }
}