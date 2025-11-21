package levelGenerators.list3.subtasks;

import levelGenerators.list3.evaluation.HeuristicComponent;
import levelGenerators.list3.evaluation.SimulationComponent;
import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.optimization.SimulatedAnnealing;
import levelGenerators.list3.structure.LevelStructure;

import java.util.Map;

import static java.util.Map.entry;

public class Subtask1 extends SimulatedAnnealing {
    @Override
    public LevelStructure getBestLevel() {
        reset();
        return getBestLevel(
                WEIGHTS,
                agents.robinBaumgarten.Agent::new,
                20,
                10
        );
    }

    @Override
    public String getGeneratorName() {
        return "SimulatedAnnealing1";
    }

    public static final Weights WEIGHTS = new Weights(
            100f, // heuristicUpperBound
            100f, // simulationUpperBound
            Map.ofEntries(
                    // ------------ diversity ------------
                    entry(HeuristicComponent.WIDER_TERRAINS_BONUS, 10f),
                    entry(HeuristicComponent.TERRAIN_DIVERSITY, 10f),
                    entry(HeuristicComponent.DECOR_DIVERSITY, 10f),
                    entry(HeuristicComponent.LOCAL_DECOR_DIVERSITY, 10f),
                    entry(HeuristicComponent.LOCAL_TERRAIN_DIVERSITY, 5f),
                    // ------------ decorators ------------
                    entry(HeuristicComponent.ENEMIES_COUNT, 5f),
                    entry(HeuristicComponent.BLOCKS_COUNT, 1f),
                    entry(HeuristicComponent.COINS_COUNT, 1f),
                    // ------------ terrains ------------
                    entry(HeuristicComponent.HILLS_COUNT, 2f),
                    entry(HeuristicComponent.BILLS_COUNT, 1f),
                    entry(HeuristicComponent.PIPES_COUNT, 2f),
                    entry(HeuristicComponent.GAPS_COUNT, 1f),
                    entry(HeuristicComponent.PLAINS_COUNT, 2f),
                    // ------------ penalties ------------
                    entry(HeuristicComponent.ENEMY_GROUPS, -5f),
                    entry(HeuristicComponent.ENEMY_FIRST, -25f),
                    entry(HeuristicComponent.NOT_PASSABLE_GAPS, -50f),
                    entry(HeuristicComponent.NOT_PASSABLE_JUMPS, -50f),
                    entry(HeuristicComponent.GAP_FIRST, -100f)
            ),
            Map.of(
                    SimulationComponent.COMPLETION, 50f,
                    SimulationComponent.KILLS, 3f,
                    SimulationComponent.JUMPS, 0.3f,
                    SimulationComponent.COINS, 0.1f,
                    SimulationComponent.NOT_PASSED, -100f
            )
    );
}
