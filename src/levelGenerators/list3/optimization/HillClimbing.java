package levelGenerators.list3.optimization;

import levelGenerators.list3.evaluation.Evaluation;
import levelGenerators.list3.structure.Decorator;
import levelGenerators.list3.structure.LevelStructure;
import levelGenerators.list3.structure.Terrain;
import levelGenerators.list3.subtasks.Subtask1;

import java.util.List;

public class HillClimbing extends OptimizationAlgorithm {
    protected final static int GENERATIONS = 10000;
    protected final static float INITIAL_MUTATION_CHANCE = 10.0f;
    protected final static float MUTATION_CHANCE_MULTIPLIER = 0.999f;
    protected final static float MIN_MUTATION_CHANCE = 0.1f;
    protected final static float SMALL_MUTATIONS_THRESHOLD = 0.1f;

    private float mutationChance = INITIAL_MUTATION_CHANCE;
    private float bestScore = Integer.MIN_VALUE;
    private LevelStructure bestLevel;
    private float curScore;
    private LevelStructure curLevel;

    @Override
    public LevelStructure getBestLevel() {
        curLevel = generateSingleLevel();
        bestLevel = curLevel;
        curScore = Evaluation.heuristic(curLevel, Subtask1.WEIGHTS);
        int generation = 0;
        while (generation < GENERATIONS) {
            LevelStructure mutatedLevel = mutateLevel(curLevel);
            float mutatedScore = Evaluation.heuristic(mutatedLevel, Subtask1.WEIGHTS);
            if (mutatedScore > curScore) {
                curLevel = mutatedLevel;
                curScore = mutatedScore;
                if (curScore > bestScore) {
                    bestLevel = curLevel;
                    bestScore = curScore;
                }
            }
            mutationChance *= MUTATION_CHANCE_MULTIPLIER;
            generation += 1;
        }
        return bestLevel;
    }

    @Override
    protected LevelStructure mutateLevel(LevelStructure level) {
        List<Decorator> decorators = level.getDecorators();
        List<Terrain> terrains = level.getTerrains();

        if (rng.nextFloat() < mutationChance) {
            decorators = swapDecorators(decorators);
        }
        if (rng.nextFloat() < mutationChance) {
            terrains = swapTerrains(terrains);
        }
        if (rng.nextFloat() < mutationChance) {
            decorators = replaceDecorator(decorators);
        }
        if (rng.nextFloat() < mutationChance) {
            terrains = replaceTerrain(terrains);
        }

        // close to the end of evolution embrace small mutations
        if (mutationChance < SMALL_MUTATIONS_THRESHOLD) {
            terrains.forEach(terrain -> {
                if (rng.nextFloat() < mutationChance) {
                    terrain.mutate();
                }
            });
            decorators.forEach(decorator -> {
                if (rng.nextFloat() < mutationChance) {
                    decorator.mutate();
                }
            });
        }

        return new LevelStructure(terrains, decorators);
    }

    @Override
    public String getGeneratorName() {
        return "HillClimberGenerator";
    }
}
