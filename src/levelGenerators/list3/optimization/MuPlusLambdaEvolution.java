package levelGenerators.list3.optimization;

import engine.core.MarioAgent;
import levelGenerators.list3.evaluation.Evaluation;
import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.structure.LevelStructure;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public abstract class MuPlusLambdaEvolution extends OptimizationAlgorithm {
    int MU = 5;
    int LAMBDA = 20;
    int N = 100;
    int REPEAT_N = 5;

    List<LevelStructure> population;

    public LevelStructure getBestLevel(
            Weights weights,
            Supplier<MarioAgent> agent,
            int maxTries,
            int simulateEvery
    ) {
        population = generateMultipleLevels(MU + LAMBDA);
        Map<LevelStructure, Float> scores = new ConcurrentHashMap<>();
        for (int iteration = 0; iteration < N; iteration++) {
            scores.clear();
            // evaluate all levels in population
            if (iteration % simulateEvery == 0) {
                population.parallelStream().forEach(level -> {
                    float score = Evaluation.simulation(level, agent.get(), weights);
                    scores.put(level, score);
                    System.out.println("Level score: " + score + " in loop");
                });
            } else {
                population.parallelStream().forEach(level ->
                        scores.put(level, Evaluation.heuristic(level, weights))
                );
            }
            // sort population by score
            population.sort(Comparator.comparing(scores::get).reversed());
            // replace the worst LAMBDA levels with mutated "children" of the best MU levels
            for (int i = 0; i < MU; i++) {
                LevelStructure parent = population.get(i);
                for (int j = 0; j < LAMBDA / MU; j++) {
                    LevelStructure child = mutateLevel(parent);
                    population.set(MU + i * (LAMBDA / MU) + j, child);
                }
            }
        }

        // final simulation
        scores.clear();
        population.parallelStream().forEach(level -> {
            float score = Evaluation.repeatedSimulation(level, agent, weights, REPEAT_N);
            scores.put(level, score);
            System.out.println("Level score: " + score + " at FINAL iteration");
        });
        LevelStructure best =  population.stream().max(Comparator.comparing(scores::get)).orElse(null);
        if (best != null) {
            if (scores.get(best) > 0 || maxTries <= 0) {
                float bestScore = scores.get(best);
                System.out.println("Best level score: " + bestScore);
                return best;
            } else {
                System.out.println("Best level score: " + scores.get(best) + ", retrying...");
                return getBestLevel(weights, agent, maxTries - 1, simulateEvery);
            }
        }
        System.out.println("sth went wrong...");
        return null;
    }
}
