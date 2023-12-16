public class Main {
    public static void main(String[] args) {
        observeBasicModelOutputs();
    }

    public static void observeBasicModelOutputs() {
        var basicConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        ExperimentHelper.observeOutput(basicConveyorModel, 1500, 25).forEach((factor, values) -> {
            System.out.print(factor + ": ");
            values.forEach(value -> System.out.print(value + "; "));
            System.out.println();
        });
    }
}