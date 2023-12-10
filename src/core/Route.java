package core;

public class Route {
    private Element destination = null;
    private int priority = 0;
    private BlockCondition block = null;

    public Route() {
    }

    public Route(Element destination) {
        this.destination = destination;
    }

    public Route(Element destination, int priority) {
        this.destination = destination;
        this.priority = priority;
    }

    public Route(Element destination, int priority, BlockCondition block) {
        this.destination = destination;
        this.priority = priority;
        this.block = block;
    }

    public Element getDestination() {
        return destination;
    }

    public int getPriority() {
        return priority;
    }

    public Boolean isBlocked() {
        if (block == null) {
            return false;
        }
        return block.call();
    }

    @FunctionalInterface
    public interface BlockCondition {
        Boolean call();
    }
}
