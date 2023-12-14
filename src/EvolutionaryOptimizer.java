/*
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

interface FitnessFunction<T> {
    double evaluate(T chromosome);
}

public class EvolutionaryOptimizer<T> {
    private final int populationSize;
    private final int chromosomeLength;
    private final int maxGenerations;
    private final double crossoverRate;
    private final double mutationRate;
    private final FitnessFunction<T> fitnessFunction;
    private List<T> population;

    public EvolutionaryOptimizer(
            int populationSize,
            int chromosomeLength,
            int maxGenerations,
            double crossoverRate,
            double mutationRate,
            FitnessFunction<T> fitnessFunction
    ) {
        this.populationSize = populationSize;
        this.chromosomeLength = chromosomeLength;
        this.maxGenerations = maxGenerations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.fitnessFunction = fitnessFunction;
    }

    private T crossover(T parent1, T parent2) {
        Random random = new Random();

        // Perform two-point crossover
        int crossoverPoint1 = random.nextInt(chromosomeLength);
        int crossoverPoint2 = random.nextInt(chromosomeLength);

        if (crossoverPoint2 < crossoverPoint1) {
            int temp = crossoverPoint1;
            crossoverPoint1 = crossoverPoint2;
            crossoverPoint2 = temp;
        }

      */
/*  for (int i = 0; i < chromosomeLength; i++) {
            if (i >= crossoverPoint1 && i <= crossoverPoint2) {
                offspring[0][i] = parent2[i];
                offspring[1][i] = parent1[i];
            } else {
                offspring[0][i] = parent1[i];
                offspring[1][i] = parent2[i];
            }
        }*//*


        return parent1;
    }

    private void mutate(T[] chromosome) {
        Random random = new Random();
        for (int i = 0; i < chromosomeLength; i++) {
            if (random.nextDouble() < mutationRate) {
                chromosome[i] = (T) (Object) random.nextInt(1001);
            }
        }
    }

    private List<T> selectParents() {
        Random random = new Random();
        var parents = new ArrayList<T>(2);
        int index1 = random.nextInt(populationSize);
        int index2 = random.nextInt(populationSize);
        parents.add(population.get(index1));
        parents.add(population.get(index2));
        return parents;
    }

    private void evolve() {
        List<T> newPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i += 2) {
            List<T> parents = selectParents();

            if (Math.random() < crossoverRate) {
                T offspring = crossover(parents.get(0), parents.get(1));
                newPopulation[i] = offspring[0];
                newPopulation[i + 1] = offspring[1];
            } else {
                newPopulation[i] = parents[0];
                newPopulation[i + 1] = parents[1];
            }

            mutate(newPopulation[i]);
            mutate(newPopulation[i + 1]);
        }
        population = newPopulation;
    }

    public void run() {
        for (int generation = 1; generation <= maxGenerations; generation++) {
            evolve();
            System.out.println("Generation " + generation + ": Best solution = " + getBestSolution().toString());
        }
    }

    private T getBestSolution() {
        T bestSolution = population.get(0);
        double bestFitness = fitnessFunction.evaluate(bestSolution);
        for (int i = 1; i < populationSize; i++) {
            var currentSolution = population.get(i);
            double currentFitness = fitnessFunction.evaluate(currentSolution);
            if (currentFitness > bestFitness) {
                bestFitness = currentFitness;
                bestSolution = currentSolution;
            }
        }
        return bestSolution;
    }
}*/
