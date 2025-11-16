package levelGenerators.list3.optimization;

import levelGenerators.list3.Evaluation;
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
    private float bestScore = Integer.MIN_VALUE;
    private LevelStructure bestLevel;
    private float curScore;
    private LevelStructure curLevel;


    @Override
    public LevelStructure getBestLevel() {
        curLevel = generateSingleLevel();
        bestLevel = curLevel;
        curScore = Evaluation.task1Score(curLevel);
        for (curTemp = TEMP_START; curTemp > TEMP_END; curTemp *= ALPHA) {
            for (int i = 0; i < N; i++) {
                LevelStructure candidate = mutateLevel(curLevel);
                float candidateScore = Evaluation.task1Score(candidate);
                float diff = candidateScore - curScore;
                if (diff > 0 || Math.exp(diff / curTemp) > rng.nextDouble()) {
                    curLevel = candidate;
                    curScore = candidateScore;
                    if (curScore > bestScore) {
                        bestLevel = curLevel;
                        bestScore = curScore;
                    }
                }
            }
        }
        while (bestScore < 0) {
            LevelStructure candidate = mutateLevel(curLevel);
            float candidateScore = Evaluation.task1Score(candidate);
            if (candidateScore > bestScore) {
                bestLevel = candidate;
                bestScore = candidateScore;
            }
        }
        System.out.println(bestScore);
        return bestLevel;
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
