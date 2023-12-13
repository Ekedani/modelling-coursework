package core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class ChannelProcess extends Process {
    private final List<Channel> channels = new ArrayList<>();
    private final Queue<Job> queue = new ArrayDeque<>();
    private final int maxQueueSize;
    private double queuesSum = 0.0;

    public ChannelProcess(String name, double delayMean, int channelsCount, int maxQueueSize) {
        super(name, delayMean);
        for (int i = 0; i < channelsCount; i++) {
            channels.add(new Channel());
        }
        this.maxQueueSize = maxQueueSize;
    }

    @Override
    public int getNumberOfWorkingChannels() {
        int result = 0;
        for (Channel channel : channels) {
            result += channel.getState();
        }
        return result;
    }

    @Override
    public int getState() {
        int state = 0;
        for (Channel channel : channels) {
            state |= channel.getState();
        }
        return state;
    }

    @Override
    public void inAct(Job job) {
        var freeChannel = getFreeChannel();
        if (freeChannel != null) {
            freeChannel.setCurrentJob(job);
            freeChannel.setTNext(getTCurr() + getDelay());
        } else if (queue.size() < maxQueueSize) {
            queue.add(job);
        } else {
            changeFailures(1);
        }
    }

    @Override
    public void outAct() {
        var channelsWithMinTNext = getChannelsWithMinTNext();
        for (Channel channel : channelsWithMinTNext) {
            var job = channel.getCurrentJob();
            var nextRoute = getNextRoute();
            if (nextRoute != null) {
                var destination = nextRoute.getDestination();
                destination.inAct(job);
            }
            changeQuantity(1);
            channel.setTNext(Double.MAX_VALUE);
            channel.setCurrentJob(null);
        }
        var freeChannels = getFreeChannels();
        for (Channel channel : freeChannels) {
            if (queue.size() > 0) {
                var job = queue.poll();
                channel.setCurrentJob(job);
                channel.setTNext(getTCurr() + getDelay());
            }
        }
    }

    @Override
    public void printInfo() {
    }

    @Override
    public void printResult() {
        System.out.println(
                getName() + ": " +
                        "Quantity = " + getQuantity() +
                        ", Failures: " + getFailures() +
                        ", Average queue size: " + queuesSum / getTCurr() +
                        ", Average workload: " + getWorkTime() / getTCurr()
        );

    }

    @Override
    public void doStatistics(double delta) {
        super.doStatistics(delta);
        queuesSum += queue.size() * delta;
    }

    @Override
    public double getTNext() {
        var tNext = Double.MAX_VALUE;
        for (Channel channel : channels) {
            if (channel.getTNext() < tNext) {
                tNext = channel.getTNext();
            }
        }
        return tNext;
    }

    private Channel getFreeChannel() {
        for (Channel channel : channels) {
            if (channel.getState() == 0) {
                return channel;
            }
        }
        return null;
    }

    private List<Channel> getFreeChannels() {
        return channels.stream().filter(channel -> channel.getState() == 0).collect(Collectors.toList());
    }

    private List<Channel> getChannelsWithMinTNext() {
        var minTNext = getTNext();
        return channels.stream().filter(channel -> channel.getTNext() == minTNext).collect(Collectors.toList());
    }

    public int getQueueSize() {
        return queue.size();
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    private static class Channel {
        private Job currentJob = null;
        private double tNext = Double.MAX_VALUE;

        public int getState() {
            return currentJob == null ? 0 : 1;
        }

        public Job getCurrentJob() {
            return currentJob;
        }

        public void setCurrentJob(Job currentJob) {
            this.currentJob = currentJob;
        }

        public double getTNext() {
            return tNext;
        }

        public void setTNext(double tNext) {
            this.tNext = tNext;
        }
    }
}
