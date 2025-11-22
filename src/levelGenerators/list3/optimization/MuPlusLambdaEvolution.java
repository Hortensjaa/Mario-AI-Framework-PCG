package levelGenerators.list3.optimization;

import engine.core.MarioAgent;
import levelGenerators.list3.evaluation.Evaluation;
import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.structure.LevelStructure;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class MuPlusLambdaEvolution extends OptimizationAlgorithm {

    int MU = 5;
    int LAMBDA = 20;
    int N = 100;
    int REPEAT_SIM = 5;
    int SIMULATE_EVERY = 50;

    List<LevelStructure> population;

    // --- PUBLIC INTERFACE ---------------------------------------------------------
    /** Single-agent version */
    public LevelStructure getBestLevel(
            Weights weights,
            Supplier<MarioAgent> agent,
            int maxRetries
    ) {
        return runEvolution(
                weights,
                maxRetries,
                level -> Evaluation.repeatedSimulation(level, agent, weights, REPEAT_SIM)
        );
    }

    /** Two-agent version */
    public LevelStructure getBestLevel(
            Weights weights,
            Supplier<MarioAgent> agent1,
            Supplier<MarioAgent> agent2,
            int maxRetries
    ) {
        return runEvolution(
                weights,
                maxRetries,
                level -> average(
                        Evaluation.repeatedSimulation(level, agent1, weights, REPEAT_SIM),
                        Evaluation.repeatedSimulation(level, agent2, weights, REPEAT_SIM)
                )
        );
    }

    // --- EVOLUTION LOOP -----------------------------------------------------

    private LevelStructure runEvolution(
            Weights weights,
            int maxRetries,
            Function<LevelStructure, Float> simulationFn
    ) {
        population = generateMultipleLevels(MU + LAMBDA);
        Map<LevelStructure, Float> scores = new ConcurrentHashMap<>();

        for (int iteration = 1; iteration < N; iteration++) {
            scores.clear();
            evaluatePopulation(scores, weights, iteration, simulationFn);
            evolvePopulation(scores);
        }

        finalEvaluation(scores, simulationFn);
        return retryOrReturn(weights, maxRetries, simulationFn, scores);
    }

    // --- HELPERS ------------------------------------------------------------

    private void evaluatePopulation(
            Map<LevelStructure, Float> scores,
            Weights weights,
            int iteration,
            Function<LevelStructure, Float> simulationFn
    ) {
        if (iteration % SIMULATE_EVERY == 0) {
            population.parallelStream().forEach(level -> {
                float score = simulationFn.apply(level);
                scores.put(level, score);
                System.out.println("Level score: " + score + " in iteration: " + iteration);
            });
        } else {
            population.parallelStream().forEach(level ->
                    scores.put(level, Evaluation.heuristic(level, weights))
            );
            recorder.record(scores, iteration);
        }
    }

    private void evolvePopulation(Map<LevelStructure, Float> scores) {
        population.sort(Comparator.comparing(scores::get).reversed());

        for (int i = 0; i < MU; i++) {
            LevelStructure parent = population.get(i);
            for (int j = 0; j < LAMBDA / MU; j++) {
                LevelStructure child = mutateLevel(parent);
                population.set(MU + i * (LAMBDA / MU) + j, child);
            }
        }
    }

    private void finalEvaluation(
            Map<LevelStructure, Float> scores,
            Function<LevelStructure, Float> finalSimulationFn
    ) {
        scores.clear();
        population.parallelStream().forEach(level -> {
            float score = finalSimulationFn.apply(level);
            scores.put(level, score);
            System.out.println("Level score: " + score + " at FINAL iteration");
        });
    }

    private LevelStructure retryOrReturn(
            Weights weights,
            int maxRetries,
            Function<LevelStructure, Float> simulationFn,
            Map<LevelStructure, Float> scores
    ) {
        LevelStructure best = population.stream()
                .max(Comparator.comparing(scores::get))
                .orElse(null);

        if (best == null) {
            System.out.println("sth went wrong...");
            return null;
        }

        float bestScore = scores.get(best);
        System.out.println("Best level score: " + bestScore);

        if (bestScore > 0 || maxRetries <= 0) {
            return best;
        }

        reset();
        System.out.println("Retrying...");
        return runEvolution(weights, maxRetries - 1, simulationFn);
    }

    private float average(float a, float b) {
        return (a + b) / 2f;
    }

    @Override
    public void reset() {
        super.reset();
        population = null;
    }
}
