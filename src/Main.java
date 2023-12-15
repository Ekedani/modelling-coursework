public class Main {
    public static void main(String[] args) {
      /*  var basicConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        basicConveyorModel.simulate(10000);
        basicConveyorModel.printResults();*/

        var basicConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        ExperimentHelper.observeOutput(basicConveyorModel, 1000, 25).forEach((factor, values) -> {
            System.out.println(factor + ": " + values);
        });

        /*var customConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        var oneBufferBasicConveyorModel = ConveyorModelFactory.createCustomConveyorModel(
                1,
                4,
                5,
                1,
                1,
                1,
                1,
                5
        );

        customConveyorModel.simulate(2500);
        oneBufferBasicConveyorModel.simulate(2500);

        var results1 = customConveyorModel.getResults();
        var results2 = oneBufferBasicConveyorModel.getResults();
        ExperimentHelper.compareResults(results2, results1).forEach((factor, difference) -> {
            System.out.println(factor + " difference: " + difference[0] + " => " + difference[1] + "%");
        });*/
    }
}