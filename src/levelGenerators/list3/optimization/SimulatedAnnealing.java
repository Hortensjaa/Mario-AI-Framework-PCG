package levelGenerators.list3.optimization;

import engine.core.MarioAgent;
import levelGenerators.list3.evaluation.Evaluation;
import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.structure.LevelStructure;

import java.util.function.Supplier;

public abstract class SimulatedAnnealing extends OptimizationAlgorithm {
    private static final float TEMP_START = 10.0f;
    private static final float TEMP_END = 0.005f;
    private static final float ALPHA = 0.98f;
    private static final int N = 1000;

    //    level with the best score according to heuristic evaluation and its score (heuristic only)
    protected float bestScoreHeuristic = Integer.MIN_VALUE;
    protected LevelStructure bestLevelHeuristic;

    //    current evaluated level and its score (heuristic only)
    protected float curScore;
    protected LevelStructure curLevel;

    //      level with the best cumulative score (heuristic + simulation) and its scores
    protected float bestCumulativeScore = Integer.MIN_VALUE;            // heuristic + simulation
    protected float bestCumulativeHeuristicScore = Integer.MIN_VALUE;   // heuristic only
    protected float bestCumulativeSimulationScore = Integer.MIN_VALUE;  // simulation only
    protected LevelStructure bestCumulativeLevel;

    private float curTemp = TEMP_START;


    public LevelStructure getBestLevel(
            Weights weights,
            Supplier<MarioAgent> agent,
            int maxTries,
            int simulateEvery
    ) {
        curLevel = generateSingleLevel();
        bestLevelHeuristic = curLevel;
        curScore = Evaluation.heuristic(curLevel, weights);
        int j = 0;
        for (curTemp = TEMP_START; curTemp > TEMP_END; curTemp *= ALPHA) {
            for (int i = 0; i < N; i++) {
                LevelStructure candidate = mutateLevel(curLevel);
                float candidateScore = Evaluation.heuristic(candidate, weights);
                float diff = candidateScore - curScore;
                if (diff > 0 || Math.exp(diff / curTemp) > rng.nextDouble()) {
                    curLevel = candidate;
                    curScore = candidateScore;
                    if (curScore > bestScoreHeuristic) {
                        bestLevelHeuristic = curLevel;
                        bestScoreHeuristic = curScore;
                    }
                }
            }
            j += 1;
            if (bestScoreHeuristic > 0 && j % simulateEvery == 0) {
                float res = Evaluation.repeatedSimulation(bestLevelHeuristic, agent, weights, 5);
                float cumulative =
                        (res / weights.getSimulationUpperBound()) * 0.7f
                        + (bestScoreHeuristic / weights.getHeuristicUpperBound()) * 0.3f;
                float diff = cumulative - bestCumulativeScore;
                // decide to accept new cumulative best level if it is better or if temp is high enough
                if (diff >= 0 || Math.exp(diff / curTemp) > rng.nextDouble()) {
                    System.out.println("New best level");
                    bestCumulativeScore = cumulative;
                    bestCumulativeHeuristicScore = bestScoreHeuristic;
                    bestCumulativeSimulationScore = res;
                    bestCumulativeLevel = bestLevelHeuristic;
                } else {
                    // revert to the best cumulative level if new level is very bad
                    if (bestCumulativeLevel != null) {
                        System.out.println("Reverting best level");
                        curLevel = bestCumulativeLevel;
                        bestLevelHeuristic = bestCumulativeLevel;
                        bestScoreHeuristic = bestCumulativeHeuristicScore;
                        curScore = bestCumulativeHeuristicScore;
                    }
                }
            }
        }
        if (bestCumulativeSimulationScore <= 0 && maxTries > 0) {
            reset();
            return getBestLevel(
                    weights,
                    agent,
                    maxTries - 1,
                    simulateEvery
            );
        }
        System.out.println("BEST LEVEL CHOOSED! Cumulative: " + bestCumulativeScore
                + " Heuristic: " + bestCumulativeHeuristicScore + " Simulation: " + bestCumulativeSimulationScore);
        if (bestCumulativeLevel != null) {
            float res = Evaluation.simulation(bestCumulativeLevel, agent.get(), weights);
            System.out.println("Re-evaluated simulation score: " + res);
            return bestCumulativeLevel;
        }
        System.out.println("Heuristic level: " + bestScoreHeuristic);
        return bestLevelHeuristic;
    }

    @Override
    public void reset() {
        super.reset();
        curTemp = TEMP_START;
        bestScoreHeuristic = Integer.MIN_VALUE;
        bestLevelHeuristic = null;
        curScore = Integer.MIN_VALUE;
        curLevel = null;
        bestCumulativeScore = Integer.MIN_VALUE;
        bestCumulativeHeuristicScore = Integer.MIN_VALUE;
        bestCumulativeSimulationScore = Integer.MIN_VALUE;
        bestCumulativeLevel = null;
    }
}
