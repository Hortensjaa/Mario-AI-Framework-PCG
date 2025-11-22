package levelGenerators.list3.subtasks;

import levelGenerators.list3.evaluation.HeuristicComponent;
import levelGenerators.list3.evaluation.SimulationComponent;
import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.optimization.MuPlusLambdaEvolution;
import levelGenerators.list3.structure.LevelStructure;

import java.util.Map;

import static java.util.Map.entry;

public class Subtask3 extends MuPlusLambdaEvolution {
    @Override
    public LevelStructure getBestLevel() {
        reset();
        return getBestLevel(
                WEIGHTS,
                agents.killer.Agent::new,
                agents.collector.Agent::new,
                1
        );
    }

    public static final Weights WEIGHTS = new Weights(
            80f, // heuristicUpperBound
            25f, // simulationUpperBound
            Map.ofEntries(
                    // ------------ diversity ------------
                    entry(HeuristicComponent.WIDER_TERRAINS_BONUS, 15f),
                    entry(HeuristicComponent.TERRAIN_DIVERSITY, 5f),
                    entry(HeuristicComponent.DECOR_DIVERSITY, 10f),
                    entry(HeuristicComponent.LOCAL_TERRAIN_DIVERSITY, 5f),
                    entry(HeuristicComponent.LOCAL_DECOR_DIVERSITY, 10f),
                    // ------------ decorators ------------
                    entry(HeuristicComponent.ENEMY_GAP_DISTANCE_BONUS, 2f),
                    entry(HeuristicComponent.ENEMIES_COUNT, 1f),
                    entry(HeuristicComponent.BLOCKS_COUNT, 0f),
                    entry(HeuristicComponent.COINS_COUNT, 3f),
                    // ------------ terrains ------------
                    entry(HeuristicComponent.HILLS_COUNT, 3f),
                    entry(HeuristicComponent.BILLS_COUNT, 0f),
                    entry(HeuristicComponent.PIPES_COUNT, 3f),
                    entry(HeuristicComponent.GAPS_COUNT, 0f),
                    entry(HeuristicComponent.PLAINS_COUNT, 3f),
                    // ------------ penalties ------------
                    entry(HeuristicComponent.ENEMY_FIRST, -1f),
                    entry(HeuristicComponent.ENEMY_GROUPS, -5f),
                    entry(HeuristicComponent.NOT_PASSABLE_GAPS, -50f),
                    entry(HeuristicComponent.NOT_PASSABLE_JUMPS, -50f),
                    entry(HeuristicComponent.GAP_FIRST, -100f)
            ),
            Map.of(
                    SimulationComponent.COMPLETION, 10f,
                    SimulationComponent.KILLS, 1f,
                    SimulationComponent.JUMPS, 0.001f,
                    SimulationComponent.COINS, 0.1f,
                    SimulationComponent.NOT_PASSED, -100f
            )
    );

    @Override
    public String getGeneratorName() {
        return "MuPlusLambdaEvolution3";
    }
}

