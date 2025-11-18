package levelGenerators.list3.optimization;

import engine.core.MarioAgent;
import levelGenerators.list3.evaluation.Evaluation;
import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.structure.Decorator;
import levelGenerators.list3.structure.LevelStructure;
import levelGenerators.list3.structure.Terrain;

import java.util.List;
import java.util.function.Supplier;

public abstract class SimulatedAnnealing extends OptimizationAlgorithm {
    private static final float TEMP_START = 10.0f;
    private static final float TEMP_END = 0.001f;
    private static final float ALPHA = 0.97f;
    private static final int N = 1000;

    private float curTemp = TEMP_START;

    private float bestScoreHeuristic = Integer.MIN_VALUE;
    private LevelStructure bestLevelHeuristic;

    private float curScore;
    private LevelStructure curLevel;

    private float bestCumulativeScore = Integer.MIN_VALUE;
    private float bestCumulativeHeuristicScore = Integer.MIN_VALUE;
    private float bestCumulativeSimulationScore = Integer.MIN_VALUE;
    private LevelStructure bestCumulativeLevel;


    public LevelStructure getBestLevel(
            Weights weights,
            Supplier<MarioAgent> agent,
            int maxTries
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
            if (bestScoreHeuristic > 0 && j % 100 == 0) {
                float res = Evaluation.simulation(bestLevelHeuristic, agent.get(), weights);
                float cumulative =
                        (res / weights.getSimulationUpperBound()) * 0.7f
                        + (bestScoreHeuristic / weights.getHeuristicUpperBound()) * 0.3f;
                float diff = cumulative - bestCumulativeScore;
                if (diff > 0 || (Math.exp(diff / curTemp) > rng.nextDouble() && cumulative * bestCumulativeScore > 0)) {
                    bestCumulativeScore = cumulative;
                    bestCumulativeHeuristicScore = bestScoreHeuristic;
                    bestCumulativeSimulationScore = res;
                    bestCumulativeLevel = bestLevelHeuristic;
                }
            }
        }
        if (bestCumulativeSimulationScore <= 0 && maxTries > 0) {
            rng.setSeed(System.currentTimeMillis());
            return getBestLevel(
                    weights,
                    agent,
                    maxTries - 1
            );
        }
        System.out.println("BEST LEVEL CHOOSED! Cumulative: " + bestCumulativeScore
                + " Heuristic: " + bestCumulativeHeuristicScore + " Simulation: " + bestCumulativeSimulationScore);
        if (bestCumulativeLevel != null) {
            return bestCumulativeLevel;
        }
        System.out.println("Heuristic level: " + bestScoreHeuristic);
        return bestLevelHeuristic;
    }

    @Override
    protected LevelStructure mutateLevel(LevelStructure level) {
        List<Decorator> decorators = level.getDecorators();
        List<Terrain> terrains = level.getTerrains();
        int r = rng.nextInt(6);
        switch (r) {
            case 0 -> decorators = swapDecorators(decorators);
            case 1 -> terrains = swapTerrains(terrains);
            case 2 -> decorators = replaceDecorator(decorators);
            case 3 -> terrains = replaceTerrain(terrains);
            case 4 -> decorators = mutateRandomDecorators(decorators);
            case 5 -> terrains = mutateRandomTerrains(terrains);
        }
        return new LevelStructure(terrains, decorators);
    }
}
