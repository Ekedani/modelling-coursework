import core.*;

import java.util.ArrayList;
import java.util.List;

public class ConveyorModelFactory {
    public static Model createBasicConveyorModel() {
        final int processorsCount = 5;
        final double processorsDelayMean = 1;
        final int processorsChannelsCount = 1;
        final int processorsMaxQueueSize = 0;
        final double transportsDelayMean = 1;
        final double lastTransportDelayMean = 5;

        var packageCreate = new PackageCreate("PackageCreate", 1, 4);
        var processors = createNProcessors(processorsCount, processorsDelayMean, processorsChannelsCount, processorsMaxQueueSize);
        var transports = createNTransports(processorsCount, transportsDelayMean, lastTransportDelayMean);
        var dispose = new Dispose("Dispose");

        configureBasicConveyor(packageCreate, processors, transports, dispose);
        return null;
    }

    private static void configureBasicConveyor(PackageCreate packageCreate, List<ChannelProcess> processors, List<DelayProcess> transports, Dispose dispose) {
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
            currentTransport.addRoutes(
                    new Route(nextProcessor, 1, () -> nextProcessor.getState() == 1),
                    new Route(nextTransport, 0)
            );
        }
        for (var processor : processors) {
            processor.addRoutes(
                    new Route(dispose, 0)
            );
        }
    }

    private static List<ChannelProcess> createNProcessors(int n, double delayMean, int channelsCount, int maxQueueSize) {
        var processors = new ArrayList<ChannelProcess>();
        for (int i = 0; i < n; i++) {
            var name = "Processor " + (i + 1);
            var processor = new ChannelProcess(name, delayMean, channelsCount, maxQueueSize);
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
