package levelGenerators.list3.evaluation;

import java.util.Map;

import static java.util.Map.entry;

public final class Weights {
    public static float TASK1_HEURISTIC_UPPER_BOUND = 55f;
    public static float TASK1_SIMULATION_UPPER_BOUND = 100f;

    public static final Map<HeuristicComponent, Float> TASK1_HEURISTIC_WEIGHTS = Map.ofEntries(
            // ------------ diversity ------------
            entry(HeuristicComponent.TERRAIN_DIVERSITY, 10f),
            entry(HeuristicComponent.DECOR_DIVERSITY, 10f),
            entry(HeuristicComponent.LOCAL_TERRAIN_DIVERSITY, 10f),
            entry(HeuristicComponent.LOCAL_DECOR_DIVERSITY, 10f),
            // ------------ decorators ------------
            entry(HeuristicComponent.ENEMIES_COUNT, 3f),
            entry(HeuristicComponent.BLOCKS_COUNT, 1f),
            entry(HeuristicComponent.COINS_COUNT, 1f),
            // ------------ terrains ------------
            entry(HeuristicComponent.HILLS_COUNT, 2f),
            entry(HeuristicComponent.BILLS_COUNT, 1f),
            entry(HeuristicComponent.PIPES_COUNT, 2f),
            entry(HeuristicComponent.GAPS_COUNT, 1f),
            entry(HeuristicComponent.PLAINS_COUNT, 2f),
            // ------------ penalties ------------
            entry(HeuristicComponent.ENEMY_FIRST, -5f),
            entry(HeuristicComponent.ENEMY_GROUPS, -5f),
            entry(HeuristicComponent.NOT_PASSABLE_GAPS, -50f),
            entry(HeuristicComponent.NOT_PASSABLE_JUMPS, -50f),
            entry(HeuristicComponent.GAP_FIRST, -100f)
    );

    public static final Map<SimulationComponent, Float> TASK1_SIMULATION_WEIGHTS = Map.of(
            SimulationComponent.COMPLETION, 50f,
            SimulationComponent.KILLS, 15f,
            SimulationComponent.JUMPS, 5f,
            SimulationComponent.COINS, 5f,
            SimulationComponent.NOT_PASSED, -100f
    );
}

