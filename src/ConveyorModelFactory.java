import core.*;

import java.util.ArrayList;
import java.util.List;

public class ConveyorModelFactory {
    public static Model createBasicConveyorModel() {
        final int packageCreateDelayMean = 1;
        final int packageSize = 4;
        final int processorsCount = 5;
        final double processorsDelayMean = 1;
        final int processorsChannelsCount = 1;
        final int processorsMaxQueueSize = 0;
        final double transportsDelayMean = 1;
        final double lastTransportDelayMean = 5;

        return createCustomConveyorModel(
                packageCreateDelayMean,
                packageSize,
                processorsCount,
                processorsDelayMean,
                processorsChannelsCount,
                processorsMaxQueueSize,
                transportsDelayMean,
                lastTransportDelayMean
        );
    }

    public static Model createCustomConveyorModel(
            int packageCreateDelayMean,
            int packageSize,
            int processorsCount,
            double processorsDelayMean,
            int processorsChannelsCount,
            List<Integer> processorsMaxQueueSizes,
            double transportsDelayMean,
            double lastTransportDelayMean
    ) {
        var packageCreate = new PackageCreate("Package Creator", packageCreateDelayMean, packageSize);
        var processors = createNProcessors(processorsCount, processorsDelayMean, processorsChannelsCount, processorsMaxQueueSizes);
        var transports = createNTransports(processorsCount, transportsDelayMean, lastTransportDelayMean);
        var dispose = new Dispose("Dispose");

        configureConveyor(packageCreate, processors, transports, dispose);

        var elements = new ArrayList<Element>();
        elements.add(packageCreate);
        elements.addAll(processors);
        elements.addAll(transports);
        elements.add(dispose);
        return new Model(elements);
    }

    public static Model createCustomConveyorModel(
            int packageCreateDelayMean,
            int packageSize,
            int processorsCount,
            double processorsDelayMean,
            int processorsChannelsCount,
            int processorsMaxQueueSize,
            double transportsDelayMean,
            double lastTransportDelayMean
    ) {
        var processorsMaxQueueSizes = new ArrayList<Integer>();
        for (int i = 0; i < processorsCount; i++) {
            processorsMaxQueueSizes.add(processorsMaxQueueSize);
        }
        return createCustomConveyorModel(
                packageCreateDelayMean,
                packageSize,
                processorsCount,
                processorsDelayMean,
                processorsChannelsCount,
                processorsMaxQueueSizes,
                transportsDelayMean,
                lastTransportDelayMean
        );
    }

    private static void configureConveyor(
            PackageCreate packageCreate,
            List<ChannelProcess> processors,
            List<DelayProcess> transports,
            Dispose dispose
    ) {
        var conveyorSize = processors.size();
        var firstProcessor = processors.get(0);
        packageCreate.addRoutes(
                new Route(processors.get(0), 1, () -> firstProcessor.getState() == 1),
                new Route(transports.get(0), 0)
        );
        for (int i = 0; i < conveyorSize; i++) {
            var currentTransport = transports.get(i);
            var nextProcessor = processors.get((i + 1) % conveyorSize);
            var nextTransport = transports.get((i + 1) % conveyorSize);
            if (nextProcessor.getMaxQueueSize() == 0) {
                currentTransport.addRoutes(
                        new Route(nextProcessor, 1, () -> nextProcessor.getState() == 1),
                        new Route(nextTransport, 0)
                );
            } else {
                currentTransport.addRoutes(
                        new Route(nextProcessor, 1, () -> nextProcessor.getQueueSize() == nextProcessor.getMaxQueueSize()),
                        new Route(nextTransport, 0)
                );
            }

        }
        processors.forEach(processor -> processor.addRoutes(new Route(dispose, 0)));
    }

    private static List<ChannelProcess> createNProcessors(int n, double delayMean, int channelsCount, List<Integer> maxQueueSizes) {
        if (maxQueueSizes.size() != n) {
            throw new IllegalArgumentException("maxQueueSizes size must be equal to n");
        }
        var processors = new ArrayList<ChannelProcess>();
        for (int i = 0; i < n; i++) {
            var name = "Processor " + (i + 1);
            var processor = new ChannelProcess(name, delayMean, channelsCount, maxQueueSizes.get(i));
            processor.setDistribution(Distribution.EXPONENTIAL);
            processors.add(processor);
        }
        return processors;
    }

    private static List<DelayProcess> createNTransports(int n, double delayMean, double lastDelayMean) {
        var transports = new ArrayList<DelayProcess>();
        for (int i = 0; i < n; i++) {
            var name = "Transport " + (i + 1) + " -> " + (((i + 2) % n == 0) ? 5 : (i + 2) % n);
            var transport = new DelayProcess(name, delayMean);
            transports.add(transport);
        }
        transports.get(n - 1).setDelayMean(lastDelayMean);
        return transports;
    }
}
