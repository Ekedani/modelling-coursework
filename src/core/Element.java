package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public abstract class Element {
    private static int nextId = 0;
    private final int id;
    private final ArrayList<Route> routes;
    private final String name;
    private Distribution distribution;
    private double tCurr = 0.0;
    private double tNext = Double.MAX_VALUE;
    private double delayMean = 0.0;
    private double delayDev = 0.0;
    private int quantity = 0;


    public Element(String name) {
        this.id = nextId++;
        this.name = name;
        this.routes = new ArrayList<>();
        this.distribution = Distribution.NONE;
    }

    public static void resetNextId() {
        nextId = 0;
    }

    public abstract void inAct(Job job);

    public abstract void outAct();

    public abstract void printInfo();

    public abstract void printResult();

    public abstract void doStatistics(double delta);

    public double getDelay() {
        return switch (distribution) {
            case EXPONENTIAL -> FunRand.Exponential(delayMean);
            default -> delayMean;
        };
    }

    public double getDelayMean() {
        return delayMean;
    }

    public void setDelayMean(double delayMean) {
        this.delayMean = delayMean;
    }

    public double getDelayDev() {
        return delayDev;
    }

    public void setDelayDev(double delayDev) {
        this.delayDev = delayDev;
    }

    public int getQuantity() {
        return quantity;
    }

    public void changeQuantity(int delta) {
        this.quantity += delta;
    }

    public double getTCurr() {
        return tCurr;
    }

    public void setTCurr(double tCurr) {
        this.tCurr = tCurr;
    }

    public double getTNext() {
        return tNext;
    }

    public void setTNext(double tNext) {
        this.tNext = tNext;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

    /**
     * Returns the next highest priority route that is not blocked. If there are several routes with the same priority,
     * then one of them is selected by equal probability. If all routes are blocked, then null is returned.
     *
     * @return next route
     */
    public Route getNextRoute() {
        var unblockedRoutes = routes.stream().filter(Predicate.not(Route::isBlocked)).toList();
        if (!unblockedRoutes.isEmpty()) {
            var highestPriority = unblockedRoutes.get(0).getPriority();
            var highestPriorityRoutes = unblockedRoutes.stream().filter(route -> route.getPriority() == highestPriority).toList();
            return highestPriorityRoutes.get((int) (Math.random() * highestPriorityRoutes.size()));
        }
        return null;
    }

    /**
     * Adds routes to the element and sorts them by priority in descending order. The higher the priority, the earlier
     * the route will be processed.
     *
     * @param routes routes to add
     */
    public void addRoutes(Route... routes) {
        Collections.addAll(this.routes, routes);
        this.routes.sort((a, b) -> b.getPriority() - a.getPriority());
    }
}
