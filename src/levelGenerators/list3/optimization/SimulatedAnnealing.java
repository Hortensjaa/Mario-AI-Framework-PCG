package levelGenerators.list3.optimization;

import levelGenerators.list3.evaluation.Evaluation;
import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.structure.Decorator;
import levelGenerators.list3.structure.LevelStructure;
import levelGenerators.list3.structure.Terrain;

import java.util.List;

public class SimulatedAnnealing extends OptimizationAlgorithm {
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
    private LevelStructure bestCumulativeLevel;


    public LevelStructure getBestLevel() {
        curLevel = generateSingleLevel();
        bestLevelHeuristic = curLevel;
        curScore = Evaluation.task1heuristic(curLevel);
        for (curTemp = TEMP_START; curTemp > TEMP_END; curTemp *= ALPHA) {
            for (int i = 0; i < N; i++) {
                LevelStructure candidate = mutateLevel(curLevel);
                float candidateScore = Evaluation.task1heuristic(candidate);
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
            if (bestScoreHeuristic > 0) {
                float res = Evaluation.task1simulation(bestLevelHeuristic);
                float cumulative =
                        (res/ Weights.TASK1_HEURISTIC_UPPER_BOUND) * 0.7f
                        + (bestScoreHeuristic/ Weights.TASK1_HEURISTIC_UPPER_BOUND) * 0.3f;
                float diff = cumulative - bestCumulativeScore;
                if (diff > 0 || Math.exp(diff / curTemp) > rng.nextDouble()) {
                    bestCumulativeScore = cumulative;
                    bestCumulativeHeuristicScore = bestScoreHeuristic;
                    bestCumulativeLevel = bestLevelHeuristic;
                }
            }
            System.out.println("Temp: " + curTemp + " Best score: " + bestCumulativeHeuristicScore + " Simulation: " + bestCumulativeScore);
        }
        return bestCumulativeLevel;
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

    @Override
    public String getGeneratorName() {
        return "SimulatedAnnealingGenerator";
    }
}
