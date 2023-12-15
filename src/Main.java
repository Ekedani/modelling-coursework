public class Main {
    public static void main(String[] args) {
      /*  var basicConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        basicConveyorModel.simulate(10000);
        basicConveyorModel.printResults();*/

        var customConveyorModel = ConveyorModelFactory.createBasicConveyorModel();
        customConveyorModel.simulate(2500);
        customConveyorModel.printResults();
    }
}