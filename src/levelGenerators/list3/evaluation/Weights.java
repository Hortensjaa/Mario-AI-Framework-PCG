package levelGenerators.list3.evaluation;

import lombok.Getter;

import java.util.Map;

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
}

