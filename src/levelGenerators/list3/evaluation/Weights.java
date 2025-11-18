package levelGenerators.list3.evaluation;

import lombok.Getter;

import java.util.Map;

import static java.util.Map.entry;

@Getter
public class Weights {
    // Instance fields
    private final float heuristicUpperBound;
    private final float simulationUpperBound;
    private final Map<HeuristicComponent, Float> heuristicWeights;
    private final Map<SimulationComponent, Float> simulationWeights;

    // Constructor
    public Weights(
            float heuristicUpperBound,
            float simulationUpperBound,
            Map<HeuristicComponent, Float> heuristicWeights,
            Map<SimulationComponent, Float> simulationWeights
    ) {
        this.heuristicUpperBound = heuristicUpperBound;
        this.simulationUpperBound = simulationUpperBound;
        this.heuristicWeights = heuristicWeights;
        this.simulationWeights = simulationWeights;
    }

    // Predefined instances for different tasks
    public static final Weights SUBTASK1 = new Weights(
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
                    entry(HeuristicComponent.ENEMY_GROUPS, -5f),
                    entry(HeuristicComponent.ENEMY_FIRST, -25f),
                    entry(HeuristicComponent.NOT_PASSABLE_GAPS, -50f),
                    entry(HeuristicComponent.NOT_PASSABLE_JUMPS, -50f),
                    entry(HeuristicComponent.GAP_FIRST, -100f)
            ),
            Map.of(
                    SimulationComponent.COMPLETION, 50f,
                    SimulationComponent.KILLS, 15f,
                    SimulationComponent.JUMPS, 5f,
                    SimulationComponent.COINS, 5f,
                    SimulationComponent.NOT_PASSED, -100f
            )
    );

    public static final Weights SUBTASK2 = new Weights(
            60f, // heuristicUpperBound
            150f, // simulationUpperBound
            Map.ofEntries(
                    // ------------ diversity ------------
                    entry(HeuristicComponent.TERRAIN_DIVERSITY, 10f),
                    entry(HeuristicComponent.DECOR_DIVERSITY, 5f),
                    entry(HeuristicComponent.LOCAL_TERRAIN_DIVERSITY, 5f),
                    entry(HeuristicComponent.LOCAL_DECOR_DIVERSITY, 5f),
                    // ------------ decorators ------------
                    entry(HeuristicComponent.ENEMIES_COUNT, 10f),
                    entry(HeuristicComponent.BLOCKS_COUNT, 1f),
                    entry(HeuristicComponent.COINS_COUNT, 1f),
                    // ------------ terrains ------------
                    entry(HeuristicComponent.HILLS_COUNT, 3f),
                    entry(HeuristicComponent.BILLS_COUNT, 1f),
                    entry(HeuristicComponent.PIPES_COUNT, 3f),
                    entry(HeuristicComponent.GAPS_COUNT, 0f),
                    entry(HeuristicComponent.PLAINS_COUNT, 3f),
                    // ------------ penalties ------------
                    entry(HeuristicComponent.ENEMY_FIRST, -1f),
                    entry(HeuristicComponent.ENEMY_GROUPS, 0f),
                    entry(HeuristicComponent.NOT_PASSABLE_GAPS, -50f),
                    entry(HeuristicComponent.NOT_PASSABLE_JUMPS, -50f),
                    entry(HeuristicComponent.GAP_FIRST, -100f)
            ),
            Map.of(
                    SimulationComponent.COMPLETION, 50f,
                    SimulationComponent.KILLS, 25f,
                    SimulationComponent.JUMPS, 5f,
                    SimulationComponent.COINS, 5f,
                    SimulationComponent.NOT_PASSED, -100f
            )
    );

    public static final Weights SUBTASK3 = new Weights(
            60f, // heuristicUpperBound
            100f, // simulationUpperBound
            Map.ofEntries(
                    // ------------ diversity ------------
                    entry(HeuristicComponent.TERRAIN_DIVERSITY, 10f),
                    entry(HeuristicComponent.DECOR_DIVERSITY, 10f),
                    entry(HeuristicComponent.LOCAL_TERRAIN_DIVERSITY, 5f),
                    entry(HeuristicComponent.LOCAL_DECOR_DIVERSITY, 5f),
                    // ------------ decorators ------------
                    entry(HeuristicComponent.ENEMIES_COUNT, 3f),
                    entry(HeuristicComponent.BLOCKS_COUNT, 1f),
                    entry(HeuristicComponent.COINS_COUNT, 1f),
                    // ------------ terrains ------------
                    entry(HeuristicComponent.HILLS_COUNT, 3f),
                    entry(HeuristicComponent.BILLS_COUNT, 1f),
                    entry(HeuristicComponent.PIPES_COUNT, 3f),
                    entry(HeuristicComponent.GAPS_COUNT, 0.1f),
                    entry(HeuristicComponent.PLAINS_COUNT, 2f),
                    // ------------ penalties ------------
                    entry(HeuristicComponent.ENEMY_FIRST, -5f),
                    entry(HeuristicComponent.ENEMY_GROUPS, -5f),
                    entry(HeuristicComponent.NOT_PASSABLE_GAPS, -50f),
                    entry(HeuristicComponent.NOT_PASSABLE_JUMPS, -50f),
                    entry(HeuristicComponent.GAP_FIRST, -100f)
            ),
            Map.of(
                    SimulationComponent.COMPLETION, 50f,
                    SimulationComponent.KILLS, 15f,
                    SimulationComponent.JUMPS, 5f,
                    SimulationComponent.COINS, 5f,
                    SimulationComponent.NOT_PASSED, -100f
            )
    );
}

