package core;

public class PackageCreate extends Create {
    private final int packageSize;

    public PackageCreate(String name, int delayMean, int packageSize) {
        super(name, delayMean);
        this.setTNext(0.0);
        this.packageSize = packageSize;
    }

    @Override
    public void outAct() {
        for (int i = 0; i < packageSize; i++) {
            var job = new Job(getTCurr());
            var nextRoute = getNextRoute();
            if (nextRoute != null) {
                var destination = nextRoute.getDestination();
                destination.inAct(job);
                changeQuantity(1);
            } else {
                changeFailures(1);
            }
        }
        setTNext(getTCurr() + getDelay());
    }

    @Override
    public void printInfo() {
        System.out.println("CreatePackage: " + getName());
    }

    @Override
    public void printResult() {
        System.out.println("Package size: " + packageSize);
        System.out.println("Failures: " + getFailures());
    }

    @Override
    public void doStatistics(double delta) {
    }
}
