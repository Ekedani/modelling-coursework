package core;

public class Job {
    private final double timeIn;
    private double timeOut = Double.MAX_VALUE;

    public Job(double timeIn) {
        this.timeIn = timeIn;
    }

    public double getTimeIn() {
        return timeIn;
    }

    public double getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(double timeOut) {
        this.timeOut = timeOut;
    }

    public double getTimeInModel() {
        return timeOut - timeIn;
    }
}
