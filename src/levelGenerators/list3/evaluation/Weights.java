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
    private final Map<SimulationComponent, Float> simulationWeights2;

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
        this.simulationWeights2 = null;
    }

    // Constructor
    public Weights(
            float heuristicUpperBound,
            float simulationUpperBound,
            Map<HeuristicComponent, Float> heuristicWeights,
            Map<SimulationComponent, Float> simulationWeights,
            Map<SimulationComponent, Float> simulationWeights2
    ) {
        this.heuristicUpperBound = heuristicUpperBound;
        this.simulationUpperBound = simulationUpperBound;
        this.heuristicWeights = heuristicWeights;
        this.simulationWeights = simulationWeights;
        this.simulationWeights2 = simulationWeights2;
    }
}

